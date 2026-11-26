package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CurrencyRupee
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.StopCircle
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.model.AppLanguage
import com.example.model.ChhaapStrings
import com.example.model.CreateWorkMarkDraft
import com.example.model.EvidenceAttachment
import com.example.model.QrPayloadBuilder
import com.example.model.Trade
import com.example.model.WorkMark
import com.example.model.WorkMarkQrMode
import com.example.model.WorkerProfile
import com.example.ui.components.EvidenceThumbnail
import com.example.ui.components.QrCodeMatrix
import com.example.ui.components.VoiceAssistIconButton
import com.example.ui.components.formatInr
import com.example.ui.components.tradeIcon
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.ChhaapOrange
import com.example.ui.theme.ChhaapOrangeSoft
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.DeepNavyMid
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.PendingAmber
import com.example.ui.theme.PendingAmberBorder
import com.example.ui.theme.PendingAmberSoft
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SurfaceBg
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenBorder
import com.example.ui.theme.VerifiedGreenSoft
import com.example.ui.theme.WarmGold

@Composable
fun CreateWorkMarkScreen(
    worker: WorkerProfile,
    draft: CreateWorkMarkDraft,
    generatedWorkMark: WorkMark?,
    language: AppLanguage,
    isVoiceActive: Boolean,
    onSpeakGuide: () -> Unit,
    onUpdateDraft: ((CreateWorkMarkDraft) -> CreateWorkMarkDraft) -> Unit,
    onFillSampleJob: () -> Unit,
    onAttachSampleEvidence: () -> Unit,
    onGenerateWorkMark: () -> Unit,
    onOpenCustomerVerification: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler {
        if (draft.step == 2) {
            onUpdateDraft { it.copy(step = 1) }
        } else {
            onBack()
        }
    }

    var showCustomerQrDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 580.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        if (draft.step == 2) onUpdateDraft { it.copy(step = 1) } else onBack()
                    },
                    modifier = Modifier.testTag("create_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepNavy
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = ChhaapStrings.t(language, "Create WorkMark", "नया वर्कमार्क बनाएं"),
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepNavy
                    )
                    Text(
                        text = "Step ${draft.step} of 3",
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateMuted
                    )
                }
                VoiceAssistIconButton(onClick = onSpeakGuide, isActive = isVoiceActive)
            }

            // 3-Step Visual Stepper
            StepProgressBar(
                currentStep = draft.step,
                language = language,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

            // Animated Step Body
            AnimatedContent(
                targetState = draft.step,
                label = "create_workmark_step",
                modifier = Modifier.weight(1f)
            ) { step ->
                when (step) {
                    1 -> Step1JobDetails(
                        draft = draft,
                        language = language,
                        onUpdateDraft = onUpdateDraft,
                        onFillSample = onFillSampleJob,
                        onNext = { onUpdateDraft { it.copy(step = 2) } }
                    )
                    2 -> Step2EvidenceUpload(
                        draft = draft,
                        language = language,
                        onUpdateDraft = onUpdateDraft,
                        onAttachSampleAll = onAttachSampleEvidence,
                        onBackStep = { onUpdateDraft { it.copy(step = 1) } },
                        onGenerate = onGenerateWorkMark
                    )
                    else -> Step3WorkMarkCertificate(
                        worker = worker,
                        workMark = generatedWorkMark,
                        language = language,
                        onShowQrModal = { showCustomerQrDialog = true },
                        onOpenCustomerVerification = { id -> onOpenCustomerVerification(id) },
                        onDoneToDashboard = onBack
                    )
                }
            }
        }
    }

    // Full-screen "Show QR to Customer" Modal
    if (showCustomerQrDialog && generatedWorkMark != null) {
        ShowQrToCustomerDialog(
            worker = worker,
            workMark = generatedWorkMark,
            language = language,
            onDismiss = { showCustomerQrDialog = false },
            onProceedCustomerVerify = {
                showCustomerQrDialog = false
                onOpenCustomerVerification(generatedWorkMark.id)
            }
        )
    }
}

