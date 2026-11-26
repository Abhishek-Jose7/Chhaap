package com.example.state

import androidx.lifecycle.ViewModel
import com.example.R
import com.example.model.AppLanguage
import com.example.model.AppScreen
import com.example.model.BottomTab
import com.example.model.CreateWorkMarkDraft
import com.example.model.DemoSeedData
import com.example.model.EvidenceAttachment
import com.example.model.Trade
import com.example.model.UstaadVouch
import com.example.model.WorkMark
import com.example.model.WorkMarkStatus
import com.example.model.WorkerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

data class ChhaapUiState(
    val backStack: List<AppScreen> = listOf(AppScreen.Welcome),
    val language: AppLanguage = AppLanguage.ENGLISH,
    val voiceAssistEnabled: Boolean = true,
    val activeVoiceMessage: String? = null,
    val offlineMode: Boolean = false,
    val lastSyncedLabel: String = "Synced just now · Local & Offline Ready",
    val worker: WorkerProfile = DemoSeedData.createRameshProfile(),
    val directoryWorkers: List<WorkerProfile> = emptyList(),
    val createDraft: CreateWorkMarkDraft = CreateWorkMarkDraft(),
    val selectedWorkMarkId: String? = null,
    val justVerifiedWorkMarkId: String? = null,
    val snackbarMessage: String? = null
) {
    val currentScreen: AppScreen
        get() = backStack.lastOrNull() ?: AppScreen.Welcome

    val allWorkers: List<WorkerProfile>
        get() = listOf(worker) + directoryWorkers.filterNot { it.id == worker.id }
}

class ChhaapViewModel : ViewModel() {

    private var sequenceCounter = 4209

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<ChhaapUiState> = _uiState.asStateFlow()

    private fun createInitialState(): ChhaapUiState {
        val ramesh = DemoSeedData.createRameshProfile()
        return ChhaapUiState(
            backStack = listOf(AppScreen.Welcome),
            worker = ramesh,
            directoryWorkers = DemoSeedData.createDirectoryWorkers(ramesh)
        )
    }

    // Navigation helpers
    fun navigateTo(screen: AppScreen) {
        _uiState.update { state ->
            state.copy(backStack = state.backStack + screen)
        }
    }

    fun replaceRoot(screen: AppScreen) {
        _uiState.update { state ->
            state.copy(backStack = listOf(screen))
        }
    }

    fun switchTab(tab: BottomTab) {
        _uiState.update { state ->
            state.copy(backStack = listOf(AppScreen.MainTabs(tab)))
        }
    }

    fun navigateBack(): Boolean {
        val current = _uiState.value.backStack
        if (current.size <= 1) return false
        _uiState.update { it.copy(backStack = current.dropLast(1)) }
        return true
    }

    fun toggleLanguage() {
        _uiState.update { state ->
            val next = if (state.language == AppLanguage.ENGLISH) AppLanguage.HINDI else AppLanguage.ENGLISH
            state.copy(language = next)
        }
    }

    fun setLanguage(language: AppLanguage) {
        _uiState.update { it.copy(language = language) }
    }

    fun setVoiceAssistEnabled(enabled: Boolean) {
        _uiState.update {
            it.copy(
                voiceAssistEnabled = enabled,
                activeVoiceMessage = if (!enabled) null else it.activeVoiceMessage
            )
        }
    }

    fun showVoiceBanner(message: String?) {
        _uiState.update { it.copy(activeVoiceMessage = message) }
    }

    fun toggleOfflineMode(enabled: Boolean) {
        _uiState.update { state ->
            val syncText = if (enabled) {
                "Offline Mode active · WorkMarks queued locally"
            } else {
                "Synced just now · All ${state.worker.workMarks.size} records up to date"
            }
            state.copy(offlineMode = enabled, lastSyncedLabel = syncText)
        }
    }

