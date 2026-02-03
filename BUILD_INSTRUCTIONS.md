# Foqos Android - Complete Build Instructions

This guide will help you build a working APK from this project.

---

## Method 1: Download Pre-built APK (Easiest)

1. Go to: https://github.com/nish261/foqos-android/actions
2. Click on the latest successful workflow run
3. Download the **foqos-debug** artifact
4. Unzip it to get `app-debug.apk`
5. Install on your Android device

**Note:** GitHub Actions will automatically build APKs when code is pushed. Check the Actions tab for downloadable builds.

---

## Method 2: Build in Android Studio (Recommended)

### Prerequisites
- **Android Studio:** Hedgehog (2023.1.1) or later
- **JDK:** 17 (bundled with Android Studio)
- **Internet connection:** To download dependencies

### Steps

1. **Install Android Studio**
   - Download from: https://developer.android.com/studio
   - Install and launch
   - Go through initial setup wizard

2. **Clone the Repository**
   ```bash
   git clone https://github.com/nish261/foqos-android.git
   cd foqos-android
   ```

3. **Open Project in Android Studio**
   - Launch Android Studio
   - Click "Open"
   - Select the `foqos-android` folder
   - Click "OK"

4. **Wait for Gradle Sync**
   - Android Studio will automatically download dependencies
   - This may take 5-10 minutes on first build
   - Watch the progress bar at the bottom
   - If errors appear, click "Try Again"

