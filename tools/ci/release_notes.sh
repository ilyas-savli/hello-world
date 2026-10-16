#!/usr/bin/env bash
# Release notes from Conventional Commit subjects (feat / fix) since the previous v* tag, cut to 500 characters
# (the Play and Firebase limit). Usage: release_notes.sh <out-file> [<out-file> …] — the same text into each file.
set -euo pipefail
[ $# -ge 1 ] || { echo "usage: release_notes.sh <out-file> [...]" >&2; exit 2; }
prev="$(git describe --tags --abbrev=0 --match 'v*' HEAD^ 2>/dev/null || true)"
range="HEAD"; [ -n "$prev" ] && range="$prev..HEAD"
notes="$(git log --no-merges --format='%s' "$range" \
  | grep -E '^(feat|fix)(\([^)]*\))?!?: ' \
  | sed -E 's/^(feat|fix)(\([^)]*\))?!?: /- /' | head -20 || true)"
[ -n "$notes" ] || notes="- Stability and maintenance improvements"
for out in "$@"; do
  mkdir -p "$(dirname "$out")"
  printf '%s\n' "$notes" | python3 -c 'import sys; sys.stdout.write(sys.stdin.read()[:500])' > "$out"
done
