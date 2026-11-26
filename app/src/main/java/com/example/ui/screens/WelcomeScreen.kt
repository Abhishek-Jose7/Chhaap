package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.BusinessCenter
import androidx.compose.material.icons.rounded.Construction
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.ChhaapStrings
import com.example.ui.components.ChhaapBrandBadge
import com.example.ui.components.LanguageTogglePill
import com.example.ui.components.VoiceAssistIconButton
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.ChhaapOrange
import com.example.ui.theme.ChhaapOrangeSoft
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SurfaceBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenSoft

@Composable
fun WelcomeScreen(
    language: AppLanguage,
    isVoiceActive: Boolean,
    onToggleLanguage: () -> Unit,
    onSpeakGuide: () -> Unit,
    onGetStarted: () -> Unit,
    onExploreDemo: () -> Unit,
    onOpenContractorMode: () -> Unit,
    onOpenQrScanner: () -> Unit
) {
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Top bar: Brand + Language + Voice
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChhaapBrandBadge()
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LanguageTogglePill(language = language, onToggle = onToggleLanguage)
                    VoiceAssistIconButton(onClick = onSpeakGuide, isActive = isVoiceActive)
                }
            }

            // Hero Illustration Card
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = SurfaceWhite,
                border = BorderStroke(1.dp, BorderHairline),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.img_welcome_hero),
                        contentDescription = "Skilled worker connected to verified Skill Card through QR",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(4f / 3f)
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DeepNavy)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ChhaapStrings.tagline(language),
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = VerifiedGreen
                        ) {
                            Text(
                                text = "QR · OTP Verified",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // Headline & Subtext
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = ChhaapStrings.welcomeHeadline(language),
                    style = MaterialTheme.typography.displayMedium,
                    color = DeepNavy
                )
                Text(
                    text = ChhaapStrings.welcomeSubtext(language),
                    style = MaterialTheme.typography.bodyLarge,
                    color = SlateMuted
                )
            }

            // Three Visual Steps
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SurfaceWhite,
                border = BorderStroke(1.dp, BorderHairline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    WelcomeStepRow(
                        stepNumber = "1",
                        icon = Icons.Rounded.Construction,
                        iconBg = ChhaapOrangeSoft,
                        iconTint = ChhaapOrange,
                        title = ChhaapStrings.t(language, "Do the work", "अपना काम पूरा करें"),
                        subtitle = ChhaapStrings.t(
                            language,
                            "Log your completed job with photos or voice note in 30 seconds.",
                            "फ़ोटो या आवाज़ से 30 सेकंड में काम दर्ज करें।"
                        )
                    )
                    WelcomeStepRow(
                        stepNumber = "2",
                        icon = Icons.Rounded.Verified,
                        iconBg = VerifiedGreenSoft,
                        iconTint = VerifiedGreen,
                        title = ChhaapStrings.t(language, "Customer verifies it", "ग्राहक सत्यापित करे"),
                        subtitle = ChhaapStrings.t(
                            language,
                            "Customer scans your QR code & confirms via OTP — no app needed.",
                            "ग्राहक QR स्कैन करके OTP से पुष्टि करे — बिना किसी ऐप के।"
                        )
                    )
                    WelcomeStepRow(
                        stepNumber = "3",
                        icon = Icons.Rounded.Badge,
                        iconBg = Color(0xFFEAF0FA),
                        iconTint = DeepNavy,
                        title = ChhaapStrings.t(language, "Build your Skill Card", "अपना स्किल कार्ड बनाएं"),
                        subtitle = ChhaapStrings.t(
                            language,
                            "Carry your verified work history & Trust Score to any contractor.",
                            "अपना सत्यापित अनुभव और ट्रस्ट स्कोर किसी भी ठेकेदार को दिखाएं।"
                        )
                    )
                }
            }

            // Primary CTAs
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onExploreDemo,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ChhaapOrange,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("explore_demo_button")
                ) {
                    Text(
                        text = ChhaapStrings.exploreDemo(language) + " — Ramesh (Electrician)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

                OutlinedButton(
                    onClick = onGetStarted,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, DeepNavy),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepNavy),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("get_started_button")
                ) {
                    Text(
                        text = ChhaapStrings.getStarted(language) + " — New Worker Setup",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Secondary Role Quick Entries (Contractor & Verify QR)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(
                    onClick = onOpenContractorMode,
                    modifier = Modifier.testTag("welcome_contractor_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.BusinessCenter,
                        contentDescription = null,
                        tint = DeepNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = ChhaapStrings.t(language, "Contractor View", "ठेकेदार मोड"),
                        style = MaterialTheme.typography.labelLarge,
                        color = DeepNavy
                    )
                }
                TextButton(
                    onClick = onOpenQrScanner,
                    modifier = Modifier.testTag("welcome_scan_qr_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.QrCodeScanner,
                        contentDescription = null,
                        tint = ChhaapOrange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = ChhaapStrings.t(language, "Scan Worker QR", "QR स्कैन करें"),
                        style = MaterialTheme.typography.labelLarge,
                        color = ChhaapOrange
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun WelcomeStepRow(
    stepNumber: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(DeepNavy),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stepNumber,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = CharcoalInk,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = SlateMuted
            )
        }
    }
}
