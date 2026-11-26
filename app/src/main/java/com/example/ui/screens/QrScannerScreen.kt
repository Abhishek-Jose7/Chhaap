package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.WorkMark
import com.example.model.WorkMarkStatus
import com.example.model.WorkerProfile
import com.example.ui.theme.ChhaapOrange
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.DeepNavyMid
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.PendingAmber
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.WarmGold

@Composable
fun QrScannerScreen(
    primaryWorker: WorkerProfile,
    allWorkers: List<WorkerProfile>,
    language: AppLanguage,
    onScanWorkerSkillCard: (String) -> Unit,
    onScanPendingWorkMark: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    val pendingWorkMark: WorkMark? = primaryWorker.workMarks.firstOrNull { it.status == WorkMarkStatus.PENDING }
    val secondWorker = allWorkers.firstOrNull { it.id != primaryWorker.id }

    val infiniteTransition = rememberInfiniteTransition(label = "scanner_beam")
    val beamProgress by infiniteTransition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "beam_y"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1426))
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 580.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("qr_scanner_back")) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "Verify Worker / Scan QR",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(Modifier.size(40.dp))
            }

            // Realistic Viewfinder Box
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(DeepNavyMid),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = 5.dp.toPx()
                    val corner = 34.dp.toPx()
                    val pad = 18.dp.toPx()
                    val w = size.width
                    val h = size.height

                    // Top-left bracket
                    drawLine(ChhaapOrange, Offset(pad, pad), Offset(pad + corner, pad), stroke, StrokeCap.Round)
                    drawLine(ChhaapOrange, Offset(pad, pad), Offset(pad, pad + corner), stroke, StrokeCap.Round)
                    // Top-right bracket
                    drawLine(ChhaapOrange, Offset(w - pad, pad), Offset(w - pad - corner, pad), stroke, StrokeCap.Round)
                    drawLine(ChhaapOrange, Offset(w - pad, pad), Offset(w - pad, pad + corner), stroke, StrokeCap.Round)
                    // Bottom-left bracket
                    drawLine(ChhaapOrange, Offset(pad, h - pad), Offset(pad + corner, h - pad), stroke, StrokeCap.Round)
                    drawLine(ChhaapOrange, Offset(pad, h - pad), Offset(pad, h - pad - corner), stroke, StrokeCap.Round)
                    // Bottom-right bracket
                    drawLine(ChhaapOrange, Offset(w - pad, h - pad), Offset(w - pad - corner, h - pad), stroke, StrokeCap.Round)
                    drawLine(ChhaapOrange, Offset(w - pad, h - pad), Offset(w - pad, h - pad - corner), stroke, StrokeCap.Round)

                    // Animated scan laser
                    val laserY = pad + (h - 2 * pad) * beamProgress
                    drawLine(
                        color = Color(0xFF4ADE80),
                        start = Offset(pad + 8.dp.toPx(), laserY),
                        end = Offset(w - pad - 8.dp.toPx(), laserY),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Rounded.QrCodeScanner,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.28f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "READY TO VERIFY",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = JetBrainsMono),
                        color = Color.White.copy(alpha = 0.72f)
                    )
                }
            }

            Text(
                text = "Point at any Chhaap Skill Card or WorkMark QR code.\nSelect a demo QR below to simulate scanning instantly.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.78f),
                textAlign = TextAlign.Center
            )

            // Simulated Scan Options
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "SIMULATE QR SCAN (DEMO)",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = JetBrainsMono),
                    color = WarmGold
                )

                ScanOptionCard(
                    title = "Scan ${primaryWorker.fullName}'s Skill Card",
                    subtitle = "${primaryWorker.primaryTrade.label(language)} · ${primaryWorker.city} · ${primaryWorker.totalVerifiedWorkMarks} Verified WorkMarks · Trust ${primaryWorker.trustScore}",
                    badge = "SKILL CARD QR",
                    badgeColor = VerifiedGreen,
                    icon = Icons.Rounded.Badge,
                    onClick = { onScanWorkerSkillCard(primaryWorker.id) },
                    modifier = Modifier.testTag("scan_ramesh_skill_card")
                )

                if (pendingWorkMark != null) {
                    ScanOptionCard(
                        title = "Scan Pending WorkMark QR",
                        subtitle = "${pendingWorkMark.title} (${pendingWorkMark.id}) · Customer Verification",
                        badge = "CUSTOMER VERIFY",
                        badgeColor = ChhaapOrange,
                        icon = Icons.Rounded.VerifiedUser,
                        onClick = { onScanPendingWorkMark(pendingWorkMark.id) },
                        modifier = Modifier.testTag("scan_pending_workmark")
                    )
                }

                if (secondWorker != null) {
                    ScanOptionCard(
                        title = "Scan ${secondWorker.fullName}'s Skill Card",
                        subtitle = "${secondWorker.primaryTrade.label(language)} · ${secondWorker.city} · Level ${secondWorker.skillLevel} · Trust ${secondWorker.trustScore}",
                        badge = "CONTRACTOR CHECK",
                        badgeColor = PendingAmber,
                        icon = Icons.Rounded.Badge,
                        onClick = { onScanWorkerSkillCard(secondWorker.id) },
                        modifier = Modifier.testTag("scan_second_worker_card")
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ScanOptionCard(
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = DeepNavy,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.14f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(badgeColor.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = JetBrainsMono),
                    color = badgeColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.72f)
                )
            }
            Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Color.White.copy(alpha = 0.6f))
        }
    }
}
