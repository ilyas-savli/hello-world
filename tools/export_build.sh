#!/usr/bin/env bash
# Local build export (Stage E step E-OUT, Task mode "build me an APK"): builds the app, signs it, verifies it and
# copies it to a folder the user chose, as <project>-<flavor>-<buildType>-<versionName>-<versionCode>.apk/.aab
# (+ mapping.txt for release builds, + the TV file when app-tv/ exists). Copied into projects as tools/export_build.sh.
#
# Usage: export_build.sh --out <dir> [--type apk|aab|both] [--flavor prod] [--build-type release]
#                        [--create-keystore | --debug-sign] [--install] [--project <dir>]
# Signing (release): KEYSTORE_PATH/KEYSTORE_PASSWORD/KEY_ALIAS/KEY_PASSWORD from the environment, else
#   <project>.keystore.path/.storePassword/.keyAlias/.keyPassword in ~/.gradle/gradle.properties (never in the repo);
#   --create-keystore makes ~/.android-keystores/<project>.jks and stores those four properties (back it up!);
#   --debug-sign signs with the debug key: installable for testing, never uploadable to Play.
# Verification: apksigner verify + aapt2 badging (applicationId, versionCode) for APKs; bundletool validate (or a
#   jarsigner + structure check when bundletool isn't installed) for AABs; --install installs and launches the APK on
#   the device in ANDROID_SERIAL (or the only one connected). Exit 0 only when every file was built and verified.
set -euo pipefail

TYPE=apk; FLAVOR=prod; BUILD_TYPE=release; OUT=""; KEYSTORE_MODE=""; INSTALL=0; ROOT="$(pwd)"
while [ $# -gt 0 ]; do
  case "$1" in
    --out) OUT="$2"; shift 2 ;;
    --type) TYPE="$2"; shift 2 ;;
    --flavor) FLAVOR="$2"; shift 2 ;;
    --build-type) BUILD_TYPE="$2"; shift 2 ;;
    --create-keystore) KEYSTORE_MODE=create; shift ;;
    --debug-sign) KEYSTORE_MODE=debug; shift ;;
    --install) INSTALL=1; shift ;;
    --project) ROOT="$(cd "$2" && pwd)"; shift 2 ;;
    -h|--help) sed -n '2,15p' "$0"; exit 0 ;;
    *) echo "unknown option: $1" >&2; exit 2 ;;
  esac
done
[ -n "$OUT" ] || { echo "--out <dir> is required" >&2; exit 2; }
case "$TYPE" in apk|aab|both) ;; *) echo "--type must be apk, aab or both" >&2; exit 2 ;; esac
OUT="${OUT/#\~/$HOME}"; mkdir -p "$OUT" || { echo "cannot create $OUT" >&2; exit 1; }
[ -w "$OUT" ] || { echo "output folder is not writable: $OUT" >&2; exit 1; }
cd "$ROOT"

SDK="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-$(sed -n 's/^sdk.dir=//p' local.properties 2>/dev/null)}}"
SDK="${SDK:-$HOME/Library/Android/sdk}"
BT="$(ls -d "$SDK"/build-tools/* 2>/dev/null | sort -V | tail -1)"
[ -n "$BT" ] || { echo "no Android build-tools under $SDK" >&2; exit 1; }
NAME="$(sed -n 's/^rootProject.name *= *"\(.*\)"/\1/p' settings.gradle.kts)"; NAME="${NAME:-app}"
cap() { echo "$(echo "${1:0:1}" | tr '[:lower:]' '[:upper:]')${1:1}"; }
if grep -q productFlavors app/build.gradle.kts; then VARIANT="$(cap "$FLAVOR")$(cap "$BUILD_TYPE")"; VDIR="$FLAVOR/$BUILD_TYPE"
else FLAVOR=""; VARIANT="$(cap "$BUILD_TYPE")"; VDIR="$BUILD_TYPE"; fi
BUNDLE_DIR="$(echo "${VARIANT:0:1}" | tr '[:upper:]' '[:lower:]')${VARIANT:1}"
MODULES=(app); [ -f app-tv/build.gradle.kts ] && MODULES+=(app-tv)

