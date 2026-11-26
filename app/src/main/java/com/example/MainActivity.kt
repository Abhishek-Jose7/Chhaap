package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppLanguage
import com.example.model.AppScreen
import com.example.model.BottomTab
import com.example.model.ChhaapStrings
import com.example.model.WorkMarkStatus
import com.example.state.ChhaapViewModel
import com.example.state.VoiceAssistManager
import com.example.ui.components.VoiceAssistBanner
import com.example.ui.screens.ContractorViewScreen
import com.example.ui.screens.CreateWorkMarkScreen
import com.example.ui.screens.CustomerVerificationScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QrScannerScreen
import com.example.ui.screens.SkillCardScreen
import com.example.ui.screens.TrustScoreScreen
import com.example.ui.screens.UstaadVouchingScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.screens.WorkHistoryScreen
import com.example.ui.theme.ChhaapOrange
import com.example.ui.theme.ChhaapOrangeSoft
import com.example.ui.theme.ChhaapTheme
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SurfaceBg
import com.example.ui.theme.SurfaceWhite

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        val initialDeepLink = intent?.data?.toString()
        setContent {
            ChhaapTheme {
                ChhaapApp(initialDeepLink = initialDeepLink)
            }
        }
    }
}

private data class NavTabSpec(
    val tab: BottomTab,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val label: (AppLanguage) -> String,
    val testTag: String
)

private val mainTabSpecs = listOf(
    NavTabSpec(BottomTab.HOME, Icons.Rounded.Home, Icons.Outlined.Home, ChhaapStrings::navHome, "nav_tab_home"),
    NavTabSpec(BottomTab.MY_WORK, Icons.Rounded.Work, Icons.Outlined.WorkOutline, ChhaapStrings::navMyWork, "nav_tab_my_work"),
    NavTabSpec(BottomTab.SKILL_CARD, Icons.Rounded.Badge, Icons.Outlined.Badge, ChhaapStrings::navSkillCard, "nav_tab_skill_card"),
    NavTabSpec(BottomTab.PROFILE, Icons.Rounded.Person, Icons.Outlined.Person, ChhaapStrings::navProfile, "nav_tab_profile")
)

