# RingSwitcher

[![Download APK](https://img.shields.io/badge/Download-APK-green.svg)](https://github.com/re9ant/RingSwitcher/raw/main/app/build/outputs/apk/debug/app-debug.apk)

RingSwitcher is a simple, offline-only Android application that allows users to create and instantly apply custom sound-only profiles. 

## Features
- **Create Profiles**: Define custom ring, notification, and media volumes.
- **Toggle Vibrate & DND**: Easily toggle device vibration and Do Not Disturb (DND) modes per profile.
- **One-Tap Apply**: Apply your carefully crafted profiles with a single tap.
- **Offline & Private**: Works completely offline. No tracking, no login, no ads.

## Requirements
- Android SDK 26 (Android 8.0 Oreo) or higher.
- Kotlin 1.9+
- Android Studio (generates the Gradle wrapper locally).

## Build and Run Instructions
1. **Clone the Repository**
   ```bash
   git clone https://github.com/re9ant/RingSwitcher.git
   ```
2. **Open in Android Studio**
   - Launch Android Studio and select **Open**.
   - Navigate to the cloned `RingSwitcher` directory and select it.
   - Wait for Gradle to synchronize the project. Android Studio will automatically set up the Gradle environment and build dependencies.
3. **Run on a Device or Emulator**
   - Connect a physical Android device or start an Android Emulator.
   - Click the green **Run** button (`Shift + F10`) in Android Studio.
4. **Permissions**
   - On the first run, the app will prompt you to grant **Do Not Disturb Access**. This must be enabled in your system's settings for the app to function correctly when modifying DND profiles.

## Technologies Used
- MVVM Architecture with `ViewModel` and `LiveData`
- `AudioManager` and `NotificationManager` SDKs
- `SharedPreferences` via `Gson` for lightweight local persistence
- Native Android UI Views and Material Components