# ---------- signing ----------
gprop() { sed -n "s/^$1=//p" "$HOME/.gradle/gradle.properties" 2>/dev/null | tail -1; }
DEBUG_SIGN=0
if [ "$BUILD_TYPE" = "release" ] && [ -z "${KEYSTORE_PATH:-}" ]; then
  if [ -n "$(gprop "$NAME.keystore.path")" ]; then
    export KEYSTORE_PATH="$(gprop "$NAME.keystore.path")" KEYSTORE_PASSWORD="$(gprop "$NAME.keystore.storePassword")"
    export KEY_ALIAS="$(gprop "$NAME.keystore.keyAlias")" KEY_PASSWORD="$(gprop "$NAME.keystore.keyPassword")"
    echo "signing: upload keystore from ~/.gradle/gradle.properties ($KEYSTORE_PATH)"
  elif [ "$KEYSTORE_MODE" = "create" ]; then
    KS="$HOME/.android-keystores/$NAME.jks"; mkdir -p "$(dirname "$KS")"; chmod 700 "$(dirname "$KS")"
    if [ -e "$KS" ]; then echo "$KS already exists but isn't in ~/.gradle/gradle.properties; add its properties by hand" >&2; exit 1; fi
    PASS="$(openssl rand -base64 24 | tr -d '/+=' | cut -c1-24)"
    # alias "<project>-upload", not a plain word: CI masks secret values (KEY_ALIAS) in every log line
    keytool -genkeypair -keystore "$KS" -storepass "$PASS" -keypass "$PASS" -alias "$NAME-upload" -keyalg RSA -keysize 4096 \
      -validity 10000 -dname "CN=$NAME upload key" >/dev/null 2>&1
    touch "$HOME/.gradle/gradle.properties"; chmod 600 "$HOME/.gradle/gradle.properties"
    printf '\n# %s upload keystore (export_build.sh) — back up the .jks and these values; losing them blocks updates\n%s.keystore.path=%s\n%s.keystore.storePassword=%s\n%s.keystore.keyAlias=%s-upload\n%s.keystore.keyPassword=%s\n' \
      "$NAME" "$NAME" "$KS" "$NAME" "$PASS" "$NAME" "$NAME" "$NAME" "$PASS" >> "$HOME/.gradle/gradle.properties"
    export KEYSTORE_PATH="$KS" KEYSTORE_PASSWORD="$PASS" KEY_ALIAS="$NAME-upload" KEY_PASSWORD="$PASS"
    echo "signing: created $KS (passwords in ~/.gradle/gradle.properties — back both up)"
  elif [ "$KEYSTORE_MODE" = "debug" ]; then
    DEBUG_SIGN=1; echo "signing: debug key (for testing only — Play rejects debug-signed uploads)"
  else
    echo "no release keystore: set KEYSTORE_* or rerun with --create-keystore (recommended) or --debug-sign" >&2; exit 2
  fi
fi

# ---------- build ----------
tasks=()
for m in "${MODULES[@]}"; do
  if [ "$TYPE" != aab ]; then tasks+=(":$m:assemble$VARIANT"); fi
  if [ "$TYPE" != apk ]; then tasks+=(":$m:bundle$VARIANT"); fi
done
./gradlew --console=plain -q "${tasks[@]}"

