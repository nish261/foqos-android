# Foqos Android - Quick Start Guide

Get Foqos running on your Android device in 5 minutes.

---

## Step 1: Download the APK

### Option A: Pre-built APK (Easiest)

1. Open: https://github.com/nish261/foqos-android/actions
2. Click the latest workflow run with a green checkmark ✅
3. Scroll down to **Artifacts**
4. Click **foqos-debug** to download
5. Unzip the downloaded file
6. You'll get: `app-debug.apk`

### Option B: Build from Source

See [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md)

---

## Step 2: Install on Your Phone

### Enable Installation

**Android 8.0+:**
1. Try to open the APK
2. You'll see "Can't install from unknown sources"
3. Tap **Settings**
4. Enable **Allow from this source**
5. Go back and tap **Install**

**Android 7.1 and below:**
1. **Settings → Security**
2. Enable **Unknown Sources**
3. Tap the APK and install

### Install the APK

1. Copy `app-debug.apk` to your phone (via USB, Bluetooth, or cloud)
2. Open it from your phone's file manager
3. Tap **Install**
4. Tap **Open** when installation completes

---

## Step 3: Grant Permissions

Foqos needs special permissions to block apps.

### Accessibility Service (Required)

**Why:** Detects when blocked apps launch and blocks them

**How to enable:**
1. Open **Settings → Accessibility**
2. Find **Foqos** in the list
3. Tap it and toggle **ON**
4. Confirm the warning (this is normal for app blockers)

### Usage Stats Access (Required)

**Why:** Monitors which apps are being used

**How to enable:**
1. Open **Settings → Apps → Special Access → Usage Access**
2. Find **Foqos**
3. Toggle **ON**

### Camera Permission (For QR Codes)

Foqos will ask when you try to scan a QR code for the first time.

### Notifications Permission

Foqos will ask when you start your first session.

---

## Step 4: Create Your First Profile

1. Open **Foqos**
2. Go to **Profiles** tab
3. Tap **Create Profile** (or the + button)
4. Give it a name (e.g., "Work", "Study", "Sleep")
5. **Select apps to block:**
   - Tap **Select Apps**
   - Check the apps you want to block (Instagram, TikTok, etc.)
   - Tap **Done**
6. **Choose blocking strategy:**
   - **Manual:** Start/stop from the app
   - **NFC:** Tap NFC tag to start/stop
   - **QR Code:** Scan QR code to start/stop
   - **NFC + Timer:** Set a timer, stop early with NFC
   - (More strategies available)
7. Tap **Save**

---

## Step 5: Start a Session

### Method 1: Manual Start

1. Go to **Profiles** tab
2. Tap your profile
3. Tap **Start Session**
4. Apps in this profile are now blocked!

### Method 2: NFC Tag (If You Have One)

1. Get an NFC tag (cheap on Amazon/AliExpress - NTAG213)
2. In your profile, tap **Write NFC Tag**
3. Hold your phone to the NFC tag
4. Tag is now programmed!
5. Tap the tag anytime to start/stop sessions

### Method 3: QR Code

1. In your profile, tap **Show QR Code**
2. Print or save the QR code
3. Put it somewhere (e.g., taped to your desk)
4. Scan it to start/stop sessions

---

## Step 6: Test App Blocking

1. Start a session with a profile that blocks an app you have (e.g., Instagram)
2. Try to open that app
3. **What should happen:**
   - The app opens briefly
   - Then immediately goes back to home screen
   - You see a notification: "App blocked by Foqos"

If this works, congratulations! Foqos is working. 🎉

---

## What to Do If It's Not Working

### "Apps aren't being blocked"

**Solution:**
1. Make sure **Accessibility Service** is enabled (Settings → Accessibility → Foqos → ON)
2. Make sure a session is active (you should see it in the app)
3. Try force-stopping Foqos and reopening it
4. Restart your phone

### "Accessibility Service keeps turning off"

**Solution:**
- Some phone manufacturers (Samsung, Xiaomi, Oppo) aggressively kill background services
- Go to **Settings → Apps → Foqos → Battery**
- Set to **No restrictions** or **Allow background activity**
- Also disable battery optimization for Foqos

### "NFC tag not working"

**Solution:**
1. Make sure NFC is enabled (Settings → Connections → NFC)
2. Hold phone flat against the tag (not at an angle)
3. Try the tag with another NFC app (like NFC Tools) to verify it works
4. Rewrite the tag in Foqos

### "QR code scanner not opening"

**Solution:**
- Grant camera permission when prompted
- If denied, go to Settings → Apps → Foqos → Permissions → Camera → Allow

---

## Tips for Using Foqos

### Start Small
- Don't block everything at once
- Start with your most distracting app (probably TikTok or Instagram)
- Add more apps as you build the habit

### Use Physical Unlock
- When creating a profile, enable "Require Physical Unlock"
- Write a special NFC tag and hide it somewhere inconvenient (different room)
- Or print a QR code and put it far away
- Makes it harder to cheat during a session

### Create Multiple Profiles
- **Work:** Block social media, games
- **Study:** Block everything except educational apps
- **Sleep:** Block everything except alarm and calls
- **Focus:** Block your top 3 distractors

### Use Timers
- "NFC + Timer" strategy is powerful:
  - Start a 2-hour focus session
  - Apps are blocked for 2 hours
  - Can only stop early by tapping your NFC tag
- Forces you to honor your commitment

### Smart Breaks
- During a session, tap **Take Break** (not implemented yet in MVP)
- Temporarily pause blocking for 5-10 minutes
- Helps avoid burnout on long sessions

---

## Current Limitations (MVP)

This is the initial release. Some features are placeholder implementations:

**What works:**
- ✅ Basic app blocking via Accessibility Service
- ✅ Creating profiles
- ✅ NFC tag read/write
- ✅ Basic navigation

**What's coming:**
- ⏳ Full profile management UI
- ⏳ QR code scanner
- ⏳ Timer implementation
- ⏳ Stats visualization
- ⏳ Smart breaks
- ⏳ Website blocking

Check the [README](README.md) for full status and roadmap.

---

## Need Help?

- **Bug Reports:** [Open an issue on GitHub](https://github.com/nish261/foqos-android/issues)
- **Questions:** Check existing issues or open a new one
- **Feature Requests:** See [TODO.md](TODO.md) first

---

## What's Next?

1. **Use Foqos daily for a week**
   - See what works, what doesn't
   - Report bugs and friction points

2. **Experiment with strategies**
   - Try different blocking strategies
   - Find what keeps you accountable

3. **Track your stats**
   - (Coming soon) See your focus time and blocked launches

4. **Contribute feedback**
   - Your experience shapes development priority
   - What feature would make Foqos 10x better for you?

---

**Happy focusing! 🚀**