5. **Build APK**
   - **Menu:** Build → Build Bundle(s) / APK(s) → Build APK(s)
   - **Or Shortcut:** Click the hammer icon in the toolbar
   - Wait for build to complete (you'll see a notification)

6. **Find the APK**
   - After build completes, click "locate" in the notification
   - Or navigate to: `app/build/outputs/apk/debug/`
   - File name: `app-debug.apk`

7. **Install on Device**
   - **Via USB:**
     - Connect your Android device
     - Enable USB debugging in Developer Options
     - Click the "Run" button (green triangle) in Android Studio
     - Select your device
   
   - **Via File Transfer:**
     - Copy `app-debug.apk` to your phone
     - Open it on your phone
     - Allow "Install from Unknown Sources" if prompted
     - Tap "Install"

---

## Method 3: Build from Command Line (Advanced)

### Prerequisites
- **JDK 17** installed and in PATH
- **Git** installed
- **Internet connection**

### On Linux/macOS

```bash
# Clone repository
git clone https://github.com/nish261/foqos-android.git
cd foqos-android

# Make gradlew executable
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# APK location
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

### On Windows

```cmd
# Clone repository
git clone https://github.com/nish261/foqos-android.git
cd foqos-android

# Build debug APK
gradlew.bat assembleDebug

# APK location
dir app\build\outputs\apk\debug\app-debug.apk
```

### Build Release APK (Unsigned)

```bash
./gradlew assembleRelease

# APK location
app/build/outputs/apk/release/app-release-unsigned.apk
```

**Note:** Release APKs need to be signed before installation. Use debug APKs for testing.

---

## Troubleshooting

### "SDK location not found"

**Solution:**
1. Open Android Studio
2. Go to: File → Project Structure → SDK Location
3. Set Android SDK location (usually `~/Library/Android/sdk` on Mac, `C:\Users\<you>\AppData\Local\Android\Sdk` on Windows)
4. Or create `local.properties` file in project root:
   ```
   sdk.dir=/path/to/your/android/sdk
   ```

### "Failed to download dependencies"

**Solution:**
- Check internet connection
- Try again: `./gradlew --refresh-dependencies assembleDebug`
- Or in Android Studio: File → Invalidate Caches → Invalidate and Restart

### "Gradle sync failed"

**Solution:**
1. Check that JDK 17 is installed
2. In Android Studio: File → Settings → Build, Execution, Deployment → Build Tools → Gradle
3. Set Gradle JDK to "Embedded JDK 17"
4. Click "Apply"
5. Click "Sync Now"

### "Build failed: Execution failed for task ':app:compileDebugKotlin'"

**Solution:**
- This means there's a code error
- Check the error message in the Build tab
- Open an issue on GitHub with the full error

### "Installation failed: INSTALL_PARSE_FAILED_NO_CERTIFICATES"

**Solution:**
- The APK is corrupted or unsigned improperly
- Rebuild: `./gradlew clean assembleDebug`
- If using release APK, switch to debug APK

### "App crashes on launch"

**Solution:**
1. Connect device via USB
2. Open Android Studio → Logcat tab
3. Run the app
4. Copy the crash log
5. Open an issue on GitHub with the log

---

## Installing on Your Phone

### Enable Developer Options

1. Go to **Settings → About Phone**
2. Tap **Build Number** 7 times
3. Go back to **Settings → Developer Options**
4. Enable **USB Debugging**

### Allow Installation from Unknown Sources

#### Android 8.0+
1. When you try to install the APK, you'll see a prompt
2. Tap **Settings**
3. Enable **Allow from this source**
4. Go back and install

#### Android 7.1 and below
1. Go to **Settings → Security**
2. Enable **Unknown Sources**
3. Install the APK

### Install via ADB (Alternative)

```bash
# Connect device via USB
# Enable USB debugging

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# If app is already installed
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## Signing APKs for Release

### Generate Keystore

```bash
keytool -genkey -v -keystore foqos-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias foqos-key
```

### Sign APK

```bash
# Build unsigned release APK
./gradlew assembleRelease

# Sign it
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore foqos-release-key.jks \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  foqos-key

# Verify signature
jarsigner -verify -verbose -certs \
  app/build/outputs/apk/release/app-release-unsigned.apk

# Zipalign (optimize)
zipalign -v 4 \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  app/build/outputs/apk/release/foqos-release.apk
```

---

## Building Different Variants

### Debug Build (Default)
- Fast build
- Includes debug symbols
- Not optimized
- Larger APK size

```bash
./gradlew assembleDebug
```

### Release Build
- Optimized
- ProGuard/R8 applied
- Smaller APK size
- Must be signed

```bash
./gradlew assembleRelease
```

---

## Build Output Locations

```
foqos-android/
├── app/
│   └── build/
│       └── outputs/
│           └── apk/
│               ├── debug/
│               │   └── app-debug.apk          ← Debug APK
│               └── release/
│                   └── app-release-unsigned.apk  ← Release APK (unsigned)
```

---

## Next Steps After Installing

1. **Grant Permissions**
   - Accessibility Service (required for app blocking)
   - Usage Stats Access (required to monitor apps)
   - Camera (for QR scanning)
   - Notifications (for session status)

2. **Create Your First Profile**
   - Open Foqos
   - Tap "Create Profile"
   - Name it (e.g., "Work")
   - Select apps to block
   - Choose blocking strategy

3. **Test NFC (if available)**
   - Get a cheap NFC tag (NTAG213 on Amazon)
   - Write the tag from within Foqos
   - Tap to start/stop sessions

---

## Current Build Status

**Current State:** MVP foundation complete

The app will compile and run, but some features need UI implementation:
- ✅ Compiles successfully
- ✅ Launches without crashing
- ✅ Basic navigation works
- ⏳ Profile management UI (placeholder)
- ⏳ QR scanner (not yet integrated)
- ⏳ Full session controls (basic implementation)

See `README.md` for full list of what's implemented vs. what's pending.

---

## Automated Builds via GitHub Actions

Every push to the `main` branch triggers an automated build:

1. Go to: https://github.com/nish261/foqos-android/actions
2. Click on the latest workflow run
3. Wait for it to complete (green checkmark)
4. Download the artifact: `foqos-debug`
5. Unzip to get the APK

This is the easiest way to get a working APK without setting up Android Studio locally.

---

## Need Help?

- **Code Issues:** Open an issue on GitHub
- **Build Errors:** Include full error log in the issue
- **Feature Requests:** Check TODO.md first, then open an issue

---

**Happy Building! 🚀**