@Composable
fun ChhaapApp(
    initialDeepLink: String? = null,
    viewModel: ChhaapViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val voiceAssist = remember(context) { VoiceAssistManager(context) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(initialDeepLink) {
        if (!initialDeepLink.isNullOrBlank()) {
            when {
                initialDeepLink.startsWith("chhaap://verify/") -> {
                    val wmId = initialDeepLink.removePrefix("chhaap://verify/").substringBefore("/")
                    val resolvedId = viewModel.findWorkMarkById(wmId)?.id
                        ?: state.worker.workMarks.firstOrNull()?.id
                    if (resolvedId != null) {
                        viewModel.replaceRoot(AppScreen.MainTabs(BottomTab.HOME))
                        viewModel.openCustomerVerification(resolvedId)
                    }
                }
                initialDeepLink.startsWith("chhaap://skillcard/") -> {
                    val wId = initialDeepLink.removePrefix("chhaap://skillcard/").substringBefore("/")
                    viewModel.replaceRoot(AppScreen.MainTabs(BottomTab.HOME))
                    viewModel.navigateTo(AppScreen.InspectedSkillCard(wId, fromContractor = true))
                }
            }
        }
    }

    DisposableEffect(voiceAssist) {
        onDispose { voiceAssist.shutdown() }
    }

    LaunchedEffect(state.snackbarMessage) {
        val msg = state.snackbarMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    fun triggerVoiceGuide(script: String) {
        if (!state.voiceAssistEnabled) {
            viewModel.showSnackbar("Voice Assistance is turned off in Profile settings.")
            return
        }
        viewModel.showVoiceBanner(script)
        voiceAssist.speak(script, state.language)
    }

    fun stopVoiceGuide() {
        voiceAssist.stop()
        viewModel.showVoiceBanner(null)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = state.currentScreen,
            transitionSpec = {
                fadeIn(tween(220)) togetherWith fadeOut(tween(180))
            },
            label = "root_screen_transition"
        ) { screen ->
            when (screen) {
                is AppScreen.Welcome -> {
                    WelcomeScreen(
                        language = state.language,
                        isVoiceActive = state.activeVoiceMessage != null,
                        onToggleLanguage = viewModel::toggleLanguage,
                        onSpeakGuide = { triggerVoiceGuide(ChhaapStrings.voiceGuideWelcome(state.language)) },
                        onGetStarted = { viewModel.navigateTo(AppScreen.Onboarding) },
                        onExploreDemo = { viewModel.replaceRoot(AppScreen.MainTabs(BottomTab.HOME)) },
                        onOpenContractorMode = { viewModel.navigateTo(AppScreen.ContractorDirectory) },
                        onOpenQrScanner = { viewModel.navigateTo(AppScreen.QrScanner) }
                    )
                }

                is AppScreen.Onboarding -> {
                    OnboardingScreen(
                        language = state.language,
                        onToggleLanguage = viewModel::toggleLanguage,
                        onSpeakOnboardingGuide = { triggerVoiceGuide(ChhaapStrings.voiceGuideOnboarding(state.language)) },
                        onBack = { viewModel.navigateBack() },
                        onSkipUseDemo = { viewModel.replaceRoot(AppScreen.MainTabs(BottomTab.HOME)) },
                        onSubmitProfile = viewModel::completeOnboarding
                    )
                }

                is AppScreen.MainTabs -> {
                    MainTabsShell(
                        activeTab = screen.tab,
                        language = state.language,
                        snackbarHostState = snackbarHostState,
                        onSelectTab = viewModel::switchTab
                    ) {
                        when (screen.tab) {
                            BottomTab.HOME -> DashboardScreen(
                                worker = state.worker,
                                language = state.language,
                                isVoiceActive = state.activeVoiceMessage != null,
                                onToggleLanguage = viewModel::toggleLanguage,
                                onSpeakGuide = { triggerVoiceGuide(ChhaapStrings.voiceGuideDashboard(state.language)) },
                                onCreateWorkMark = viewModel::startCreateWorkMark,
                                onOpenQrScanner = { viewModel.navigateTo(AppScreen.QrScanner) },
                                onOpenTrustScore = { viewModel.navigateTo(AppScreen.TrustScore) },
                                onOpenUstaadVouching = { viewModel.navigateTo(AppScreen.UstaadVouching) },
                                onOpenContractorView = { viewModel.navigateTo(AppScreen.ContractorDirectory) },
                                onOpenSkillCard = { viewModel.switchTab(BottomTab.SKILL_CARD) },
                                onOpenAllWork = { viewModel.switchTab(BottomTab.MY_WORK) },
                                onWorkMarkClick = { wm ->
                                    if (wm.status == WorkMarkStatus.PENDING) {
                                        viewModel.openCustomerVerification(wm.id)
                                    } else {
                                        viewModel.switchTab(BottomTab.MY_WORK)
                                    }
                                }
                            )

                            BottomTab.MY_WORK -> WorkHistoryScreen(
                                worker = state.worker,
                                language = state.language,
                                onCreateWorkMark = viewModel::startCreateWorkMark,
                                onVerifyPendingWorkMark = viewModel::openCustomerVerification
                            )

                            BottomTab.SKILL_CARD -> SkillCardScreen(
                                worker = state.worker,
                                language = state.language,
                                isVoiceActive = state.activeVoiceMessage != null,
                                isContractorInspection = false,
                                onBack = null,
                                onSpeakGuide = { triggerVoiceGuide(ChhaapStrings.voiceGuideSkillCard(state.language)) },
                                onOpenTrustScore = { viewModel.navigateTo(AppScreen.TrustScore) },
                                onOpenUstaadVouching = { viewModel.navigateTo(AppScreen.UstaadVouching) },
                                onNotify = viewModel::showSnackbar
                            )

                            BottomTab.PROFILE -> ProfileScreen(
                                worker = state.worker,
                                language = state.language,
                                voiceAssistEnabled = state.voiceAssistEnabled,
                                offlineMode = state.offlineMode,
                                lastSyncedLabel = state.lastSyncedLabel,
                                onToggleLanguage = viewModel::toggleLanguage,
                                onToggleVoiceAssist = viewModel::setVoiceAssistEnabled,
                                onTestVoiceAssist = { triggerVoiceGuide(ChhaapStrings.voiceGuideDashboard(state.language)) },
                                onToggleOfflineMode = viewModel::toggleOfflineMode,
                                onSyncNow = viewModel::syncNow,
                                onUpdateProfile = viewModel::updateProfile,
                                onOpenContractorView = { viewModel.navigateTo(AppScreen.ContractorDirectory) },
                                onOpenTrustScore = { viewModel.navigateTo(AppScreen.TrustScore) },
                                onOpenUstaadVouching = { viewModel.navigateTo(AppScreen.UstaadVouching) },
                                onResetDemo = viewModel::resetDemo
                            )
                        }
                    }
                }

                is AppScreen.CreateWorkMark -> {
                    val generated = state.createDraft.generatedWorkMarkId?.let { viewModel.findWorkMarkById(it) }
                    CreateWorkMarkScreen(
                        worker = state.worker,
                        draft = state.createDraft,
                        generatedWorkMark = generated,
                        language = state.language,
                        isVoiceActive = state.activeVoiceMessage != null,
                        onSpeakGuide = { triggerVoiceGuide(ChhaapStrings.voiceGuideCreate(state.language)) },
                        onUpdateDraft = viewModel::updateCreateDraft,
                        onFillSampleJob = viewModel::fillSampleJobDetails,
                        onAttachSampleEvidence = viewModel::attachSampleEvidenceAll,
                        onGenerateWorkMark = { viewModel.generateWorkMarkFromDraft() },
                        onOpenCustomerVerification = viewModel::openCustomerVerification,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.CustomerVerification -> {
                    val wm = viewModel.findWorkMarkById(screen.workMarkId)
                    CustomerVerificationScreen(
                        worker = state.worker,
                        workMark = wm,
                        language = state.language,
                        onConfirmVerification = viewModel::verifyWorkMark,
                        onViewUpdatedSkillCard = viewModel::openUpdatedSkillCard,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.QrScanner -> {
                    QrScannerScreen(
                        primaryWorker = state.worker,
                        allWorkers = state.allWorkers,
                        language = state.language,
                        onScanWorkerSkillCard = { workerId ->
                            viewModel.navigateTo(AppScreen.InspectedSkillCard(workerId, fromContractor = true))
                        },
                        onScanPendingWorkMark = viewModel::openCustomerVerification,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.TrustScore -> {
                    TrustScoreScreen(
                        worker = state.worker,
                        language = state.language,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.UstaadVouching -> {
                    UstaadVouchingScreen(
                        worker = state.worker,
                        language = state.language,
                        onAddVouch = viewModel::addUstaadVouch,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.ContractorDirectory -> {
                    ContractorViewScreen(
                        workers = state.allWorkers,
                        language = state.language,
                        onViewWorkerSkillCard = { workerId ->
                            viewModel.navigateTo(AppScreen.InspectedSkillCard(workerId, fromContractor = true))
                        },
                        onOpenQrScanner = { viewModel.navigateTo(AppScreen.QrScanner) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.InspectedSkillCard -> {
                    val targetWorker = viewModel.findWorkerById(screen.workerId)
                    SkillCardScreen(
                        worker = targetWorker,
                        language = state.language,
                        isVoiceActive = state.activeVoiceMessage != null,
                        isContractorInspection = screen.fromContractor,
                        onBack = { viewModel.navigateBack() },
                        onSpeakGuide = { triggerVoiceGuide(ChhaapStrings.voiceGuideSkillCard(state.language)) },
                        onOpenTrustScore = { viewModel.navigateTo(AppScreen.TrustScore) },
                        onOpenUstaadVouching = { viewModel.navigateTo(AppScreen.UstaadVouching) },
                        onNotify = viewModel::showSnackbar
                    )
                }
            }
        }

        // Floating Voice Assistant Active Indicator Banner
        VoiceAssistBanner(
            message = state.activeVoiceMessage,
            onDismiss = ::stopVoiceGuide,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 6.dp)
        )
    }
}

@Composable
private fun MainTabsShell(
    activeTab: BottomTab,
    language: AppLanguage,
    snackbarHostState: SnackbarHostState,
    onSelectTab: (BottomTab) -> Unit,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 600.dp

        if (useRail) {
            Scaffold(
                containerColor = SurfaceBg,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                contentWindowInsets = WindowInsets.safeDrawing
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    NavigationRail(
                        containerColor = SurfaceWhite
                    ) {
                        mainTabSpecs.forEach { spec ->
                            val selected = activeTab == spec.tab
                            NavigationRailItem(
                                selected = selected,
                                onClick = { onSelectTab(spec.tab) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) spec.selectedIcon else spec.unselectedIcon,
                                        contentDescription = spec.label(language)
                                    )
                                },
                                label = {
                                    Text(
                                        text = spec.label(language),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = ChhaapOrange,
                                    selectedTextColor = DeepNavy,
                                    indicatorColor = ChhaapOrangeSoft,
                                    unselectedIconColor = SlateMuted,
                                    unselectedTextColor = SlateMuted
                                ),
                                modifier = Modifier.testTag(spec.testTag)
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        content()
                    }
                }
            }
        } else {
            Scaffold(
                containerColor = SurfaceBg,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                contentWindowInsets = WindowInsets.safeDrawing,
                bottomBar = {
                    NavigationBar(
                        containerColor = SurfaceWhite,
                        tonalElevation = 3.dp
                    ) {
                        mainTabSpecs.forEach { spec ->
                            val selected = activeTab == spec.tab
                            NavigationBarItem(
                                selected = selected,
                                onClick = { onSelectTab(spec.tab) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) spec.selectedIcon else spec.unselectedIcon,
                                        contentDescription = spec.label(language)
                                    )
                                },
                                label = {
                                    Text(
                                        text = spec.label(language),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = ChhaapOrange,
                                    selectedTextColor = DeepNavy,
                                    indicatorColor = ChhaapOrangeSoft,
                                    unselectedIconColor = SlateMuted,
                                    unselectedTextColor = SlateMuted
                                ),
                                modifier = Modifier.testTag(spec.testTag)
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    content()
                }
            }
        }
    }
}
