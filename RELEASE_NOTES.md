# Foqos Android v0.1.0 - Release Notes

**Release Date:** February 3, 2026  
**Status:** Pre-release / Development Preview

---

## 🎉 What's New

This is the **first public release** of Foqos for Android - a free, open-source app blocker using NFC tags and QR codes.

### Key Highlights

✅ **Complete MVP Foundation**
- Full project architecture with Kotlin + Jetpack Compose
- Room database with Profiles, Sessions, and Stats
- NFC Manager for reading and writing tags
- Accessibility Service for app blocking
- All 7 blocking strategies implemented

✅ **Build System Ready**
- GitHub Actions CI/CD for automatic APK builds
- Gradle wrapper included
- ProGuard/R8 optimization

✅ **Comprehensive Documentation**
- 5 detailed docs covering architecture, building, and usage
- Developer-friendly with clear contribution guidelines

---

## 📥 Download APK

### Method 1: From GitHub Actions (Recommended)

1. Go to [Actions tab](https://github.com/nish261/foqos-android/actions)
2. Click on the workflow run for commit `b5892c9`
3. Wait for build to complete (green checkmark)
4. Download **foqos-debug** artifact
5. Unzip and install `app-debug.apk` on Android device

### Method 2: Build from Source

```bash
git clone https://github.com/nish261/foqos-android.git
cd foqos-android
git checkout v0.1.0
./gradlew assembleDebug
# APK location: app/build/outputs/apk/debug/app-debug.apk
```

See [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) for detailed guide.

---

## 🛠️ What Works

### Architecture (100%)
- ✅ Room database fully configured
- ✅ Repository pattern with all CRUD operations
- ✅ Data models for all features
- ✅ Type converters and DAOs

### Services (80%)
- ✅ NFC Manager (read/write tags)
- ✅ Accessibility Service (detects app launches)
- ✅ Basic app blocking (returns to home screen)
- ⏳ Foreground service (structure ready, needs integration)
- ⏳ Timer service (WorkManager pending)

### UI Framework (60%)
- ✅ Navigation structure
- ✅ Material 3 theming
- ✅ Basic screen layouts
- ⏳ Full screen implementations (placeholders currently)

---

## ⏳ What's Coming in v0.2.0

**Target:** 2 weeks from now (mid-February 2026)

### High Priority
1. **Profile Management UI**
   - Create/edit profile screens
   - App picker with installed apps
   - Website list editor

2. **Session Controls**
   - Start/stop session UI
   - Timer countdown display
   - Active session notification

3. **QR Scanner Integration**
   - Camera + ML Kit barcode scanning
   - QR code generation and display

4. **Permissions Flow**
   - Onboarding wizard
   - Step-by-step permission requests
   - Clear instructions for each permission

5. **Stats Visualization**
   - Focus time charts
   - Streak display
   - Session history

---

## ⚠️ Known Limitations

### Not Yet Functional
- ❌ No profile creation UI (screens are placeholders)
- ❌ No session management UI
- ❌ QR scanner not integrated
- ❌ Timer functionality incomplete
- ❌ Stats screen shows placeholder text

### Works But Limited
- ⚠️ App blocking works but has no custom overlay
- ⚠️ Accessibility service may stop on some devices
- ⚠️ No error handling in most flows

### Not Recommended For
- ❌ Production use
- ❌ Daily driver (yet)
- ❌ Relying on for actual focus sessions

### Recommended For
- ✅ Developers wanting to contribute
- ✅ Testing the foundation
- ✅ Providing feedback on architecture

---

## 🎯 Blocking Strategies

All 7 strategies are **architecturally complete** but need UI:

1. **Manual** - Start/stop from app
2. **NFC** - Tap NFC tag to toggle
3. **QR Code** - Scan QR to toggle
4. **NFC + Manual** - Start manually, require NFC to stop
5. **QR + Manual** - Start manually, require QR to stop
6. **NFC + Timer** - Set timer, stop early with NFC
7. **QR + Timer** - Set timer, stop early with QR

Data models, database schema, and repository methods are done. Only UI implementation is pending.

---

## 📋 Requirements

### Device Requirements
- Android 8.0 (API 26) or higher
- NFC-capable device (optional, for NFC features)
- Camera (for QR scanning - coming in v0.2.0)

### Permissions Required
- **Accessibility Service** (critical) - Detects app launches
- **Usage Stats Access** (critical) - Monitors app usage
- **Camera** (optional) - For QR scanning
- **Notifications** (optional) - Session status alerts

---

## 🐛 Reporting Issues

Found a bug? Have a suggestion?

1. Check [existing issues](https://github.com/nish261/foqos-android/issues)
2. If new, [open an issue](https://github.com/nish261/foqos-android/issues/new)
3. Include:
   - Android version
   - Device model
   - Steps to reproduce
   - Screenshots if relevant

---

## 🤝 Contributing

We need help with:
- **UI Implementation** (High priority)
- **QR Scanner Integration** (Medium priority)
- **Testing on Different Devices** (Always needed)
- **Documentation Improvements**

See [TODO.md](TODO.md) for detailed task breakdown.

**Quick start for contributors:**
```bash
git clone https://github.com/nish261/foqos-android.git
cd foqos-android
# Open in Android Studio
# Pick a task from TODO.md
# Submit a PR
```

---

## 📊 Project Stats

- **Total Files:** 26
- **Lines of Code:** ~2,000
- **Kotlin Files:** 12
- **Database Tables:** 3
- **Documentation Files:** 5
- **Blocking Strategies:** 7

---

## 🙏 Credits

- **Original iOS App:** [Foqos by Ali Waseem](https://github.com/awaseem/foqos)
- **Android Port:** Built by Hermes (AI assistant)
- **Concept:** Free alternative to Brick, Bloom, Unpluq, Blok

---

## 📄 License

MIT License - See [LICENSE](LICENSE) for details

---

## 🔗 Links

- **Website:** https://www.foqos.app/ (iOS version)
- **iOS Source:** https://github.com/awaseem/foqos
- **Android Source:** https://github.com/nish261/foqos-android
- **Issues:** https://github.com/nish261/foqos-android/issues
- **Discussions:** https://github.com/nish261/foqos-android/discussions

---

## 🚀 What's Next?

**Immediate (v0.2.0 - 2 weeks):**
- Complete UI implementation
- Full feature functionality
- Beta testing phase

**Short-term (v0.3.0 - 1 month):**
- Polish and bug fixes
- Performance optimization
- Multiple device testing

**Long-term (v1.0.0 - 6 weeks):**
- Production-ready release
- Play Store submission
- Feature parity with iOS

---

**Thank you for trying Foqos Android!** 🎉

Your feedback shapes the development roadmap. Let us know what you think!
