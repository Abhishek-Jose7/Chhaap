package com.example.ui.components

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Carpenter
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Construction
import androidx.compose.material.icons.rounded.FormatPaint
import androidx.compose.material.icons.rounded.Handyman
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Plumbing
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppLanguage
import com.example.model.ChhaapStrings
import com.example.model.EvidenceAttachment
import com.example.model.Trade
import com.example.model.WorkMark
import com.example.model.WorkMarkStatus
import com.example.model.WorkerProfile
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.ChhaapOrange
import com.example.ui.theme.ChhaapOrangeSoft
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.PendingAmber
import com.example.ui.theme.PendingAmberBorder
import com.example.ui.theme.PendingAmberSoft
import com.example.ui.theme.SkillCardBgEnd
import com.example.ui.theme.SkillCardBgMid
import com.example.ui.theme.SkillCardBgStart
import com.example.ui.theme.SkillCardBorder
import com.example.ui.theme.SkillCardGlass
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenBorder
import com.example.ui.theme.VerifiedGreenSoft
import com.example.ui.theme.WarmGold
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.text.NumberFormat
import java.util.Locale

fun formatInr(amount: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))
    return "₹${formatter.format(amount)}"
}

fun tradeIcon(trade: Trade): ImageVector = when (trade) {
    Trade.ELECTRICIAN -> Icons.Rounded.Bolt
    Trade.PLUMBER -> Icons.Rounded.Plumbing
    Trade.CARPENTER -> Icons.Rounded.Carpenter
    Trade.PAINTER -> Icons.Rounded.FormatPaint
    Trade.MECHANIC -> Icons.Rounded.Build
    Trade.MASON -> Icons.Rounded.Construction
    Trade.OTHER -> Icons.Rounded.Handyman
}

@Composable
fun ChhaapBrandBadge(
    modifier: Modifier = Modifier,
    darkText: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ChhaapOrange),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Verified,
                contentDescription = "Chhaap Emblem",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Column {
            Text(
                text = "CHHAAP",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.4.sp
                ),
                color = if (darkText) DeepNavy else Color.White
            )
            Text(
                text = "छाप · Verified Proof",
                style = MaterialTheme.typography.labelSmall,
                color = if (darkText) SlateMuted else Color.White.copy(alpha = 0.72f)
            )
        }
    }
}

