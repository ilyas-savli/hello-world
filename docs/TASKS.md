# Tasks
## Stage C — scaffold
- [ ] C1 Preflight
- [ ] C2 Resolve versions (no optional keys)
- [ ] C3 Gradle skeleton (render project template, wrapper, local.properties, tools/check_rules.py, git init)
- [ ] C4 Version catalog
- [ ] C5–C11 build-logic, quality tooling, core/common, designsystem, i18n, model, ui
- [ ] C13 core/data (Kover module)
- [ ] C14 core/testing
- [ ] C15 CI workflows
- [ ] C16 Scaffold gate → commit `build: scaffold project` (auto)
## Stage D — features
### D0 app shell (with first feature)
### hello
- [ ] hello-6 ViewModel + UiState + tests          gate: feature/hello
- [ ] hello-7 UI: Route, Screen, previews, UI test  gate: feature/hello
- [ ] hello-9 Navigation wiring                    gate: app
- [ ] hello-10 Strings (en)                         gate: core/i18n
- [ ] hello-done Full gate + dead code + commit (auto)
## Stage E — hardening & handover
- [ ] E2 prodRelease build · E2b smoke test on emulator · E3 final gate · E4 code review · E5 audit · E7 handover · E8 docs · E9 report
