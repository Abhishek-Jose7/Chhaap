package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.GppGood
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.ChhaapStrings
import com.example.model.DemoSeedData
import com.example.model.WorkerProfile
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

@Composable
fun TrustScoreScreen(
    worker: WorkerProfile,
    language: AppLanguage,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    val factors = remember(worker) { DemoSeedData.buildTrustFactors(worker) }
    val fraudChecks = DemoSeedData.antiFraudSignals

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("trust_score_back")) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = DeepNavy)
                        }
                        Column {
                            Text(
                                text = ChhaapStrings.t(language, "Trust Score Breakdown", "ट्रस्ट स्कोर विवरण"),
                                style = MaterialTheme.typography.headlineMedium,
                                color = DeepNavy
                            )
                            Text(
                                text = "Transparent proof-based credibility",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateMuted
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = ChhaapOrangeSoft
                    ) {
                        Text(
                            text = "DEMO MODEL",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = JetBrainsMono),
                            color = ChhaapOrange,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Hero Trust Gauge Card
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = SurfaceWhite,
                    border = BorderStroke(1.dp, BorderHairline),
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(164.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val strokeWidth = 14.dp.toPx()
                                val inset = strokeWidth / 2f
                                val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                                drawArc(
                                    color = Color(0xFFE8EEF5),
                                    startAngle = 140f,
                                    sweepAngle = 260f,
                                    useCenter = false,
                                    topLeft = Offset(inset, inset),
                                    size = arcSize,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                                drawArc(
                                    color = VerifiedGreen,
                                    startAngle = 140f,
                                    sweepAngle = 260f * (worker.trustScore / 100f),
                                    useCenter = false,
                                    topLeft = Offset(inset, inset),
                                    size = arcSize,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${worker.trustScore}",
                                    style = MaterialTheme.typography.displayLarge,
                                    color = DeepNavy,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "out of 100",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = SlateMuted
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = VerifiedGreenSoft,
                            border = BorderStroke(1.dp, VerifiedGreenBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Rounded.VerifiedUser, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "High Trust · Top 15% in ${worker.city}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = VerifiedGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = "Unlike star-rating apps, Chhaap Trust Score combines OTP-verified jobs, repeat homeowners, evidence quality, and mentor vouches.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateMuted
                        )
                    }
                }
            }

            // Score Factors Header
            item {
                Text(
                    text = "Score Composition (Demo Calculation)",
                    style = MaterialTheme.typography.titleLarge,
                    color = DeepNavy
                )
            }

            items(factors) { factor ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceWhite,
                    border = BorderStroke(1.dp, BorderHairline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = ChhaapStrings.t(language, factor.titleEn, factor.titleHi),
                                style = MaterialTheme.typography.titleSmall,
                                color = CharcoalInk,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SurfaceBg
                            ) {
                                Text(
                                    text = "Weight ${factor.weightPercent}%",
                                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = JetBrainsMono),
                                    color = DeepNavy,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        LinearProgressIndicator(
                            progress = { (factor.achievedPercent / 100f).coerceIn(0f, 1f) },
                            color = if (factor.weightPercent >= 25) VerifiedGreen else ChhaapOrange,
                            trackColor = SurfaceBg,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = factor.detail,
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateMuted,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${factor.achievedPercent}%",
                                style = MaterialTheme.typography.labelMedium,
                                color = DeepNavy,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Anti-Fraud Protection Section
            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Anti-Fraud Protection Signals",
                    style = MaterialTheme.typography.titleLarge,
                    color = DeepNavy
                )
            }

            items(fraudChecks) { check ->
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
                        Icon(
                            imageVector = Icons.Rounded.GppGood,
                            contentDescription = null,
                            tint = VerifiedGreen,
                            modifier = Modifier.size(26.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ChhaapStrings.t(language, check.titleEn, check.titleHi),
                                style = MaterialTheme.typography.titleSmall,
                                color = CharcoalInk,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = check.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateMuted
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = VerifiedGreenSoft
                        ) {
                            Text(
                                text = check.statusLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = VerifiedGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Demo Note
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = ChhaapOrangeSoft,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Rounded.Info, contentDescription = null, tint = ChhaapOrange, modifier = Modifier.size(18.dp))
                        Text(
                            text = "Demo Calculation Notice: Weights and anti-fraud signals shown above are simulated locally for this interactive Chhaap prototype.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CharcoalInk
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(10.dp)) }
        }
    }
}
