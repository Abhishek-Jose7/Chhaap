package com.example.model

object ChhaapStrings {
    fun t(lang: AppLanguage, en: String, hi: String): String =
        if (lang == AppLanguage.HINDI) hi else en

    fun navHome(lang: AppLanguage) = t(lang, "Home", "होम")
    fun navMyWork(lang: AppLanguage) = t(lang, "My Work", "मेरा काम")
    fun navSkillCard(lang: AppLanguage) = t(lang, "Skill Card", "स्किल कार्ड")
    fun navProfile(lang: AppLanguage) = t(lang, "Profile", "प्रोफ़ाइल")

    fun tagline(lang: AppLanguage) = t(lang, "Every job leaves a mark.", "हर काम की पक्की पहचान।")
    fun welcomeHeadline(lang: AppLanguage) = t(
        lang,
        "Your work speaks.\nNow it has proof.",
        "आपका काम बोलता है।\nअब उसका पक्का सबूत है।"
    )
    fun welcomeSubtext(lang: AppLanguage) = t(
        lang,
        "Turn every completed job into verified experience that stays with you.",
        "हर पूरे काम को सत्यापित अनुभव में बदलें जो हमेशा आपके साथ रहे।"
    )
    fun getStarted(lang: AppLanguage) = t(lang, "Get Started", "शुरू करें")
    fun exploreDemo(lang: AppLanguage) = t(lang, "Explore Demo", "डेमो देखें")
    fun createWorkMark(lang: AppLanguage) = t(lang, "+ Create WorkMark", "+ नया वर्कमार्क बनाएं")
    fun verified(lang: AppLanguage) = t(lang, "Verified", "सत्यापित")
    fun pending(lang: AppLanguage) = t(lang, "Pending", "बाकी है")
    fun awaitingVerification(lang: AppLanguage) =
        t(lang, "Awaiting Customer Verification", "ग्राहक सत्यापन की प्रतीक्षा")
    fun showQrToCustomer(lang: AppLanguage) =
        t(lang, "Show QR to Customer", "ग्राहक को QR दिखाएं")
    fun confirmWorkCompleted(lang: AppLanguage) =
        t(lang, "Confirm Work Completed", "काम पूरा होने की पुष्टि करें")
    fun voiceGuideWelcome(lang: AppLanguage) = t(
        lang,
        "Welcome to Chhaap. Every completed job becomes your verified Skill Card. Tap Explore Demo to try Ramesh's profile right away.",
        "छाप में आपका स्वागत है। हर पूरा किया काम आपका पक्का स्किल कार्ड बनाता है। रमेश का डेमो देखने के लिए डेमो देखें दबाएं।"
    )
    fun voiceGuideOnboarding(lang: AppLanguage) = t(
        lang,
        "Enter your full name, phone number, trade, years of experience, and city. You can also skip to explore the demo profile.",
        "अपना पूरा नाम, मोबाइल नंबर, काम का प्रकार, अनुभव और शहर भरें। आप सीधे डेमो भी देख सकते हैं।"
    )
    fun voiceGuideDashboard(lang: AppLanguage) = t(
        lang,
        "Namaste! Here is your dashboard. Tap the orange Create WorkMark button to record a new completed job and get customer verification.",
        "नमस्ते! यह आपका डैशबोर्ड है। नया काम जोड़ने और ग्राहक से सत्यापन लेने के लिए नारंगी वर्कमार्क बटन दबाएं।"
    )
    fun voiceGuideCreate(lang: AppLanguage) = t(
        lang,
        "Step by step: enter job details, add before and after work photos, then generate a QR code for your customer to verify.",
        "कदम दर कदम: काम की जानकारी लिखें, पहले और बाद की फोटो जोड़ें, फिर ग्राहक को दिखाने के लिए QR कोड बनाएं।"
    )
    fun voiceGuideSkillCard(lang: AppLanguage) = t(
        lang,
        "This is your portable Chhaap Skill Card. Share it on WhatsApp or show your QR code to contractors to prove your verified work.",
        "यह आपका पोर्टेबल छाप स्किल कार्ड है। ठेकेदार को अपना सत्यापित काम दिखाने के लिए इसे व्हाट्सएप पर शेयर करें या QR दिखाएं।"
    )
}
