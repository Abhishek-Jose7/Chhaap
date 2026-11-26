package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Sms
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material.icons.rounded.StopCircle
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ChhaapStrings
import com.example.model.WorkMark
import com.example.model.WorkerProfile
import com.example.ui.components.EvidenceThumbnail
import com.example.ui.components.WorkerAvatar
import com.example.ui.components.formatInr
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.ChhaapOrange
import com.example.ui.theme.ChhaapOrangeSoft
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SurfaceBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenBorder
import com.example.ui.theme.VerifiedGreenSoft
import com.example.ui.theme.WarmGold

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomerVerificationScreen(
    worker: WorkerProfile,
    workMark: WorkMark?,
    language: AppLanguage,
    onConfirmVerification: (
        workMarkId: String,
        rating: Int,
        feedback: String,
        voiceFeedback: String?,
        customerPhone: String
    ) -> Unit,
    onViewUpdatedSkillCard: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    if (workMark == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("WorkMark not found", color = SlateMuted)
        }
        return
    }

    var isWorkConfirmed by remember { mutableStateOf(true) }
    var rating by remember { mutableIntStateOf(5) }
    var feedback by remember { mutableStateOf("") }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var recordedVoiceNote by remember { mutableStateOf<String?>(null) }
    var customerPhone by remember { mutableStateOf("98204 55189") }
    var otpSent by remember { mutableStateOf(false) }
    var otpInput by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf<String?>(null) }
    var verificationCompleted by remember { mutableStateOf(false) }
    val demoOtp = "4829"

    val previousMarks = remember(workMark.id) { worker.totalVerifiedWorkMarks }
    val previousTrust = remember(workMark.id) { worker.trustScore }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedContent(
            targetState = verificationCompleted,
            label = "customer_verification_state",
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 580.dp)
        ) { verified ->
            if (verified) {
                VerificationSuccessView(
                    worker = worker,
                    workMark = workMark,
                    previousMarks = previousMarks,
                    previousTrust = previousTrust,
                    rating = rating,
                    language = language,
                    onViewUpdatedSkillCard = onViewUpdatedSkillCard,
                    onDone = onBack
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Simulated Lightweight Customer Web-Link Bar (No App/Login Needed)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = DeepNavy,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .size(34.dp)
                                    .testTag("customer_verify_back")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Public,
                                    contentDescription = null,
                                    tint = Color(0xFF4ADE80),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "chhaap.in/verify · No Login Required",
                                    style = MaterialTheme.typography.labelMedium.copy(fontFamily = JetBrainsMono),
                                    color = Color.White
                                )
                            }
                            Spacer(Modifier.width(24.dp))
                        }
                    }

                    // Title
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = ChhaapStrings.t(language, "Verify Completed Work", "पूरे हुए काम की पुष्टि करें"),
                            style = MaterialTheme.typography.headlineLarge,
                            color = DeepNavy
                        )
                        Text(
                            text = "Confirm that ${worker.shortName} completed this job so it is added to their Skill Card.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SlateMuted
                        )
                    }

                    // Worker & Job Summary Card
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SurfaceWhite,
                        border = BorderStroke(1.dp, BorderHairline),
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                WorkerAvatar(worker = worker, size = 54.dp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = worker.shortName,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = DeepNavy
                                    )
                                    Text(
                                        text = "${workMark.trade.label(language)} · ${worker.city}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = SlateMuted
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = SurfaceBg
                                ) {
                                    Text(
                                        text = formatInr(workMark.amountInr),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = DeepNavy,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            HorizontalDivider(color = BorderHairline)

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = workMark.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = CharcoalInk,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = workMark.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SlateMuted
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = ChhaapOrange, modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "${workMark.location} · Today",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SlateMuted
                                    )
                                }
                            }

                            // Evidence Before & After Photos
                            if (workMark.beforePhoto?.hasPhoto == true || workMark.afterPhoto?.hasPhoto == true) {
                                Text(
                                    text = "Submitted Work Evidence",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = DeepNavy
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        EvidenceThumbnail(
                                            attachment = workMark.beforePhoto,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(4f / 3f)
                                        )
                                        Text("Before", style = MaterialTheme.typography.labelSmall, color = SlateMuted)
                                    }
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        EvidenceThumbnail(
                                            attachment = workMark.afterPhoto,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(4f / 3f)
                                        )
                                        Text("After", style = MaterialTheme.typography.labelSmall, color = VerifiedGreen, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // 1. Completion Confirmation Checkbox
                    Surface(
                        onClick = { isWorkConfirmed = !isWorkConfirmed },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isWorkConfirmed) VerifiedGreenSoft else SurfaceWhite,
                        border = BorderStroke(1.dp, if (isWorkConfirmed) VerifiedGreenBorder else BorderHairline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("confirm_work_checkbox_card")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isWorkConfirmed,
                                onCheckedChange = { isWorkConfirmed = it },
                                colors = CheckboxDefaults.colors(checkedColor = VerifiedGreen)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = ChhaapStrings.t(
                                    language,
                                    "I confirm this work was completed satisfactorily",
                                    "मैं पुष्टि करता/करती हूँ कि यह काम संतोषजनक ढंग से पूरा हुआ"
                                ),
                                style = MaterialTheme.typography.titleSmall,
                                color = CharcoalInk,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // 2. Star Rating & Feedback
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
                            Text(
                                text = ChhaapStrings.t(language, "Rate ${worker.shortName}", "${worker.shortName} को रेटिंग दें"),
                                style = MaterialTheme.typography.titleMedium,
                                color = DeepNavy
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                (1..5).forEach { star ->
                                    IconButton(
                                        onClick = { rating = star },
                                        modifier = Modifier
                                            .size(50.dp)
                                            .testTag("rating_star_$star")
                                    ) {
                                        Icon(
                                            imageVector = if (star <= rating) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                                            contentDescription = "$star stars",
                                            tint = if (star <= rating) WarmGold else BorderHairline,
                                            modifier = Modifier.size(38.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = when (rating) {
                                    5 -> "5/5 · Excellent craftsmanship"
                                    4 -> "4/5 · Good reliable work"
                                    3 -> "3/5 · Satisfactory"
                                    2 -> "2/5 · Needs improvement"
                                    else -> "1/5 · Poor"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = DeepNavy,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                            val quickTags = listOf("Clean work", "On time", "Safe wiring", "Fair price")
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                quickTags.forEach { tag ->
                                    val selected = feedback.contains(tag)
                                    Surface(
                                        onClick = {
                                            feedback = if (selected) {
                                                feedback.replace(tag, "").trim().trim(',')
                                            } else {
                                                if (feedback.isBlank()) tag else "$feedback, $tag"
                                            }
                                        },
                                        shape = RoundedCornerShape(50),
                                        color = if (selected) ChhaapOrangeSoft else SurfaceBg,
                                        border = BorderStroke(1.dp, if (selected) ChhaapOrange else BorderHairline)
                                    ) {
                                        Text(
                                            text = tag,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (selected) ChhaapOrange else CharcoalInk,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = feedback,
                                onValueChange = { feedback = it },
                                label = { Text("Text feedback (optional)") },
                                placeholder = { Text("Share how the work went...") },
                                minLines = 2,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ChhaapOrange,
                                    unfocusedBorderColor = BorderHairline
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("customer_feedback_input")
                            )

                            // Voice feedback recorder
                            Surface(
                                onClick = {
                                    if (isRecordingVoice) {
                                        isRecordingVoice = false
                                        recordedVoiceNote = "Voice feedback · 0:14 (“Bahut saaf aur samay par kaam kiya Ramesh ji ne.”)"
                                    } else {
                                        isRecordingVoice = true
                                    }
                                },
                                shape = RoundedCornerShape(14.dp),
                                color = if (recordedVoiceNote != null) VerifiedGreenSoft else SurfaceBg,
                                border = BorderStroke(
                                    1.dp,
                                    if (isRecordingVoice) Color(0xFFDC2626) else if (recordedVoiceNote != null) VerifiedGreenBorder else BorderHairline
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("customer_voice_feedback_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isRecordingVoice) Icons.Rounded.StopCircle else Icons.Rounded.Mic,
                                        contentDescription = "Record voice feedback",
                                        tint = if (isRecordingVoice) Color(0xFFDC2626) else ChhaapOrange
                                    )
                                    Text(
                                        text = when {
                                            isRecordingVoice -> "Recording voice feedback... Tap to save"
                                            recordedVoiceNote != null -> recordedVoiceNote!!
                                            else -> "Record a short voice feedback (optional)"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CharcoalInk,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // 3. Simulated OTP Confirmation Card
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SurfaceWhite,
                        border = BorderStroke(1.dp, BorderHairline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Rounded.Sms, contentDescription = null, tint = DeepNavy)
                                Text(
                                    text = "Confirm via OTP (Simulated Demo)",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = DeepNavy
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = customerPhone,
                                    onValueChange = { customerPhone = it },
                                    label = { Text("Customer Mobile (+91)") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1f)
                                )
                                Button(
                                    onClick = {
                                        otpSent = true
                                        otpError = null
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                                    modifier = Modifier
                                        .height(54.dp)
                                        .testTag("send_otp_button")
                                ) {
                                    Text(if (otpSent) "Resend" else "Send OTP")
                                }
                            }

                            AnimatedVisibility(visible = otpSent) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = ChhaapOrangeSoft,
                                        border = BorderStroke(1.dp, ChhaapOrange.copy(alpha = 0.4f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Demo SMS OTP: $demoOtp",
                                                style = MaterialTheme.typography.titleSmall.copy(fontFamily = JetBrainsMono),
                                                color = DeepNavy,
                                                fontWeight = FontWeight.Bold
                                            )
                                            TextButton(
                                                onClick = {
                                                    otpInput = demoOtp
                                                    otpError = null
                                                },
                                                modifier = Modifier.testTag("autofill_otp_button")
                                            ) {
                                                Text("Auto-fill", color = ChhaapOrange, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }

                                    OutlinedTextField(
                                        value = otpInput,
                                        onValueChange = {
                                            if (it.length <= 4) {
                                                otpInput = it.filter { ch -> ch.isDigit() }
                                                otpError = null
                                            }
                                        },
                                        label = { Text("Enter 4-digit OTP") },
                                        placeholder = { Text("4829") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("otp_input_field")
                                    )
                                }
                            }

                            if (otpError != null) {
                                Text(
                                    text = otpError!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFDC2626)
                                )
                            }
                        }
                    }

                    // Confirm Work Completed CTA
                    Button(
                        onClick = {
                            if (!isWorkConfirmed) {
                                otpError = "Please confirm that the work was completed first."
                                return@Button
                            }
                            if (otpSent && otpInput.isNotBlank() && otpInput != demoOtp) {
                                otpError = "Please enter Demo OTP $demoOtp (or tap Auto-fill)."
                                return@Button
                            }
                            onConfirmVerification(
                                workMark.id,
                                rating,
                                feedback,
                                recordedVoiceNote,
                                customerPhone
                            )
                            verificationCompleted = true
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerifiedGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .testTag("confirm_work_completed_button")
                    ) {
                        Icon(Icons.Rounded.Verified, contentDescription = null, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = ChhaapStrings.confirmWorkCompleted(language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}

@Composable
private fun VerificationSuccessView(
    worker: WorkerProfile,
    workMark: WorkMark,
    previousMarks: Int,
    previousTrust: Int,
    rating: Int,
    language: AppLanguage,
    onViewUpdatedSkillCard: () -> Unit,
    onDone: () -> Unit
) {
    var playPop by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { playPop = true }
    val scale by animateFloatAsState(
        targetValue = if (playPop) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "verified_seal_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(116.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(VerifiedGreenSoft),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .clip(CircleShape)
                    .background(VerifiedGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Work Verified",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        Spacer(Modifier.height(22.dp))

        Text(
            text = "Work Verified ✓",
            style = MaterialTheme.typography.displayMedium,
            color = VerifiedGreen,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "This verified job has now been added to ${worker.fullName.split(" ").first()}'s portable Skill Card.",
            style = MaterialTheme.typography.bodyLarge,
            color = SlateMuted,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(22.dp))

        // Live Updated Credential Delta Card
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = SurfaceWhite,
            border = BorderStroke(1.5.dp, VerifiedGreenBorder),
            shadowElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = workMark.id,
                        style = MaterialTheme.typography.labelMedium.copy(fontFamily = JetBrainsMono),
                        color = DeepNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(shape = RoundedCornerShape(50), color = VerifiedGreen) {
                        Text(
                            text = "OTP VERIFIED",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Text(
                    text = workMark.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = CharcoalInk,
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider(color = BorderHairline)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DeltaStat(
                        label = "Verified WorkMarks",
                        before = "$previousMarks",
                        after = "${worker.totalVerifiedWorkMarks}"
                    )
                    DeltaStat(
                        label = "Trust Score",
                        before = "$previousTrust",
                        after = "${worker.trustScore}"
                    )
                    DeltaStat(
                        label = "Rating",
                        before = "",
                        after = "$rating.0 ★"
                    )
                }
            }
        }

        Spacer(Modifier.height(26.dp))

        Button(
            onClick = onViewUpdatedSkillCard,
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ChhaapOrange, contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .testTag("view_updated_skill_card_button")
        ) {
            Icon(Icons.Rounded.Badge, contentDescription = null, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                text = ChhaapStrings.t(language, "View Updated Skill Card", "अपडेटेड स्किल कार्ड देखें"),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = onDone,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Back to Previous Screen", color = DeepNavy)
        }
    }
}

@Composable
private fun DeltaStat(
    label: String,
    before: String,
    after: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (before.isNotBlank() && before != after) "$before → $after" else after,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
            color = VerifiedGreen,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = SlateMuted
        )
    }
}