@Composable
fun LanguageTogglePill(
    language: AppLanguage,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(50),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderHairline),
        modifier = modifier
            .minimumInteractiveComponentSize()
            .testTag("language_toggle_pill")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "EN",
                style = MaterialTheme.typography.labelMedium,
                color = if (language == AppLanguage.ENGLISH) ChhaapOrange else SlateMuted,
                fontWeight = if (language == AppLanguage.ENGLISH) FontWeight.Bold else FontWeight.Medium
            )
            Text(text = "|", style = MaterialTheme.typography.labelSmall, color = BorderHairline)
            Text(
                text = "हिं",
                style = MaterialTheme.typography.labelMedium,
                color = if (language == AppLanguage.HINDI) ChhaapOrange else SlateMuted,
                fontWeight = if (language == AppLanguage.HINDI) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun VoiceAssistIconButton(
    onClick: () -> Unit,
    isActive: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (isActive) ChhaapOrange else ChhaapOrangeSoft,
        border = BorderStroke(1.dp, if (isActive) ChhaapOrange else ChhaapOrange.copy(alpha = 0.3f)),
        modifier = modifier
            .size(44.dp)
            .minimumInteractiveComponentSize()
            .testTag("voice_assist_button")
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                contentDescription = "Voice Assistance",
                tint = if (isActive) Color.White else ChhaapOrange,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun VoiceAssistBanner(
    message: String?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = message != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        if (message != null) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DeepNavy,
                shadowElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(ChhaapOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                            contentDescription = "Voice Guide Speaking",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Voice Guide Active",
                            style = MaterialTheme.typography.labelSmall,
                            color = WarmGold,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Stop Voice Guide",
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VerificationStatusPill(
    status: WorkMarkStatus,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val isVerified = status == WorkMarkStatus.VERIFIED
    Surface(
        shape = RoundedCornerShape(50),
        color = if (isVerified) VerifiedGreenSoft else PendingAmberSoft,
        border = BorderStroke(1.dp, if (isVerified) VerifiedGreenBorder else PendingAmberBorder),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = if (isVerified) Icons.Rounded.CheckCircle else Icons.Rounded.Schedule,
                contentDescription = null,
                tint = if (isVerified) VerifiedGreen else PendingAmber,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = if (isVerified) ChhaapStrings.verified(language) else ChhaapStrings.pending(language),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (isVerified) VerifiedGreen else PendingAmber
            )
        }
    }
}

@Composable
fun WorkerAvatar(
    worker: WorkerProfile,
    size: Dp = 52.dp,
    modifier: Modifier = Modifier
) {
    val initials = remember(worker.fullName) {
        worker.fullName.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercaseChar().toString() }
            .ifBlank { "RK" }
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(ChhaapOrangeSoft)
            .border(2.dp, SurfaceWhite, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when {
            !worker.avatarUri.isNullOrBlank() -> {
                AsyncImage(
                    model = Uri.parse(worker.avatarUri),
                    contentDescription = worker.fullName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            worker.avatarResId != null -> {
                Image(
                    painter = painterResource(id = worker.avatarResId),
                    contentDescription = worker.fullName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                Text(
                    text = initials,
                    style = MaterialTheme.typography.titleMedium,
                    color = ChhaapOrange,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EvidenceThumbnail(
    attachment: EvidenceAttachment?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceSubtle)
            .border(1.dp, BorderHairline, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        when {
            attachment?.uriString != null -> {
                AsyncImage(
                    model = Uri.parse(attachment.uriString),
                    contentDescription = attachment.label,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            attachment?.sampleResId != null -> {
                Image(
                    painter = painterResource(id = attachment.sampleResId),
                    contentDescription = attachment.label,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                Text(
                    text = "No Photo",
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateMuted
                )
            }
        }
    }
}

/**
 * Standards-compliant ISO/IEC 18004 QR Code renderer powered by ZXing Core.
 * Renders crisp high-contrast modules with a full 4-module white quiet zone
 * so any real iPhone or Android camera scans it immediately from screen.
 */
@Composable
fun QrCodeMatrix(
    data: String,
    modifier: Modifier = Modifier,
    darkColor: Color = Color.Black,
    lightColor: Color = Color.White,
    accentCenter: Boolean = false
) {
    val bitMatrix = remember(data, accentCenter) {
        val hints = mapOf(
            EncodeHintType.ERROR_CORRECTION to if (accentCenter) ErrorCorrectionLevel.H else ErrorCorrectionLevel.M,
            EncodeHintType.MARGIN to 2,
            EncodeHintType.CHARACTER_SET to "UTF-8"
        )
        runCatching {
            QRCodeWriter().encode(data.ifBlank { "CHHAAP" }, BarcodeFormat.QR_CODE, 0, 0, hints)
        }.getOrNull()
    }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = lightColor,
        border = BorderStroke(1.5.dp, BorderHairline),
        shadowElevation = 2.dp,
        modifier = modifier.aspectRatio(1f)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            val matrix = bitMatrix ?: return@Canvas
            val cols = matrix.width
            val rows = matrix.height
            if (cols <= 0 || rows <= 0) return@Canvas

            val cellSize = size.minDimension / maxOf(cols, rows)
            val startX = (size.width - cellSize * cols) / 2f
            val startY = (size.height - cellSize * rows) / 2f

            // Solid crisp white background for maximum camera contrast
            drawRect(color = lightColor, topLeft = Offset.Zero, size = size)

            // Draw exact square modules (+0.4px overlap eliminates anti-alias subpixel hairlines)
            val drawDim = cellSize + 0.4f
            for (y in 0 until rows) {
                for (x in 0 until cols) {
                    if (matrix.get(x, y)) {
                        drawRect(
                            color = darkColor,
                            topLeft = Offset(startX + x * cellSize, startY + y * cellSize),
                            size = Size(drawDim, drawDim)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkMarkListCard(
    workMark: WorkMark,
    language: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = SurfaceWhite,
        border = BorderStroke(
            width = if (workMark.isNewlyVerified) 1.5.dp else 1.dp,
            color = if (workMark.isNewlyVerified) VerifiedGreen else BorderHairline
        ),
        shadowElevation = if (workMark.isNewlyVerified) 3.dp else 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("workmark_card_${workMark.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (workMark.status == WorkMarkStatus.VERIFIED) VerifiedGreenSoft else PendingAmberSoft
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = tradeIcon(workMark.trade),
                        contentDescription = workMark.trade.labelEn,
                        tint = if (workMark.status == WorkMarkStatus.VERIFIED) VerifiedGreen else PendingAmber,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = workMark.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = CharcoalInk,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (workMark.isNewlyVerified) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = VerifiedGreen
                            ) {
                                Text(
                                    text = "NEW",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = null,
                            tint = SlateMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "${workMark.location} · ${workMark.dateLabel}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VerificationStatusPill(status = workMark.status, language = language)
                    if (workMark.rating != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "Rating",
                                tint = WarmGold,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "${workMark.rating}.0",
                                style = MaterialTheme.typography.labelMedium,
                                color = CharcoalInk,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    text = formatInr(workMark.amountInr),
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepNavy,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PremiumSkillCardHero(
    worker: WorkerProfile,
    language: AppLanguage,
    onShowQr: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedMarks by animateIntAsState(
        targetValue = worker.totalVerifiedWorkMarks,
        animationSpec = tween(600),
        label = "verified_marks_anim"
    )
    val animatedTrust by animateIntAsState(
        targetValue = worker.trustScore,
        animationSpec = tween(600),
        label = "trust_score_anim"
    )
    val animatedVouches by animateIntAsState(
        targetValue = worker.ustaadVouchCount,
        animationSpec = tween(600),
        label = "vouches_anim"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(SkillCardBgStart, SkillCardBgMid, SkillCardBgEnd)
                )
            )
            .border(1.dp, SkillCardBorder, RoundedCornerShape(24.dp))
            .animateContentSize()
    ) {
        // Subtle fintech credential guilloche pattern
        Canvas(modifier = Modifier.matchParentSize()) {
            val circleColor = Color.White.copy(alpha = 0.045f)
            drawCircle(
                color = circleColor,
                radius = size.width * 0.42f,
                center = Offset(size.width * 0.92f, size.height * 0.14f),
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(
                color = circleColor,
                radius = size.width * 0.28f,
                center = Offset(size.width * 0.92f, size.height * 0.14f),
                style = Stroke(width = 1.dp.toPx())
            )
            drawCircle(
                color = ChhaapOrange.copy(alpha = 0.10f),
                radius = size.width * 0.35f,
                center = Offset(size.width * 0.08f, size.height * 0.95f)
            )
        }

        Column(modifier = Modifier.padding(20.dp)) {
            // Card Top Header
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
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ChhaapOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VerifiedUser,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "CHHAAP SKILL CARD",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = JetBrainsMono,
                                letterSpacing = 1.2.sp
                            ),
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = ChhaapStrings.t(language, "VERIFIED DIGITAL CREDENTIAL", "सत्यापित डिजिटल प्रमाणपत्र"),
                            style = MaterialTheme.typography.labelSmall,
                            color = WarmGold
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = VerifiedGreen.copy(alpha = 0.22f),
                    border = BorderStroke(1.dp, VerifiedGreen.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Verified,
                            contentDescription = "Verified Identity",
                            tint = Color(0xFF4ADE80),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "LEVEL ${worker.skillLevel}",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = JetBrainsMono),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            // Identity Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                WorkerAvatar(worker = worker, size = 64.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = worker.shortName.uppercase(Locale.US),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "${worker.primaryTrade.label(language)} · ${worker.experienceYears} yrs exp",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.88f)
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "${worker.city.uppercase(Locale.US)}, ${worker.state.uppercase(Locale.US)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = JetBrainsMono,
                            letterSpacing = 1.sp
                        ),
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }

                // Mini QR tap trigger
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .clickable(onClick = onShowQr)
                        .padding(3.dp)
                        .testTag("skill_card_mini_qr")
                ) {
                    QrCodeMatrix(
                        data = com.example.model.QrPayloadBuilder.buildSkillCardPayload(worker),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // Metrics Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SkillCardGlass)
                    .border(1.dp, SkillCardBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SkillCardMetricItem(
                    value = "$animatedTrust",
                    label = "TRUST SCORE",
                    valueColor = Color(0xFF4ADE80)
                )
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(34.dp)
                        .background(Color.White.copy(alpha = 0.16f))
                )
                SkillCardMetricItem(
                    value = "$animatedMarks",
                    label = "WORKMARKS",
                    valueColor = Color.White
                )
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(34.dp)
                        .background(Color.White.copy(alpha = 0.16f))
                )
                SkillCardMetricItem(
                    value = "$animatedVouches",
                    label = "USTAAD VOUCHES",
                    valueColor = WarmGold
                )
            }

            Spacer(Modifier.height(12.dp))

            // Footer credential ID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = worker.publicProfileUrl,
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = JetBrainsMono),
                    color = Color.White.copy(alpha = 0.72f)
                )
                Text(
                    text = "SKILL LEVEL ${worker.skillLevel} · ★ ${worker.averageRating}",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = JetBrainsMono),
                    color = WarmGold,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SkillCardMetricItem(
    value: String,
    label: String,
    valueColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = valueColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                letterSpacing = 0.6.sp
            ),
            color = Color.White.copy(alpha = 0.75f)
        )
    }
}
