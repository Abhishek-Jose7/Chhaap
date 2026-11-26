# Chhaap (छाप)

**Every job leaves a mark.**

Chhaap is an Android mobile prototype that turns completed real-world jobs into verified, portable digital credentials for India's skilled workforce—electricians, plumbers, carpenters, painters, mechanics, and masons.

---

## The Problem

Millions of skilled workers across India build expertise through years of hands-on work, yet have no portable way to prove their track record when applying to a new contractor, moving cities, or negotiating fair rates. Existing gig platforms lock ratings inside closed silos, while paper visiting cards and verbal references cannot be verified.

## How Chhaap Works

1. **Do the Work → Create a WorkMark:** After finishing a job, the worker logs job details, before/after photos, and an optional voice note in under 30 seconds to generate a unique WorkMark certificate and scannable QR code.
2. **Customer Verifies (No App or Login Required):** The customer scans the QR code, reviews the job and photos, rates the work, adds text or voice feedback, and confirms via OTP.
3. **Build a Portable Skill Card:** Every verified WorkMark updates the worker's **Chhaap Skill Card**, **Skill Level**, and multi-factor **Trust Score**—owned by the worker and shareable across WhatsApp, housing societies, contractors, and employers.

---

## 2-Minute Demo Walkthrough

No account creation, external API keys, or SMS gateway is required to run the full end-to-end demo:

1. Launch **Chhaap** and tap **Explore Demo — Ramesh (Electrician)** (42 Verified WorkMarks · 9 Ustaad Vouches · Skill Level 3 · Trust Score 86/100).
2. On the **Worker Dashboard**, tap **`+ Create WorkMark`**.
3. **Step 1 (Job Details):** Tap **Quick Demo Fill** (or enter custom details) → **Next: Add Work Evidence**.
4. **Step 2 (Evidence):** Tap **Use Sample Before/After Evidence** (or upload photos via Android Photo Picker and record a voice description) → **Generate WorkMark**.
5. **Step 3 (Certificate & QR):** Inspect the generated WorkMark ID and live scannable QR code. Tap **Show QR to Customer** (or **Open Customer Verification Page (Demo)**).
6. **Customer Verification:** Review the before/after evidence, select a star rating, tap **Send OTP**, tap **Auto-fill** (Demo OTP `4829`), and tap **Confirm Work Completed**.
7. **Updated Skill Card:** Watch the animated **Work Verified ✓** confirmation and tap **View Updated Skill Card** to see Ramesh's live updated metrics (`42 → 43` Verified WorkMarks, `86 → 88` Trust Score) and the newly verified job highlighted at the top of his timeline.

> Tip: You can reset the demo at any time from **Profile → Reset Hackathon Demo State**.

---

## Real Scannable QR Codes

Both the **Skill Card QR** and **WorkMark QR** are generated locally with ISO/IEC 18004 compliance (via ZXing Core) and can be scanned immediately using any physical iPhone or Android phone camera:

- **Skill Card QR Modes:**
  - **Full Skill Card (Default):** Displays trade, city, Skill Level, Trust Score, Verified WorkMarks, Ustaad Vouches, full bulleted list of verified skills, and phone number immediately on scan.
  - **WhatsApp Card:** Opens WhatsApp on iPhone or Android with a pre-formatted message listing the worker's verified credentials and skills.
  - **Save Contact (vCard):** Saves the worker to phone contacts with trade, level, trust score, and skills embedded in visible contact fields.
- **WorkMark QR Modes:**
  - **WhatsApp Verify (Default):** Opens a 1-tap pre-filled job confirmation message with WorkMark ID, job title, location, and amount.
  - **SMS Verify:** Offline-capable `SMSTO:` verification confirmation.
  - **Proof Text / Deep Link:** Readable WorkMark proof summary with `chhaap://verify/<id>` Android app deep-link support.

---

## How Workers Are Discovered

Chhaap is designed as a **portable trust layer** rather than a closed gig marketplace:

- **WhatsApp & Local Referrals (Primary):** Workers share their Skill Card link (`chhaap.in/w/...`) or QR directly in apartment WhatsApp groups, local hardware shops, and referrals.
- **Built-in Contractor / Employer Directory:** Contractors, builders, and homeowners can search and filter verified workers inside Chhaap's **Contractor View** by trade, city, skill level, and verified job count—no resume required.
- **Ecosystem Portability:** Public verified Skill Card links and QRs can be attached to WhatsApp Business profiles, Google Maps/Justdial listings, RWA apps (e.g., MyGate, NoBrokerHood), and hiring portals.