    fun syncNow() {
        _uiState.update { state ->
            state.copy(
                offlineMode = false,
                lastSyncedLabel = "Synced just now · ${state.worker.totalVerifiedWorkMarks} verified credentials backed up",
                snackbarMessage = "All WorkMarks & Skill Card synced"
            )
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun showSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }

    // Onboarding / Profile update
    fun completeOnboarding(
        fullName: String,
        phone: String,
        trade: Trade,
        experienceYears: Int,
        city: String,
        avatarUri: String?
    ) {
        val cleanName = fullName.trim().ifBlank { "Ramesh Kumar" }
        val parts = cleanName.split(" ").filter { it.isNotBlank() }
        val shortName = if (parts.size >= 2) {
            "${parts.first()} ${parts.last().first().uppercaseChar()}."
        } else {
            cleanName
        }
        val cleanCity = city.trim().ifBlank { "Thane" }
        val slug = cleanName.lowercase(Locale.US).replace(Regex("[^a-z0-9]+"), "-").trim('-')
        val cityCode = cleanCity.take(3).lowercase(Locale.US)

        _uiState.update { state ->
            val updatedWorker = state.worker.copy(
                fullName = cleanName,
                shortName = shortName,
                phoneNumber = phone.trim().ifBlank { state.worker.phoneNumber },
                primaryTrade = trade,
                experienceYears = experienceYears.coerceIn(1, 45),
                city = cleanCity,
                avatarUri = avatarUri ?: state.worker.avatarUri,
                publicProfileUrl = "chhaap.in/w/$slug-$cityCode"
            )
            state.copy(
                worker = updatedWorker,
                backStack = listOf(AppScreen.MainTabs(BottomTab.HOME)),
                snackbarMessage = "Welcome to Chhaap, ${parts.firstOrNull() ?: cleanName}!"
            )
        }
    }

    fun updateProfile(fullName: String, phone: String, city: String, trade: Trade, experienceYears: Int) {
        completeOnboarding(fullName, phone, trade, experienceYears, city, _uiState.value.worker.avatarUri)
        switchTab(BottomTab.PROFILE)
    }

    // Create WorkMark Flow
    fun startCreateWorkMark() {
        val worker = _uiState.value.worker
        _uiState.update {
            it.copy(
                createDraft = CreateWorkMarkDraft(
                    step = 1,
                    trade = worker.primaryTrade,
                    location = "${worker.city}, Maharashtra"
                ),
                backStack = it.backStack + AppScreen.CreateWorkMark
            )
        }
    }

    fun updateCreateDraft(transform: (CreateWorkMarkDraft) -> CreateWorkMarkDraft) {
        _uiState.update { state ->
            state.copy(createDraft = transform(state.createDraft))
        }
    }

    fun fillSampleJobDetails() {
        _uiState.update { state ->
            state.copy(
                createDraft = state.createDraft.copy(
                    jobTitle = "Installed electrical wiring in a 2BHK apartment",
                    trade = state.worker.primaryTrade,
                    description = "Complete concealed copper wiring for living room and bedrooms with Anchor modular switchboard and earthing check.",
                    customerName = "Amit Deshmukh",
                    amountInr = "3200",
                    location = "Hiranandani Meadows, ${state.worker.city}"
                )
            )
        }
    }

    fun attachSampleEvidenceAll() {
        _uiState.update { state ->
            state.copy(
                createDraft = state.createDraft.copy(
                    beforePhoto = EvidenceAttachment("Before Photo", sampleResId = R.drawable.img_work_before),
                    afterPhoto = EvidenceAttachment("After Photo", sampleResId = R.drawable.img_work_after),
                    finishedPhoto = EvidenceAttachment("Finished Work", sampleResId = R.drawable.img_work_after),
                    workerVoiceNote = "2BHK hall aur bedroom ki nayi wiring aur modular switchboard lagakar safety test kar diya hai."
                )
            )
        }
    }

    fun generateWorkMarkFromDraft(): WorkMark {
        val state = _uiState.value
        val draft = state.createDraft
        val cityCode = state.worker.city.take(3).uppercase(Locale.US).ifBlank { "THN" }
        val newId = "CHH-2026-$cityCode-${sequenceCounter++}"
        val parsedAmount = draft.amountInr.filter { it.isDigit() }.toIntOrNull() ?: 2500
        val title = draft.jobTitle.trim().ifBlank { "Installed electrical wiring in a 2BHK apartment" }
        val desc = draft.description.trim().ifBlank {
            "Completed professional ${draft.trade.labelEn.lowercase(Locale.US)} work with quality testing and clean site handover."
        }
        val customer = draft.customerName.trim().ifBlank { "Homeowner Customer" }
        val loc = draft.location.trim().ifBlank { state.worker.city }

        val workMark = WorkMark(
            id = newId,
            title = title,
            trade = draft.trade,
            description = desc,
            customerName = customer,
            customerPhoneMasked = "+91 98204 •••89",
            amountInr = parsedAmount,
            location = loc,
            dateLabel = "Today · Just now",
            timestampOrder = System.currentTimeMillis(),
            status = WorkMarkStatus.PENDING,
            workerVoiceNote = draft.workerVoiceNote,
            beforePhoto = draft.beforePhoto ?: EvidenceAttachment("Before Photo", sampleResId = R.drawable.img_work_before),
            afterPhoto = draft.afterPhoto ?: EvidenceAttachment("After Photo", sampleResId = R.drawable.img_work_after),
            finishedPhoto = draft.finishedPhoto,
            verifiedViaOtp = false,
            verificationMethod = "Awaiting Customer"
        )

        _uiState.update { current ->
            val updatedMarks = listOf(workMark) + current.worker.workMarks
            current.copy(
                worker = current.worker.copy(workMarks = updatedMarks),
                createDraft = current.createDraft.copy(
                    step = 3,
                    generatedWorkMarkId = newId
                )
            )
        }
        return workMark
    }

    // Customer Verification Flow
    fun openCustomerVerification(workMarkId: String) {
        navigateTo(AppScreen.CustomerVerification(workMarkId))
    }

    fun verifyWorkMark(
        workMarkId: String,
        rating: Int,
        feedback: String,
        voiceFeedback: String?,
        customerPhone: String
    ) {
        _uiState.update { state ->
            val existing = state.worker.workMarks.find { it.id == workMarkId }
            val wasAlreadyVerified = existing?.status == WorkMarkStatus.VERIFIED
            val maskedPhone = if (customerPhone.length >= 4) {
                "+91 ${customerPhone.take(5).ifBlank { "98204" }} •••${customerPhone.takeLast(2)}"
            } else {
                "+91 98204 •••89"
            }

            val updatedMarks = state.worker.workMarks.map { wm ->
                if (wm.id == workMarkId) {
                    wm.copy(
                        status = WorkMarkStatus.VERIFIED,
                        rating = rating.coerceIn(1, 5),
                        customerFeedback = feedback.trim().ifBlank {
                            "Work completed neatly and verified via Chhaap OTP."
                        },
                        customerVoiceNote = voiceFeedback,
                        customerPhoneMasked = maskedPhone,
                        verifiedViaOtp = true,
                        verificationMethod = if (voiceFeedback != null) "QR + OTP + Voice" else "QR + OTP",
                        dateLabel = "Today · Verified",
                        isNewlyVerified = true
                    )
                } else {
                    wm.copy(isNewlyVerified = false)
                }
            }

            val deltaVerified = if (wasAlreadyVerified) 0 else 1
            val newVerifiedTotal = state.worker.totalVerifiedWorkMarks + deltaVerified
            val newTrust = (state.worker.trustScore + if (wasAlreadyVerified) 0 else 2).coerceAtMost(99)
            val newRepeat = state.worker.repeatCustomers + if (!wasAlreadyVerified && rating >= 5) 1 else 0

            val updatedWorker = state.worker.copy(
                totalVerifiedWorkMarks = newVerifiedTotal,
                trustScore = newTrust,
                repeatCustomers = newRepeat,
                workMarks = updatedMarks
            )

            state.copy(
                worker = updatedWorker,
                justVerifiedWorkMarkId = workMarkId
            )
        }
    }

    fun openUpdatedSkillCard() {
        _uiState.update { state ->
            state.copy(
                backStack = listOf(AppScreen.MainTabs(BottomTab.SKILL_CARD))
            )
        }
    }

    // Ustaad Vouching
    fun addUstaadVouch(
        ustaadName: String,
        ustaadTitle: String,
        yearsExperience: Int,
        relationship: String,
        skills: List<String>,
        note: String
    ) {
        val cleanSkills = skills.ifEmpty { listOf("Residential wiring", "Electrical safety") }
        val newVouch = UstaadVouch(
            id = "vouch-${System.currentTimeMillis()}",
            ustaadName = ustaadName.trim().ifBlank { "Raghavendra Kulkarni" },
            ustaadTitle = ustaadTitle.trim().ifBlank { "Master Electrician" },
            yearsExperience = yearsExperience.coerceIn(5, 45),
            city = _uiState.value.worker.city,
            relationship = relationship.trim().ifBlank { "Senior Trade Mentor" },
            endorsedSkills = cleanSkills,
            note = note.trim().ifBlank { "Consistent workmanship and strong safety practices on residential sites." },
            dateLabel = "Verified Mentor · Today"
        )
        _uiState.update { state ->
            val updatedBadges = (state.worker.skillBadges + cleanSkills).distinct().take(6)
            val updatedWorker = state.worker.copy(
                ustaadVouchCount = state.worker.ustaadVouchCount + 1,
                trustScore = (state.worker.trustScore + 1).coerceAtMost(99),
                vouches = listOf(newVouch) + state.worker.vouches,
                skillBadges = updatedBadges
            )
            state.copy(
                worker = updatedWorker,
                snackbarMessage = "Ustaad vouch added! Credibility updated."
            )
        }
    }

    fun findWorkMarkById(workMarkId: String): WorkMark? {
        return _uiState.value.worker.workMarks.find { it.id == workMarkId }
    }

    fun findWorkerById(workerId: String): WorkerProfile {
        return _uiState.value.allWorkers.find { it.id == workerId } ?: _uiState.value.worker
    }

    fun resetDemo() {
        sequenceCounter = 4209
        val fresh = createInitialState()
        _uiState.value = fresh.copy(
            backStack = listOf(AppScreen.MainTabs(BottomTab.HOME)),
            language = _uiState.value.language,
            snackbarMessage = "Demo reset to Ramesh Kumar's initial state"
        )
    }
}