@Composable
private fun StepProgressBar(
    currentStep: Int,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        1 to ChhaapStrings.t(language, "Job Details", "काम विवरण"),
        2 to ChhaapStrings.t(language, "Evidence", "फ़ोटो सबूत"),
        3 to ChhaapStrings.t(language, "WorkMark QR", "QR प्रमाण")
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        steps.forEach { (num, label) ->
            val isDone = currentStep > num
            val isActive = currentStep == num
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            when {
                                isDone -> VerifiedGreen
                                isActive -> ChhaapOrange
                                else -> BorderHairline
                            }
                        )
                )
                Text(
                    text = "$num. $label",
                    style = MaterialTheme.typography.labelSmall,
                    color = when {
                        isDone -> VerifiedGreen
                        isActive -> ChhaapOrange
                        else -> SlateMuted
                    },
                    fontWeight = if (isActive || isDone) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step1JobDetails(
    draft: CreateWorkMarkDraft,
    language: AppLanguage,
    onUpdateDraft: ((CreateWorkMarkDraft) -> CreateWorkMarkDraft) -> Unit,
    onFillSample: () -> Unit,
    onNext: () -> Unit
) {
    val quickTemplates = listOf(
        "Installed electrical wiring in a 2BHK apartment",
        "Ceiling fan installation & regulator",
        "MCB distribution box replacement",
        "False ceiling LED lighting repair"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Quick Demo Auto-fill pill
        Surface(
            onClick = onFillSample,
            shape = RoundedCornerShape(14.dp),
            color = ChhaapOrangeSoft,
            border = BorderStroke(1.dp, ChhaapOrange.copy(alpha = 0.35f)),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("fill_sample_job_button")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = ChhaapOrange)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ChhaapStrings.t(language, "Quick Demo Fill", "डेमो जानकारी भरें"),
                        style = MaterialTheme.typography.titleSmall,
                        color = DeepNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tap to pre-fill 2BHK electrical wiring job in Thane (₹3,200)",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateMuted
                    )
                }
            }
        }

        // Quick Job Title Chips
        Text(
            text = ChhaapStrings.t(language, "Quick Job Suggestions", "आम काम चुनें"),
            style = MaterialTheme.typography.labelLarge,
            color = DeepNavy
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickTemplates.forEach { template ->
                val selected = draft.jobTitle == template
                Surface(
                    onClick = { onUpdateDraft { it.copy(jobTitle = template) } },
                    shape = RoundedCornerShape(50),
                    color = if (selected) DeepNavy else SurfaceWhite,
                    border = BorderStroke(1.dp, if (selected) DeepNavy else BorderHairline)
                ) {
                    Text(
                        text = template,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selected) Color.White else CharcoalInk,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceWhite,
            border = BorderStroke(1.dp, BorderHairline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChhaapOrange,
                    unfocusedBorderColor = BorderHairline,
                    focusedLabelColor = ChhaapOrange
                )

                OutlinedTextField(
                    value = draft.jobTitle,
                    onValueChange = { v -> onUpdateDraft { it.copy(jobTitle = v) } },
                    label = { Text(ChhaapStrings.t(language, "Job Title", "काम का नाम")) },
                    placeholder = { Text("e.g., Installed electrical wiring in a 2BHK apartment") },
                    shape = RoundedCornerShape(14.dp),
                    colors = colors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("create_job_title_input")
                )

                Text(
                    text = ChhaapStrings.t(language, "Trade", "काम की श्रेणी"),
                    style = MaterialTheme.typography.labelMedium,
                    color = SlateMuted
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Trade.entries.forEach { trade ->
                        val active = draft.trade == trade
                        Surface(
                            onClick = { onUpdateDraft { it.copy(trade = trade) } },
                            shape = RoundedCornerShape(12.dp),
                            color = if (active) DeepNavy else SurfaceBg,
                            border = BorderStroke(1.dp, if (active) DeepNavy else BorderHairline)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = tradeIcon(trade),
                                    contentDescription = null,
                                    tint = if (active) ChhaapOrange else SlateMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = trade.label(language),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (active) Color.White else CharcoalInk
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = draft.description,
                    onValueChange = { v -> onUpdateDraft { it.copy(description = v) } },
                    label = { Text(ChhaapStrings.t(language, "Work Description", "काम का विवरण")) },
                    placeholder = { Text("Materials used, rooms completed, safety checks...") },
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(14.dp),
                    colors = colors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("create_job_desc_input")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = draft.amountInr,
                        onValueChange = { v ->
                            onUpdateDraft { it.copy(amountInr = v.filter { ch -> ch.isDigit() }) }
                        },
                        label = { Text(ChhaapStrings.t(language, "Amount (₹)", "राशि (₹)")) },
                        leadingIcon = { Icon(Icons.Rounded.CurrencyRupee, contentDescription = null, tint = DeepNavy) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(14.dp),
                        colors = colors,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("create_amount_input")
                    )

                    OutlinedTextField(
                        value = draft.customerName,
                        onValueChange = { v -> onUpdateDraft { it.copy(customerName = v) } },
                        label = { Text(ChhaapStrings.t(language, "Customer (Opt.)", "ग्राहक का नाम")) },
                        leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null, tint = DeepNavy) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = colors,
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("create_customer_input")
                    )
                }

                OutlinedTextField(
                    value = draft.location,
                    onValueChange = { v -> onUpdateDraft { it.copy(location = v) } },
                    label = { Text(ChhaapStrings.t(language, "Location", "स्थान")) },
                    leadingIcon = { Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = ChhaapOrange) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = colors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("create_location_input")
                )
            }
        }

        Button(
            onClick = {
                if (draft.jobTitle.isBlank()) onFillSample()
                onNext()
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ChhaapOrange, contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("step1_next_button")
        ) {
            Text(
                text = ChhaapStrings.t(language, "Next: Add Work Evidence", "आगे: फ़ोटो और सबूत जोड़ें"),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null)
        }

        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun Step2EvidenceUpload(
    draft: CreateWorkMarkDraft,
    language: AppLanguage,
    onUpdateDraft: ((CreateWorkMarkDraft) -> CreateWorkMarkDraft) -> Unit,
    onAttachSampleAll: () -> Unit,
    onBackStep: () -> Unit,
    onGenerate: () -> Unit
) {
    val beforePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onUpdateDraft { it.copy(beforePhoto = EvidenceAttachment("Before Photo", uriString = uri.toString())) }
        }
    }

    val afterPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onUpdateDraft { it.copy(afterPhoto = EvidenceAttachment("After Photo", uriString = uri.toString())) }
        }
    }

    val finishedPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onUpdateDraft { it.copy(finishedPhoto = EvidenceAttachment("Finished Work", uriString = uri.toString())) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // One-tap demo sample evidence helper
        Surface(
            onClick = onAttachSampleAll,
            shape = RoundedCornerShape(14.dp),
            color = VerifiedGreenSoft,
            border = BorderStroke(1.dp, VerifiedGreenBorder),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("use_sample_evidence_button")
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Rounded.PhotoLibrary, contentDescription = null, tint = VerifiedGreen)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ChhaapStrings.t(language, "Use Sample Before/After Evidence", "सैंपल फ़ोटो और वॉइस जोड़ें"),
                        style = MaterialTheme.typography.titleSmall,
                        color = DeepNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Attach authentic 2BHK switchboard photos & Hindi voice description",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateMuted
                    )
                }
            }
        }

        // Photo Upload Grid (Before & After)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EvidenceUploadSlot(
                title = ChhaapStrings.t(language, "Before Photo", "पहले की फ़ोटो"),
                attachment = draft.beforePhoto,
                onPickFromGallery = {
                    beforePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onUseSample = {
                    onUpdateDraft {
                        it.copy(beforePhoto = EvidenceAttachment("Before Photo", sampleResId = R.drawable.img_work_before))
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("upload_before_photo_slot")
            )

            EvidenceUploadSlot(
                title = ChhaapStrings.t(language, "After Photo", "बाद की फ़ोटो"),
                attachment = draft.afterPhoto,
                onPickFromGallery = {
                    afterPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onUseSample = {
                    onUpdateDraft {
                        it.copy(afterPhoto = EvidenceAttachment("After Photo", sampleResId = R.drawable.img_work_after))
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("upload_after_photo_slot")
            )
        }

        // Finished Work Photo Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            border = BorderStroke(1.dp, BorderHairline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (draft.finishedPhoto?.hasPhoto == true) {
                    EvidenceThumbnail(
                        attachment = draft.finishedPhoto,
                        modifier = Modifier.size(62.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceSubtle),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.CameraAlt, contentDescription = null, tint = DeepNavy)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ChhaapStrings.t(language, "Capture Finished Work Photo", "पूरे काम की फ़ोटो"),
                        style = MaterialTheme.typography.titleSmall,
                        color = CharcoalInk,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (draft.finishedPhoto?.hasPhoto == true) "Finished work photo attached ✓" else "Optional close-up of completed work",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (draft.finishedPhoto?.hasPhoto == true) VerifiedGreen else SlateMuted
                    )
                }
                OutlinedButton(
                    onClick = {
                        finishedPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (draft.finishedPhoto?.hasPhoto == true) "Change" else "Upload")
                }
            }
        }

        // Voice Description Card
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = SurfaceWhite,
            border = BorderStroke(
                width = 1.dp,
                color = if (draft.isRecordingVoice) ChhaapOrange else BorderHairline
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        onClick = {
                            if (draft.isRecordingVoice) {
                                onUpdateDraft {
                                    it.copy(
                                        isRecordingVoice = false,
                                        workerVoiceNote = "2BHK flat me nayi copper wiring aur Anchor switchboard lagakar earthing test kar diya hai."
                                    )
                                }
                            } else {
                                onUpdateDraft { it.copy(isRecordingVoice = true) }
                            }
                        },
                        shape = CircleShape,
                        color = if (draft.isRecordingVoice) Color(0xFFDC2626) else ChhaapOrange,
                        modifier = Modifier
                            .size(52.dp)
                            .testTag("record_voice_description_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (draft.isRecordingVoice) Icons.Rounded.StopCircle else Icons.Rounded.Mic,
                                contentDescription = "Record voice description",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (draft.isRecordingVoice) {
                                ChhaapStrings.t(language, "Recording voice note... Tap to stop", "आवाज़ रिकॉर्ड हो रही है... रोकने के लिए दबाएं")
                            } else {
                                ChhaapStrings.t(language, "Add Voice Description", "बोलकर काम का विवरण जोड़ें")
                            },
                            style = MaterialTheme.typography.titleSmall,
                            color = DeepNavy,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = ChhaapStrings.t(
                                language,
                                "Speak in Hindi or English — helpful when typing is hard",
                                "हिन्दी या अंग्रेज़ी में बोलें — टाइप करने की ज़रूरत नहीं"
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateMuted
                        )
                    }
                }

                if (!draft.workerVoiceNote.isNullOrBlank()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = VerifiedGreenSoft,
                        border = BorderStroke(1.dp, VerifiedGreenBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(18.dp))
                            Text(
                                text = "“${draft.workerVoiceNote}”",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalInk
                            )
                        }
                    }
                }
            }
        }

        // Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onBackStep,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(0.4f)
                    .height(56.dp)
            ) {
                Text(ChhaapStrings.t(language, "Back", "पीछे"))
            }
            Button(
                onClick = onGenerate,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ChhaapOrange, contentColor = Color.White),
                modifier = Modifier
                    .weight(0.6f)
                    .height(56.dp)
                    .testTag("generate_workmark_button")
            ) {
                Icon(Icons.Rounded.QrCode2, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = ChhaapStrings.t(language, "Generate WorkMark", "वर्कमार्क QR बनाएं"),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun EvidenceUploadSlot(
    title: String,
    attachment: EvidenceAttachment?,
    onPickFromGallery: () -> Unit,
    onUseSample: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, if (attachment?.hasPhoto == true) VerifiedGreen else BorderHairline),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (attachment?.hasPhoto == true) {
                EvidenceThumbnail(
                    attachment = attachment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceBg),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Rounded.CameraAlt, contentDescription = title, tint = SlateMuted, modifier = Modifier.size(28.dp))
                        Spacer(Modifier.height(4.dp))
                        Text(title, style = MaterialTheme.typography.labelMedium, color = SlateMuted)
                    }
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = CharcoalInk,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OutlinedButton(
                    onClick = onPickFromGallery,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Pick", style = MaterialTheme.typography.labelSmall)
                }
                TextButton(
                    onClick = onUseSample,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Sample", style = MaterialTheme.typography.labelSmall, color = ChhaapOrange)
                }
            }
        }
    }
}

