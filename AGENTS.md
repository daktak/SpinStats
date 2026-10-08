# AGENTS.md

## Build
- Java 21 required (uses AGP 8.3.0-beta01, Kotlin 1.9.22)
- Build: `./gradlew :app:assembleDebug`
- Clean: `./gradlew clean`

## Testing
- Unit tests: `./gradlew test`
- Android tests: `./gradlew connectedAndroidTest` (requires device/emulator)

## Linting & checks
- Lint: `./gradlew lint`
- All checks: `./gradlew check`

## Notes
- API key is stored at runtime via DataStore (Settings screen), not in BuildConfig.
- App uses Hilt, Compose, Navigation, DataStore.
