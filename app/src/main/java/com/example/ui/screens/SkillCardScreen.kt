package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.MilitaryTech
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppLanguage
import com.example.model.ChhaapStrings
import com.example.model.QrPayloadBuilder
import com.example.model.SkillCardQrMode
import com.example.model.WorkMark
import com.example.model.WorkMarkStatus
import com.example.model.WorkerProfile
import com.example.ui.components.PremiumSkillCardHero
import com.example.ui.components.QrCodeMatrix
import com.example.ui.components.VoiceAssistIconButton
import com.example.ui.components.formatInr
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.ChhaapOrange
import com.example.ui.theme.ChhaapOrangeSoft
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.PendingAmber
import com.example.ui.theme.PendingAmberSoft
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SurfaceBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenBorder
import com.example.ui.theme.VerifiedGreenSoft
import com.example.ui.theme.WarmGold
import com.example.ui.theme.WhatsAppDark
import com.example.ui.theme.WhatsAppGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillCardScreen(
    worker: WorkerProfile,
    language: AppLanguage,
    isVoiceActive: Boolean,
    isContractorInspection: Boolean = false,
    onBack: (() -> Unit)? = null,
    onSpeakGuide: () -> Unit,
    onOpenTrustScore: () -> Unit,
    onOpenUstaadVouching: () -> Unit,
    onNotify: (String) -> Unit
) {
    if (onBack != null) {
        BackHandler(onBack = onBack)
    }

    val context = LocalContext.current
    var showQrDialog by remember { mutableStateOf(false) }
    var showWhatsAppDialog by remember { mutableStateOf(false) }

    val verifiedHistory = worker.workMarks.filter { it.status == WorkMarkStatus.VERIFIED }
    val shareMessage = remember(worker) {
        "Chhaap Verified Skill Card — ${worker.fullName}\n" +
            "${worker.primaryTrade.labelEn} · ${worker.city} (${worker.experienceYears} yrs exp)\n" +
            "Skill Level ${worker.skillLevel} | Trust Score: ${worker.trustScore}/100 | ${worker.totalVerifiedWorkMarks} Verified WorkMarks | ${worker.ustaadVouchCount} Ustaad Vouches\n" +
            "Verify online: https://${worker.publicProfileUrl}"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .then(if (onBack != null) Modifier.statusBarsPadding().navigationBarsPadding() else Modifier),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (onBack != null) {
                            IconButton(onClick = onBack, modifier = Modifier.testTag("skill_card_back")) {
                                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = DeepNavy)
                            }
                        }
                        Column {
                            Text(
                                text = if (isContractorInspection) {
                                    "Verified Worker Credential"
                                } else {
                                    ChhaapStrings.t(language, "Portable Skill Card", "पोर्टेबल स्किल कार्ड")
                                },
                                style = MaterialTheme.typography.headlineMedium,
                                color = DeepNavy
                            )
                            Text(
                                text = ChhaapStrings.tagline(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateMuted
                            )
                        }
                    }
                    VoiceAssistIconButton(onClick = onSpeakGuide, isActive = isVoiceActive)
                }
            }

            if (isContractorInspection) {
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = VerifiedGreenSoft,
                        border = BorderStroke(1.dp, VerifiedGreenBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Rounded.Verified, contentDescription = null, tint = VerifiedGreen)
                            Text(
                                text = "Contractor Verification View · Authentic Chhaap Credential",
                                style = MaterialTheme.typography.labelLarge,
                                color = DeepNavy
                            )
                        }
                    }
                }
            }

            // Flagship Metallic Credential Card
            item {
                PremiumSkillCardHero(
                    worker = worker,
                    language = language,
                    onShowQr = { showQrDialog = true }
                )
            }

            // Shareable Public Profile Link Bar
            item {
                Surface(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                        clipboard?.setPrimaryClip(ClipData.newPlainText("Chhaap Skill Card", "https://${worker.publicProfileUrl}"))
                        onNotify("Public Skill Card link copied: ${worker.publicProfileUrl}")
                    },
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceWhite,
                    border = BorderStroke(1.dp, BorderHairline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("copy_public_link_card")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Public Verified Link",
                                style = MaterialTheme.typography.labelSmall,
                                color = SlateMuted
                            )
                            Text(
                                text = "https://${worker.publicProfileUrl}",
                                style = MaterialTheme.typography.labelLarge.copy(fontFamily = JetBrainsMono),
                                color = DeepNavy
                            )
                        }
                        Icon(Icons.Rounded.ContentCopy, contentDescription = "Copy link", tint = ChhaapOrange)
                    }
                }
            }

            // 3 Primary Actions: Share Skill Card | Download Credential | Show QR Code
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showWhatsAppDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ChhaapOrange, contentColor = Color.White),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
                        modifier = Modifier
                            .weight(1.1f)
                            .height(50.dp)
                            .testTag("share_skill_card_button")
                    ) {
                        Icon(Icons.Rounded.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Share Card", style = MaterialTheme.typography.labelLarge)
                    }

                    OutlinedButton(
                        onClick = {
                            onNotify("Skill Card PDF & image credential saved for ${worker.shortName}")
                        },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DeepNavy),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("download_credential_button")
                    ) {
                        Icon(Icons.Rounded.Download, contentDescription = null, tint = DeepNavy, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Download", style = MaterialTheme.typography.labelLarge, color = DeepNavy)
                    }

                    OutlinedButton(
                        onClick = { showQrDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, BorderHairline),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
                        modifier = Modifier
                            .weight(0.9f)
                            .height(50.dp)
                            .testTag("show_skill_card_qr_button")
                    ) {
                        Icon(Icons.Rounded.QrCode2, contentDescription = null, tint = DeepNavy, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("QR", style = MaterialTheme.typography.labelLarge, color = DeepNavy)
                    }
                }
            }

            // Skill Badges & Repeat Customer Indicator
            item {
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = ChhaapStrings.t(language, "Verified Skill Badges", "सत्यापित कौशल बैज"),
                                style = MaterialTheme.typography.titleMedium,
                                color = DeepNavy
                            )
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = VerifiedGreenSoft,
                                border = BorderStroke(1.dp, VerifiedGreenBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Rounded.Repeat, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "${worker.repeatCustomers} Repeat Customers",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = VerifiedGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            worker.skillBadges.forEach { badge ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = SurfaceBg,
                                    border = BorderStroke(1.dp, BorderHairline)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Verified,
                                            contentDescription = null,
                                            tint = ChhaapOrange,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = badge,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = CharcoalInk
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Trust Score & Ustaad Endorsement Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        onClick = onOpenTrustScore,
                        shape = RoundedCornerShape(18.dp),
                        color = SurfaceWhite,
                        border = BorderStroke(1.dp, BorderHairline),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("skill_card_trust_link")
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(Icons.Rounded.Shield, contentDescription = null, tint = VerifiedGreen)
                                Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = SlateMuted)
                            }
                            Text(
                                text = "Trust Score ${worker.trustScore}/100",
                                style = MaterialTheme.typography.titleSmall,
                                color = DeepNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Multi-factor proof + anti-fraud checks",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateMuted
                            )
                        }
                    }

                    Surface(
                        onClick = onOpenUstaadVouching,
                        shape = RoundedCornerShape(18.dp),
                        color = SurfaceWhite,
                        border = BorderStroke(1.dp, BorderHairline),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("skill_card_ustaad_link")
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(Icons.Rounded.MilitaryTech, contentDescription = null, tint = PendingAmber)
                                Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = SlateMuted)
                            }
                            Text(
                                text = "${worker.ustaadVouchCount} Ustaad Vouches",
                                style = MaterialTheme.typography.titleSmall,
                                color = DeepNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = worker.vouches.firstOrNull()?.let { "Vouched by ${it.ustaadName}" }
                                    ?: "Senior mentor endorsements",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateMuted
                            )
                        }
                    }
                }
            }

            // Verified Job History Timeline Header
            item {
                Text(
                    text = ChhaapStrings.t(language, "Verified Job History Timeline", "सत्यापित काम का इतिहास"),
                    style = MaterialTheme.typography.titleLarge,
                    color = DeepNavy
                )
            }

            itemsIndexed(verifiedHistory, key = { _, wm -> wm.id }) { index, workMark ->
                TimelineWorkMarkItem(
                    workMark = workMark,
                    isLast = index == verifiedHistory.lastIndex
                )
            }

            item { Spacer(Modifier.height(14.dp)) }
        }
    }

    // Full QR Modal
    if (showQrDialog) {
        Dialog(
            onDismissRequest = { showQrDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = SurfaceWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Skill Card QR Code", style = MaterialTheme.typography.titleLarge, color = DeepNavy)
                        IconButton(onClick = { showQrDialog = false }) {
                            Icon(Icons.Rounded.Close, contentDescription = "Close", tint = SlateMuted)
                        }
                    }
                    var skillQrMode by remember { mutableStateOf(SkillCardQrMode.FULL_CARD) }
                    val skillQrPayload = remember(worker, skillQrMode) {
                        QrPayloadBuilder.buildSkillCardPayload(worker, skillQrMode)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SkillCardQrMode.entries.forEach { mode ->
                            val selected = skillQrMode == mode
                            Surface(
                                onClick = { skillQrMode = mode },
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
                        data = skillQrPayload,
                        modifier = Modifier.size(250.dp)
                    )
                    Text(
                        text = "${worker.fullName} · ${worker.primaryTrade.labelEn} · Level ${worker.skillLevel}",
                        style = MaterialTheme.typography.titleSmall,
                        color = DeepNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = skillQrMode.helperText,
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    // Simulated WhatsApp Share Preview Sheet
    if (showWhatsAppDialog) {
        Dialog(
            onDismissRequest = { showWhatsAppDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                shape = RoundedCornerShape(26.dp),
                color = SurfaceWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(WhatsAppGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.Send,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "Share via WhatsApp",
                                style = MaterialTheme.typography.titleLarge,
                                color = DeepNavy
                            )
                        }
                        IconButton(onClick = { showWhatsAppDialog = false }) {
                            Icon(Icons.Rounded.Close, contentDescription = "Close", tint = SlateMuted)
                        }
                    }

                    // WhatsApp chat bubble simulation
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFE7FFDB),
                        border = BorderStroke(1.dp, Color(0xFFBDE8AA)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "CHHAAP VERIFIED SKILL CARD",
                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = JetBrainsMono),
                                color = WhatsAppDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = shareMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalInk
                            )
                        }
                    }

                    Button(
                        onClick = {
                            showWhatsAppDialog = false
                            onNotify("Skill Card shared via WhatsApp preview!")
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppDark, contentColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("whatsapp_send_demo_button")
                    ) {
                        Text("Send on WhatsApp (Demo)", fontWeight = FontWeight.Bold)
                    }

                    TextButton(
                        onClick = {
                            showWhatsAppDialog = false
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareMessage)
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Chhaap Skill Card"))
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Open Android Share Sheet", color = DeepNavy)
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineWorkMarkItem(
    workMark: WorkMark,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Timeline dot + line
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (workMark.isNewlyVerified) VerifiedGreen else VerifiedGreenSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = if (workMark.isNewlyVerified) Color.White else VerifiedGreen,
                    modifier = Modifier.size(14.dp)
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(84.dp)
                        .background(BorderHairline)
                )
            }
        }

        // Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            border = BorderStroke(
                width = if (workMark.isNewlyVerified) 1.5.dp else 1.dp,
                color = if (workMark.isNewlyVerified) VerifiedGreen else BorderHairline
            ),
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = workMark.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = CharcoalInk,
                            fontWeight = FontWeight.Bold
                        )
                        if (workMark.isNewlyVerified) {
                            Surface(shape = RoundedCornerShape(50), color = VerifiedGreen) {
                                Text(
                                    text = "JUST ADDED",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = formatInr(workMark.amountInr),
                        style = MaterialTheme.typography.labelLarge,
                        color = DeepNavy,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "${workMark.location} · ${workMark.dateLabel} · ${workMark.verificationMethod}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateMuted
                )

                if (!workMark.customerFeedback.isNullOrBlank()) {
                    HorizontalDivider(color = BorderHairline)
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (workMark.rating != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Star, contentDescription = null, tint = WarmGold, modifier = Modifier.size(14.dp))
                                Text(
                                    text = "${workMark.rating}.0",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DeepNavy,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "“${workMark.customerFeedback}” — ${workMark.customerName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = CharcoalInk
                        )
                    }
                }
            }
        }
    }
}
