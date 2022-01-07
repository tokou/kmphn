An clone of [hn.premii.com](https://hn.premii.com) implemented in Kotlin Multiplatform with shared Android/Desktop Compose UI, SwiftUI on iOS and React for Web

This example supports the following targets:
- `Android` (Compose)
- `Desktop/JVM` (Compose)
- `Web/JavaScript` (React)
- `iOS` (SwiftUI)

Libraries used:
- Jetpack Compose - shared UI
- [Decompose](https://github.com/arkivanov/Decompose) - navigation and lifecycle
- [MVIKotlin](https://github.com/arkivanov/MVIKotlin) - presentation and business logic
- [SQLDelight](https://github.com/cashapp/sqldelight) - data storage

There are multiple modules:
- `:common:utils` - just some useful helpers
- `:common:database` - SQLDelight database definition
- `:common:main` - displays top news
- `:common:detail` - displat comments for a news item
- `:common:root` - navigates between `main` and `detail` screens
- `:common:compose-ui` - Shared Compose UI for Android and Desktop
- `:android` - Android application
- `:desktop` - Desktop application
- `:web` - Web browser application + React Web UI
- `ios` - iOS Xcode project

The root module is integrated into Android, Desktop and iOS apps.

### Running desktop application
```
./gradlew :desktop:run
```

### Running Android application

Open project in Intellij IDEA or Android Studio and run "android" configuration.
or
```
./gradlew :android:installRunDebug
```

### Running Web browser application
```
./gradlew :web:browserDevelopmentRun
```

### Running iOS application

Open and build the Xcode project located in `ios` folder.
or
```
./gradlew :ios:simulatorRunApp
```