---

## Key Features & Screens

| # | Screen | Highlights |
|---|--------|-----------|
| 1 | **Welcome / Landing** | Brand intro, visual 3-step guide, Hindi/English toggle, voice guide, demo & contractor entry |
| 2 | **Worker Onboarding** | Trade icon chips, experience stepper, Photo Picker avatar, voice-guided onboarding |
| 3 | **Worker Dashboard** | Live metrics grid, `+ Create WorkMark` CTA, Skill Level progress bar, repeat customers, recent jobs |
| 4 | **Create WorkMark Flow** | 3-step guided job logger, Photo Picker + sample evidence, voice note, digital certificate + live QR |
| 5 | **Customer Verification** | No-login verification view, evidence check, 1–5 star rating, voice/text feedback, simulated OTP (`4829`), animated success |
| 6 | **QR Scanner / Verify Worker** | Viewfinder UI with animated scan beam and instant demo QR scan targets |
| 7 | **Portable Skill Card** | Metallic deep-navy digital credential, live counters, skill badges, timeline, WhatsApp share preview, Download & QR modal |
| 8 | **Work History** | Filter by Verified/Pending, Trade, and Date/Amount sort + full evidence & verification record modal |
| 9 | **Trust Score** | Circular gauge, 5 weighted factors (`35/25/20/10/10`), and 5 anti-fraud protection signals (clearly labeled as demo calculations) |
| 10 | **Ustaad Vouching** | Senior mentor endorsements (e.g., Mahesh Patil · Master Electrician · 22 yrs) + interactive Vouch for Skill dialog |
| 11 | **Contractor / Employer View** | Search & multi-filter directory of verified workers with full Skill Card inspection |
| 12 | **Profile & Settings** | Edit profile, Hindi/English toggle, Android TTS voice assistance, Offline mode, Sync status, Help, Privacy, Demo reset |

### Accessibility for Low-Literacy Users
- Large touch targets (`>= 48dp`), icon-led navigation, minimal forms, and clear green/amber status indicators
- Instant **English / हिन्दी (Hindi)** language toggle across the app
- Built-in **Voice Assistance** powered by Android `TextToSpeech` (no extra permissions required)

---

## Tech Stack & Architecture

- **Language & UI:** Kotlin, Jetpack Compose, Material Design 3, Edge-to-Edge (`enableEdgeToEdge()`)
- **Responsive Layout:** Adaptive `NavigationBar` on phones and `NavigationRail` on wide screens/tablets
- **Architecture:** MVVM (`ChhaapViewModel` + `StateFlow` + `collectAsStateWithLifecycle`) with local reactive demo state
- **QR Encoding & Deep Links:** `com.google.zxing:core` (ISO/IEC 18004 QR generation) + `chhaap://` Android intent filters
- **Media & Typography:** Android Photo Picker (`PickVisualMedia`, zero storage permissions), Coil (`coil-compose`), and locally bundled Google Fonts (`Plus Jakarta Sans`, `Inter`, `JetBrains Mono`)

---

## Project Structure

```text
app/src/main/java/com/example/
├── MainActivity.kt                  # Edge-to-edge host, adaptive nav shell, deep links, TTS wiring
├── model/
│   ├── ChhaapModels.kt              # Domain models & typed AppScreen destinations
│   ├── ChhaapStrings.kt             # Bilingual English / Hindi copy & voice scripts
│   ├── DemoSeedData.kt              # Realistic Indian worker, job, Ustaad & trust data
│   └── QrPayloadBuilder.kt          # Scannable Skill Card & WorkMark QR payload builders
├── state/
│   ├── ChhaapViewModel.kt           # End-to-end reactive app state & demo interactions
│   └── VoiceAssistManager.kt        # Android TextToSpeech Hindi/English voice guide
└── ui/
    ├── components/ChhaapComponents.kt  # ZXing QrCodeMatrix, Skill Card hero, shared UI components
    ├── screens/                        # All 12 feature screens
    └── theme/                          # Color tokens, bundled typography, Material 3 theme
```

---

## Building the Project

- **Min SDK:** 24 (Android 7.0)
- **Target / Compile SDK:** 36
- **Build:**
  ```bash
  gradle :app:assembleDebug
  ```
- **Unit Tests:**
  ```bash
  gradle :app:testDebugUnitTest
  ```