@Composable
private fun Step3WorkMarkCertificate(
    worker: WorkerProfile,
    workMark: WorkMark?,
    language: AppLanguage,
    onShowQrModal: () -> Unit,
    onOpenCustomerVerification: (String) -> Unit,
    onDoneToDashboard: () -> Unit
) {
    if (workMark == null) return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Success Banner
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = VerifiedGreenSoft,
            border = BorderStroke(1.dp, VerifiedGreenBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = VerifiedGreen)
                Text(
                    text = ChhaapStrings.t(language, "WorkMark created successfully", "वर्कमार्क सफलतापूर्वक बन गया"),
                    style = MaterialTheme.typography.titleSmall,
                    color = VerifiedGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Physical Digital Certificate Card
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SurfaceWhite,
            border = BorderStroke(1.5.dp, DeepNavy.copy(alpha = 0.16f)),
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Navy Certificate Header Band
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(DeepNavy, DeepNavyMid))
                        )
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CHHAAP WORKMARK CERTIFICATE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = JetBrainsMono,
                                    letterSpacing = 1.sp
                                ),
                                color = WarmGold
                            )
                            Text(
                                text = workMark.id,
                                style = MaterialTheme.typography.titleMedium.copy(fontFamily = JetBrainsMono),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = Icons.Rounded.Verified,
                            contentDescription = null,
                            tint = ChhaapOrange,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Status pill
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = PendingAmberSoft,
                        border = BorderStroke(1.dp, PendingAmberBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Rounded.Schedule, contentDescription = null, tint = PendingAmber, modifier = Modifier.size(16.dp))
                            Text(
                                text = ChhaapStrings.awaitingVerification(language),
                                style = MaterialTheme.typography.labelMedium,
                                color = PendingAmber,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Live scannable QR mode switcher
                    var workMarkQrMode by remember { mutableStateOf(WorkMarkQrMode.WHATSAPP_VERIFY) }
                    val qrPayload = remember(workMark.id, workMarkQrMode) {
                        QrPayloadBuilder.buildWorkMarkPayload(worker, workMark, workMarkQrMode)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        WorkMarkQrMode.entries.forEach { mode ->
                            val selected = workMarkQrMode == mode
                            Surface(
                                onClick = { workMarkQrMode = mode },
                                shape = RoundedCornerShape(50),
                                color = if (selected) DeepNavy else SurfaceBg,
                                border = BorderStroke(1.dp, if (selected) DeepNavy else BorderHairline),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = mode.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (selected) Color.White else CharcoalInk,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 7.dp)
                                )
                            }
                        }
                    }

                    // Real Scannable ISO QR Code
                    QrCodeMatrix(
                        data = qrPayload,
                        modifier = Modifier
                            .size(220.dp)
                            .testTag("workmark_generated_qr")
                    )

                    Text(
                        text = workMarkQrMode.helperText,
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateMuted,
                        textAlign = TextAlign.Center
                    )

                    HorizontalDivider(color = BorderHairline)

                    // Job Certificate Metadata
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = workMark.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = DeepNavy
                        )
                        CertificateMetaRow("Worker", "${worker.fullName} · ${workMark.trade.label(language)}")
                        CertificateMetaRow("Location", workMark.location)
                        CertificateMetaRow("Date", workMark.dateLabel)
                        CertificateMetaRow("Amount", formatInr(workMark.amountInr))
                        CertificateMetaRow(
                            "Evidence",
                            buildList {
                                if (workMark.beforePhoto?.hasPhoto == true) add("Before Photo")
                                if (workMark.afterPhoto?.hasPhoto == true) add("After Photo")
                                if (!workMark.workerVoiceNote.isNullOrBlank()) add("Voice Note")
                            }.joinToString(" · ").ifBlank { "Photo Attached" }
                        )
                    }

                    // Evidence Mini Strip
                    if (workMark.beforePhoto?.hasPhoto == true || workMark.afterPhoto?.hasPhoto == true) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            workMark.beforePhoto?.let {
                                EvidenceThumbnail(
                                    attachment = it,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(74.dp)
                                )
                            }
                            workMark.afterPhoto?.let {
                                EvidenceThumbnail(
                                    attachment = it,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(74.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Primary CTA: Show QR to Customer
        Button(
            onClick = onShowQrModal,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ChhaapOrange, contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("show_qr_to_customer_button")
        ) {
            Icon(Icons.Rounded.QrCode2, contentDescription = null, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                text = ChhaapStrings.showQrToCustomer(language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Direct Demo Shortcut straight to Customer Verification
        OutlinedButton(
            onClick = { onOpenCustomerVerification(workMark.id) },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.5.dp, VerifiedGreen),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = VerifiedGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("open_customer_verification_direct_button")
        ) {
            Text(
                text = ChhaapStrings.t(
                    language,
                    "Open Customer Verification Page (Demo)",
                    "ग्राहक सत्यापन पेज खोलें (डेमो)"
                ),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }

        TextButton(
            onClick = onDoneToDashboard,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = ChhaapStrings.t(language, "Back to Dashboard", "डैशबोर्ड पर लौटें"),
                color = SlateMuted
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun CertificateMetaRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = SlateMuted
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = CharcoalInk,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun ShowQrToCustomerDialog(
    worker: WorkerProfile,
    workMark: WorkMark,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onProceedCustomerVerify: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = SurfaceWhite,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ChhaapStrings.t(language, "Customer Verification QR", "ग्राहक सत्यापन QR"),
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepNavy
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = SlateMuted)
                    }
                }

                var modalQrMode by remember { mutableStateOf(WorkMarkQrMode.WHATSAPP_VERIFY) }
                val modalPayload = remember(workMark.id, modalQrMode) {
                    QrPayloadBuilder.buildWorkMarkPayload(worker, workMark, modalQrMode)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    WorkMarkQrMode.entries.forEach { mode ->
                        val selected = modalQrMode == mode
                        Surface(
                            onClick = { modalQrMode = mode },
                            shape = RoundedCornerShape(50),
                            color = if (selected) DeepNavy else SurfaceBg,
                            border = BorderStroke(1.dp, if (selected) DeepNavy else BorderHairline),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = mode.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (selected) Color.White else CharcoalInk,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 7.dp)
                            )
                        }
                    }
                }

                QrCodeMatrix(
                    data = modalPayload,
                    modifier = Modifier.size(250.dp)
                )

                Text(
                    text = modalQrMode.helperText,
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateMuted,
                    textAlign = TextAlign.Center
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceBg
                ) {
                    Text(
                        text = "${workMark.id} · ${formatInr(workMark.amountInr)}",
                        style = MaterialTheme.typography.labelMedium.copy(fontFamily = JetBrainsMono),
                        color = DeepNavy,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Button(
                    onClick = onProceedCustomerVerify,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen, contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("modal_simulate_customer_scan_button")
                ) {
                    Text(
                        text = "Simulate Customer Scanning QR →",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
