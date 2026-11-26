package com.example.ui.screens

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.BusinessCenter
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.MilitaryTech
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.ChhaapStrings
import com.example.model.WorkMark
import com.example.model.WorkMarkStatus
import com.example.model.WorkerProfile
import com.example.ui.components.LanguageTogglePill
import com.example.ui.components.VoiceAssistIconButton
import com.example.ui.components.WorkMarkListCard
import com.example.ui.components.WorkerAvatar
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.ChhaapOrange
import com.example.ui.theme.ChhaapOrangeSoft
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.PendingAmber
import com.example.ui.theme.PendingAmberSoft
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SurfaceBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenSoft
import com.example.ui.theme.WarmGold

@Composable
fun DashboardScreen(
    worker: WorkerProfile,
    language: AppLanguage,
    isVoiceActive: Boolean,
    onToggleLanguage: () -> Unit,
    onSpeakGuide: () -> Unit,
    onCreateWorkMark: () -> Unit,
    onOpenQrScanner: () -> Unit,
    onOpenTrustScore: () -> Unit,
    onOpenUstaadVouching: () -> Unit,
    onOpenContractorView: () -> Unit,
    onOpenSkillCard: () -> Unit,
    onOpenAllWork: () -> Unit,
    onWorkMarkClick: (WorkMark) -> Unit
) {
    val firstName = worker.fullName.split(" ").firstOrNull().orEmpty().ifBlank { "Ramesh" }
    val animatedMarks by animateIntAsState(
        targetValue = worker.totalVerifiedWorkMarks,
        animationSpec = tween(500),
        label = "dash_marks"
    )
    val animatedTrust by animateIntAsState(
        targetValue = worker.trustScore,
        animationSpec = tween(500),
        label = "dash_trust"
    )
    val animatedVouches by animateIntAsState(
        targetValue = worker.ustaadVouchCount,
        animationSpec = tween(500),
        label = "dash_vouches"
    )

    val pendingJobs = worker.workMarks.filter { it.status == WorkMarkStatus.PENDING }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Greeting Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onOpenSkillCard)
                    ) {
                        WorkerAvatar(worker = worker, size = 50.dp)
                        Column {
                            Text(
                                text = ChhaapStrings.t(language, "Namaste, $firstName", "नमस्ते, $firstName"),
                                style = MaterialTheme.typography.headlineMedium,
                                color = DeepNavy,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${worker.primaryTrade.label(language)} · ${worker.city}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SlateMuted
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LanguageTogglePill(language = language, onToggle = onToggleLanguage)
                        VoiceAssistIconButton(onClick = onSpeakGuide, isActive = isVoiceActive)
                    }
                }
            }

            // Primary Hero CTA: + Create WorkMark
            item {
                Button(
                    onClick = onCreateWorkMark,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ChhaapOrange,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .testTag("dashboard_create_workmark_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddCircle,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = ChhaapStrings.createWorkMark(language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Pending Verification Banner (if any pending WorkMark exists)
            if (pendingJobs.isNotEmpty()) {
                item {
                    val latestPending = pendingJobs.first()
                    Surface(
                        onClick = { onWorkMarkClick(latestPending) },
                        shape = RoundedCornerShape(16.dp),
                        color = PendingAmberSoft,
                        border = BorderStroke(1.dp, PendingAmber.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dashboard_pending_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(PendingAmber),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.QrCodeScanner,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = ChhaapStrings.t(
                                        language,
                                        "${pendingJobs.size} WorkMark awaiting customer verification",
                                        "${pendingJobs.size} वर्कमार्क ग्राहक सत्यापन के लिए बाकी है"
                                    ),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = CharcoalInk,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${latestPending.title} · Tap to verify",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SlateMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = null,
                                tint = PendingAmber
                            )
                        }
                    }
                }
            }

            // 2x2 Metrics Bento Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DashboardMetricCard(
                            value = "$animatedMarks",
                            label = ChhaapStrings.t(language, "Verified WorkMarks", "सत्यापित वर्कमार्क"),
                            icon = Icons.Rounded.Verified,
                            iconBg = VerifiedGreenSoft,
                            iconTint = VerifiedGreen,
                            onClick = onOpenAllWork,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("metric_verified_workmarks")
                        )
                        DashboardMetricCard(
                            value = "$animatedTrust/100",
                            label = ChhaapStrings.t(language, "Trust Score", "ट्रस्ट स्कोर"),
                            icon = Icons.Rounded.Shield,
                            iconBg = Color(0xFFEAF0FA),
                            iconTint = DeepNavy,
                            onClick = onOpenTrustScore,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("metric_trust_score")
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DashboardMetricCard(
                            value = "${ChhaapStrings.t(language, "Level", "स्तर")} ${worker.skillLevel}",
                            label = ChhaapStrings.t(language, "Skill Level", "कौशल स्तर"),
                            icon = Icons.Rounded.WorkspacePremium,
                            iconBg = ChhaapOrangeSoft,
                            iconTint = ChhaapOrange,
                            onClick = onOpenSkillCard,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("metric_skill_level")
                        )
                        DashboardMetricCard(
                            value = "$animatedVouches",
                            label = ChhaapStrings.t(language, "Ustaad Vouches", "उस्ताद समर्थन"),
                            icon = Icons.Rounded.MilitaryTech,
                            iconBg = PendingAmberSoft,
                            iconTint = PendingAmber,
                            onClick = onOpenUstaadVouching,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("metric_ustaad_vouches")
                        )
                    }
                }
            }

            // Skill Progress & Repeat Customers Card
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
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
                            Column {
                                Text(
                                    text = ChhaapStrings.t(language, "Skill Level 3 → Level 4", "स्तर 3 → स्तर 4 प्रगति"),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = DeepNavy,
                                    fontWeight = FontWeight.Bold
                                )
                                val remaining = (50 - worker.totalVerifiedWorkMarks).coerceAtLeast(1)
                                Text(
                                    text = "$remaining more verified WorkMarks to Master Level 4",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SlateMuted
                                )
                            }
                            Text(
                                text = "${((worker.totalVerifiedWorkMarks / 50f) * 100).toInt().coerceAtMost(100)}%",
                                style = MaterialTheme.typography.titleMedium,
                                color = ChhaapOrange,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        LinearProgressIndicator(
                            progress = { (worker.totalVerifiedWorkMarks / 50f).coerceIn(0f, 1f) },
                            color = ChhaapOrange,
                            trackColor = ChhaapOrangeSoft,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Repeat,
                                    contentDescription = null,
                                    tint = VerifiedGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${worker.repeatCustomers} " + ChhaapStrings.t(
                                        language,
                                        "repeat customers",
                                        "नियमित ग्राहक"
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = CharcoalInk
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = null,
                                    tint = WarmGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${worker.averageRating} avg rating",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = CharcoalInk
                                )
                            }
                        }
                    }
                }
            }

            // Quick Actions Row (QR Scan, Trust, Ustaad, Contractor)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionTile(
                        icon = Icons.Rounded.QrCodeScanner,
                        label = ChhaapStrings.t(language, "Scan QR", "QR स्कैन"),
                        tint = ChhaapOrange,
                        onClick = onOpenQrScanner,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_action_qr")
                    )
                    QuickActionTile(
                        icon = Icons.Rounded.Shield,
                        label = ChhaapStrings.t(language, "Trust", "ट्रस्ट"),
                        tint = VerifiedGreen,
                        onClick = onOpenTrustScore,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_action_trust")
                    )
                    QuickActionTile(
                        icon = Icons.Rounded.Groups,
                        label = ChhaapStrings.t(language, "Ustaad", "उस्ताद"),
                        tint = PendingAmber,
                        onClick = onOpenUstaadVouching,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_action_ustaad")
                    )
                    QuickActionTile(
                        icon = Icons.Rounded.BusinessCenter,
                        label = ChhaapStrings.t(language, "Hire Mode", "ठेकेदार"),
                        tint = DeepNavy,
                        onClick = onOpenContractorView,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_action_contractor")
                    )
                }
            }

            // Recent Completed Jobs Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ChhaapStrings.t(language, "Recent Completed Jobs", "हाल के पूरे काम"),
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepNavy
                    )
                    TextButton(onClick = onOpenAllWork) {
                        Text(
                            text = ChhaapStrings.t(language, "View All", "सभी देखें"),
                            color = ChhaapOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Recent Jobs List
            items(worker.workMarks.take(5), key = { it.id }) { workMark ->
                WorkMarkListCard(
                    workMark = workMark,
                    language = language,
                    onClick = { onWorkMarkClick(workMark) }
                )
            }

            item { Spacer(Modifier.height(12.dp)) }
        }
    }
}

@Composable
private fun DashboardMetricCard(
    value: String,
    label: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderHairline),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = DeepNavy,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = SlateMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun QuickActionTile(
    icon: ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderHairline),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = CharcoalInk,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
