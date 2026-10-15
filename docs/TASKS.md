# Tasks
## Stage C — scaffold
- [x] C1 Preflight
- [x] C2 Resolve versions (no optional keys)
- [x] C3 Gradle skeleton (render project template, wrapper, local.properties, tools/check_rules.py, git init)
- [x] C4 Version catalog
- [x] C5–C11 build-logic, quality tooling, core/common, designsystem, i18n, model, ui
- [x] C13 core/data (Kover module)
- [x] C14 core/testing
- [x] C15 CI workflows
- [x] C16 Scaffold gate → commit `build: scaffold project` (auto)
## Stage D — features
### D0 app shell (with first feature)
### hello
- [x] hello-6 dropped — stateless screen, no ViewModel (SPEC Known exceptions)
- [x] hello-7 UI: Route, Screen, previews, UI test  gate: feature/hello
- [x] hello-9 Navigation wiring                    gate: app
- [x] hello-10 Strings (en)                         gate: core/i18n
- [x] hello-done Full gate + dead code + commit (auto)
## Stage E — hardening & handover
- [x] E2 prodRelease build · E2b smoke test on emulator · E3 final gate · E4 code review · E5 audit · E7 handover · E8 docs · E9 report
