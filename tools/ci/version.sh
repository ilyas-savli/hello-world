#!/usr/bin/env bash
# CI-07: unique, strictly increasing versionCode for every CI build.
#   versionCode = app.versionCode (gradle.properties base) + commits on HEAD × 10 + slot (0 = test build, 1 = release)
#   versionName = the v* tag without "v" (v1.2.3 → 1.2.3), else app.versionName-<short sha>
# Usage: version.sh <slot>. Appends VERSION_CODE / VERSION_NAME to $GITHUB_ENV, or prints them (GitLab: export $(…)).
# The app's build.gradle.kts reads both env vars and falls back to gradle.properties for local builds.
set -euo pipefail
slot="${1:-0}"
prop() { grep -E "^$1=" gradle.properties | head -1 | cut -d= -f2-; }
base="$(prop app.versionCode)"
count="$(git rev-list --count HEAD)"
code=$(( base + count * 10 + slot ))
ref="${CI_COMMIT_TAG:-${GITHUB_REF_NAME:-}}"
if [[ "$ref" == v[0-9]* ]]; then name="${ref#v}"; else name="$(prop app.versionName)-$(git rev-parse --short HEAD)"; fi
if [ -n "${GITHUB_ENV:-}" ]; then
  { echo "VERSION_CODE=$code"; echo "VERSION_NAME=$name"; } >> "$GITHUB_ENV"
  echo "versionCode $code · versionName $name"
else
  echo "VERSION_CODE=$code VERSION_NAME=$name"
fi