DEBUG_KS="$HOME/.android/debug.keystore"
fail=0; report=()
verify_apk() { # file module
  "$BT/apksigner" verify "$1" >/dev/null 2>&1 || { echo "FAIL apksigner verify: $1" >&2; return 1; }
  local badging pkg code
  badging="$("$BT/aapt2" dump badging "$1" 2>/dev/null || true)"; badging="${badging%%$'\n'*}"
  pkg="$(echo "$badging" | sed -n "s/^package: name='\([^']*\)'.*/\1/p")"
  code="$(echo "$badging" | sed -n "s/.* versionCode='\([^']*\)'.*/\1/p")"
  [ -n "$pkg" ] && [ -n "$code" ] || { echo "FAIL aapt2 badging: $1" >&2; return 1; }
  echo "$pkg $code"
}
for m in "${MODULES[@]}"; do
  meta="$m/build/outputs/apk/$VDIR/output-metadata.json"
  vcode=""; vname=""
  if [ -f "$meta" ]; then
    vcode="$(python3 -c 'import json,sys; e=json.load(open(sys.argv[1]))["elements"][0]; print(e.get("versionCode",""))' "$meta")"
    vname="$(python3 -c 'import json,sys; e=json.load(open(sys.argv[1]))["elements"][0]; print(e.get("versionName",""))' "$meta")"
  fi
  vcode="${vcode:-${VERSION_CODE:-$(sed -n 's/^app.versionCode=//p' gradle.properties)}}"
  vname="${vname:-${VERSION_NAME:-$(sed -n 's/^app.versionName=//p' gradle.properties)}}"
  suffix=""; if [ "$m" = app-tv ]; then suffix="-tv"; fi
  label="$NAME$suffix${FLAVOR:+-$FLAVOR}-$BUILD_TYPE-$vname-$vcode"
  if [ "$TYPE" != aab ]; then
    src="$(ls "$m/build/outputs/apk/$VDIR/"*.apk | head -1)"; dst="$OUT/$label.apk"
    if [ "$DEBUG_SIGN" = 1 ] || [[ "$src" == *-unsigned.apk ]]; then
      if [ "$DEBUG_SIGN" != 1 ]; then echo "FAIL $src is unsigned" >&2; fail=1; continue; fi
      "$BT/zipalign" -f -p 4 "$src" "$dst.tmp" && "$BT/apksigner" sign --ks "$DEBUG_KS" --ks-pass pass:android \
        --key-pass pass:android --out "$dst" "$dst.tmp" && rm -f "$dst.tmp" "$dst.idsig"
    else cp "$src" "$dst"; fi
    if info="$(verify_apk "$dst")"; then report+=("$dst|$info"); else fail=1; fi
    if [ "$INSTALL" = 1 ] && [ "$m" = app ]; then
      pkg="${info%% *}"
      if ! out="$(adb install -r "$dst" 2>&1)"; then
        echo "FAIL install of $pkg: $(echo "$out" | tail -1)" >&2
        if [[ "$out" == *UPDATE_INCOMPATIBLE* ]]; then
          echo "  the device has $pkg signed with another key; remove it first (its data is lost): adb uninstall $pkg" >&2
        fi
        fail=1
      else
        activity="$(adb shell cmd package resolve-activity --brief "$pkg" | tail -1)"
        launch="$(adb shell am start -W -n "$activity" 2>&1 || true)"
        running=0
        for _ in 1 2 3 4 5 6 7 8 9 10; do
          if adb shell pidof "$pkg" >/dev/null 2>&1; then running=1; break; fi
          sleep 1
        done
        if [ "$running" = 1 ]; then
          echo "installed and running on $(adb get-serialno): $activity"
        else
          echo "FAIL launch of $pkg: $(echo "$launch" | grep -iE 'error|exception' | head -1)" >&2; fail=1
        fi
      fi
    fi
  fi
  if [ "$TYPE" != apk ]; then
    src="$(ls "$m/build/outputs/bundle/$BUNDLE_DIR/"*.aab | head -1)"; dst="$OUT/$label.aab"; cp "$src" "$dst"
    if [ "$DEBUG_SIGN" = 1 ]; then
      jarsigner -keystore "$DEBUG_KS" -storepass android -keypass android "$dst" androiddebugkey >/dev/null
    fi
    if command -v bundletool >/dev/null; then
      bundletool validate --bundle="$dst" >/dev/null 2>&1 && report+=("$dst|bundletool validate ok") || { echo "FAIL bundletool validate: $dst" >&2; fail=1; }
    elif verified="$(jarsigner -verify "$dst" 2>&1)"; listing="$(unzip -l "$dst" 2>&1)"; \
         [[ "$verified" == *"jar verified"* ]] && [[ "$listing" == *"base/manifest/AndroidManifest.xml"* ]]; then
      report+=("$dst|signed, structure ok (bundletool not installed: no full validate)")
    else echo "FAIL AAB not signed or not a bundle: $dst" >&2; fail=1; fi
  fi
  mapping="$m/build/outputs/mapping/$BUNDLE_DIR/mapping.txt"
  if [ "$BUILD_TYPE" = release ] && [ -f "$mapping" ]; then cp "$mapping" "$OUT/$label-mapping.txt"; fi
done

echo "EXPORT ($( [ "$DEBUG_SIGN" = 1 ] && echo debug-signed || echo upload-key signed )):"
for r in ${report[@]+"${report[@]}"}; do
  f="${r%%|*}"; echo "  $f · $(du -h "$f" | cut -f1) · sha256 $(shasum -a 256 "$f" | cut -c1-16)… · ${r#*|}"
done
if [ "$TYPE" != apk ]; then echo "  note: an AAB can't be installed directly — bundletool build-apks --connected-device tests it on a device"; fi
if [ $fail -eq 0 ]; then echo "EXPORT: PASS"; else echo "EXPORT: FAIL"; fi
exit $fail
