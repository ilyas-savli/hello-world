# Hello World — Specification
Package: com.ilyassavli.helloworld · App prefix: HelloWorld · minSdk 26 · compile/target SDK 37
Architecture: MVVM
Languages: en (default) · Design source: none (Material 3 defaults) · API: none
Devices: phone

## Summary
A minimal sample app that shows a "Hello World!" greeting. It exists to measure how long the android-engineer
skill takes to go from an empty folder to a pushed repo running on an emulator.

## Features
| Feature module | Purpose | Screens |
|---|---|---|
| hello | show the greeting | Hello |

## Screens
### Hello  (feature/hello · design: none)
- Content: centered "Hello World!" headline and a short subtitle.
- States: content only (no data loading, nothing can fail).
- Actions: none.
- Components: HelloWorldTopAppBar (title = app name), headline text.
- Strings: hello_greeting ("Hello World!"), hello_subtitle; app name in common_.

## Data
- Models: none · API: none · Local: none · Repositories: none

## Navigation
HelloKey (start)

## Environments
| Flavor | applicationId | API base URL | Deep-link host |
|---|---|---|---|
| stage | com.ilyassavli.helloworld.stage | https://localhost/ | — |
| prod | com.ilyassavli.helloworld | https://localhost/ | — |

## Integrations
None picked. Always on: Navigation 3, Timber, LeakCanary (debug). Chucker not added (no network module).

## Optional modules
None.

## Hardening
| # | Item | Applies | How / why N/A |
|---|---|---|---|
| 1 | Pinning | N/A | no API |
| 4 | Play Integrity | N/A | no sensitive data |
| 5 | FLAG_SECURE | N/A | no auth/payment screens |
| 6 | Biometric | N/A | no login |
Sensitive-app defaults (SEC-07): no.

## Known exceptions
- Hello has no Route/ViewModel: the screen has no state, so HelloScreen is the entry's content (TASKS hello-6 dropped).
- Scaffold helpers a one-screen app doesn't use were deleted by the dead-code gate (state components, AppResult/AppError,
  dispatcher binding, navigateTo). core/common, core/model, core/ui and core/data stay as empty modules because the
  feature convention plugin and koverVerify expect them.
- Code review (E4): skipped "Timber planted but unused" (always-on by skill decision, OBS-01) and the scaffold-template
  root cause (skill change, recorded in the skill plan).
