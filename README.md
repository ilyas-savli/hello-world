# Hello World

Minimal Hello World sample built with the android-engineer skill.

## Requirements
- JDK 17+, Android SDK (API 37), Android Studio (latest stable)

## Run
```bash
./gradlew :app:installStageDebug   # stage environment; prod: installProdDebug
```

## Configuration
Each environment flavor (`stage`, `prod`) has its own API values. Override them in `~/.gradle/gradle.properties`
or with `-P` in CI (never commit secrets):
```properties
api.stage.baseUrl=https://stage-api.example.com/v1/
api.prod.baseUrl=https://api.example.com/v1/
api.prod.certPins=sha256/AAAA...,sha256/BBBB...
```
Debug builds include Chucker (network inspector, notification "Recording HTTP activity") and LeakCanary; release
builds contain neither.

## Quality checks
```bash
./gradlew assembleDebug detekt ktlintCheck lint testDebugUnitTest testStageDebugUnitTest koverVerify
python3 tools/check_rules.py .
```

## Release
How builds reach testers and users (local APK/AAB, Firebase App Distribution) is in
[`docs/RELEASE.md`](docs/RELEASE.md), written by the android-engineer skill's `render_ci.py` from `docs/SPEC.md` →
"Distribution & CI/CD". Signing secrets come only from the environment (`KEYSTORE_PATH`, `KEYSTORE_PASSWORD`,
`KEY_ALIAS`, `KEY_PASSWORD`).

## Modules
| Module | Purpose |
|---|---|
| `app` | Application, MainActivity, navigation host |
| `core/designsystem` | Theme, tokens, generic components (`HelloWorldTopAppBar`) |
| `core/i18n` | All user-facing strings, one file per feature |
| `core/testing` | Test helpers (`stringResource`) |
| `core/common`, `core/model`, `core/ui`, `core/data` | Empty for now; filled when a feature needs shared code, models, shared UI or a repository |
| `feature/hello` | The greeting screen (start destination) |

See `docs/SPEC.md` for features and `docs/audit/` for the latest code audit.

**What is left to do** (Firebase files, GitHub secrets, store listing, keys…) is in `docs/HANDOVER.xlsx`. Mark items
as Done in the Status column; `python3 tools/handover_check.py` re-checks what it can and keeps your statuses.
