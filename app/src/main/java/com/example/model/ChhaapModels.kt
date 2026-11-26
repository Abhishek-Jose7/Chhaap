package com.example.model

import androidx.annotation.DrawableRes

enum class AppLanguage(val code: String, val label: String) {
    ENGLISH("en", "English"),
    HINDI("hi", "हिन्दी")
}

enum class Trade(val labelEn: String, val labelHi: String) {
    ELECTRICIAN("Electrician", "इलेक्ट्रीशियन"),
    PLUMBER("Plumber", "प्लंबर"),
    CARPENTER("Carpenter", "बढ़ई"),
    PAINTER("Painter", "पेंटर"),
    MECHANIC("Mechanic", "मैकेनिक"),
    MASON("Mason", "राजमिस्त्री"),
    OTHER("Other", "अन्य");

    fun label(lang: AppLanguage): String = if (lang == AppLanguage.HINDI) labelHi else labelEn
}

enum class WorkMarkStatus {
    VERIFIED,
    PENDING
}

data class EvidenceAttachment(
    val label: String,
    @DrawableRes val sampleResId: Int? = null,
    val uriString: String? = null
) {
    val hasPhoto: Boolean get() = sampleResId != null || !uriString.isNullOrBlank()
}

data class WorkMark(
    val id: String,
    val title: String,
    val trade: Trade,
    val description: String,
    val customerName: String,
    val customerPhoneMasked: String,
    val amountInr: Int,
    val location: String,
    val dateLabel: String,
    val timestampOrder: Long,
    val status: WorkMarkStatus,
    val rating: Int? = null,
    val customerFeedback: String? = null,
    val customerVoiceNote: String? = null,
    val workerVoiceNote: String? = null,
    val beforePhoto: EvidenceAttachment? = null,
    val afterPhoto: EvidenceAttachment? = null,
    val finishedPhoto: EvidenceAttachment? = null,
    val verifiedViaOtp: Boolean = false,
    val verificationMethod: String = "QR + OTP",
    val isRepeatCustomer: Boolean = false,
    val isNewlyVerified: Boolean = false
)

data class UstaadVouch(
    val id: String,
    val ustaadName: String,
    val ustaadTitle: String,
    val yearsExperience: Int,
    val city: String,
    val relationship: String,
    val endorsedSkills: List<String>,
    val note: String,
    val dateLabel: String,
    val verifiedMaster: Boolean = true
)

data class WorkerProfile(
    val id: String,
    val fullName: String,
    val shortName: String,
    val phoneNumber: String,
    val primaryTrade: Trade,
    val experienceYears: Int,
    val city: String,
    val state: String,
    val skillLevel: Int,
    val trustScore: Int,
    val totalVerifiedWorkMarks: Int,
    val ustaadVouchCount: Int,
    val repeatCustomers: Int,
    val averageRating: Double,
    val publicProfileUrl: String,
    val skillBadges: List<String>,
    @DrawableRes val avatarResId: Int? = null,
    val avatarUri: String? = null,
    val workMarks: List<WorkMark>,
    val vouches: List<UstaadVouch>
)

data class TrustFactor(
    val titleEn: String,
    val titleHi: String,
    val weightPercent: Int,
    val achievedPercent: Int,
    val detail: String
)

data class AntiFraudSignal(
    val titleEn: String,
    val titleHi: String,
    val description: String,
    val statusLabel: String
)

data class CreateWorkMarkDraft(
    val step: Int = 1,
    val jobTitle: String = "",
    val trade: Trade = Trade.ELECTRICIAN,
    val description: String = "",
    val customerName: String = "",
    val amountInr: String = "",
    val location: String = "Thane, Maharashtra",
    val beforePhoto: EvidenceAttachment? = null,
    val afterPhoto: EvidenceAttachment? = null,
    val finishedPhoto: EvidenceAttachment? = null,
    val workerVoiceNote: String? = null,
    val isRecordingVoice: Boolean = false,
    val generatedWorkMarkId: String? = null
)

enum class BottomTab(val id: String) {
    HOME("home"),
    MY_WORK("my_work"),
    SKILL_CARD("skill_card"),
    PROFILE("profile")
}

sealed interface AppScreen {
    data object Welcome : AppScreen
    data object Onboarding : AppScreen
    data class MainTabs(val tab: BottomTab = BottomTab.HOME) : AppScreen
    data object CreateWorkMark : AppScreen
    data class CustomerVerification(val workMarkId: String) : AppScreen
    data object QrScanner : AppScreen
    data object TrustScore : AppScreen
    data object UstaadVouching : AppScreen
    data object ContractorDirectory : AppScreen
    data class InspectedSkillCard(val workerId: String, val fromContractor: Boolean = true) : AppScreen
}
