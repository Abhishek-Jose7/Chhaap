package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.Trade
import com.example.model.WorkerProfile
import com.example.ui.components.WorkerAvatar
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
import com.example.ui.theme.WarmGold

@Composable
fun ContractorViewScreen(
    workers: List<WorkerProfile>,
    language: AppLanguage,
    onViewWorkerSkillCard: (String) -> Unit,
    onOpenQrScanner: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    var searchQuery by remember { mutableStateOf("") }
    var selectedTrade by remember { mutableStateOf<Trade?>(null) }
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var minSkillLevel by remember { mutableIntStateOf(0) }
    var minVerifiedJobs by remember { mutableIntStateOf(0) }

    val cities = remember(workers) { workers.map { it.city }.distinct() }

    val filteredWorkers = remember(workers, searchQuery, selectedTrade, selectedCity, minSkillLevel, minVerifiedJobs) {
        workers.filter { w ->
            val matchesQuery = searchQuery.isBlank() ||
                w.fullName.contains(searchQuery, ignoreCase = true) ||
                w.primaryTrade.labelEn.contains(searchQuery, ignoreCase = true) ||
                w.city.contains(searchQuery, ignoreCase = true) ||
                w.skillBadges.any { it.contains(searchQuery, ignoreCase = true) }
            val matchesTrade = selectedTrade == null || w.primaryTrade == selectedTrade
            val matchesCity = selectedCity == null || w.city.equals(selectedCity, ignoreCase = true)
            val matchesLevel = w.skillLevel >= minSkillLevel
            val matchesJobs = w.totalVerifiedWorkMarks >= minVerifiedJobs
            matchesQuery && matchesTrade && matchesCity && matchesLevel && matchesJobs
        }
    }

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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("contractor_back_button")) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = DeepNavy)
                        }
                        Column {
                            Text(
                                text = "Contractor / Employer View",
                                style = MaterialTheme.typography.headlineSmall,
                                color = DeepNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Hire verified skilled workers",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateMuted
                            )
                        }
                    }
                    Surface(
                        onClick = onOpenQrScanner,
                        shape = RoundedCornerShape(12.dp),
                        color = ChhaapOrangeSoft,
                        modifier = Modifier.testTag("contractor_scan_qr_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Rounded.QrCodeScanner, contentDescription = "Scan QR", tint = ChhaapOrange, modifier = Modifier.size(18.dp))
                            Text("Scan QR", style = MaterialTheme.typography.labelMedium, color = ChhaapOrange, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Core Promise Banner
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = DeepNavy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Rounded.Verified, contentDescription = null, tint = WarmGold, modifier = Modifier.size(28.dp))
                        Column {
                            Text(
                                text = "No resume required. Verify skills through actual completed work.",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "Inspect OTP-verified WorkMarks, before/after photos, and Ustaad vouches before hiring.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.78f)
                            )
                        }
                    }
                }
            }

            // Search Field
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search trade, city, skill or worker name...") },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = SlateMuted) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedBorderColor = ChhaapOrange,
                        unfocusedBorderColor = BorderHairline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contractor_search_input")
                )
            }

            // Trade & City Filter Chips
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ContractorFilterChip("All Trades", selectedTrade == null) { selectedTrade = null }
                        Trade.entries.take(5).forEach { trade ->
                            ContractorFilterChip(trade.label(language), selectedTrade == trade) {
                                selectedTrade = if (selectedTrade == trade) null else trade
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ContractorFilterChip("All Cities", selectedCity == null) { selectedCity = null }
                        cities.forEach { city ->
                            ContractorFilterChip(city, selectedCity == city) {
                                selectedCity = if (selectedCity == city) null else city
                            }
                        }
                        ContractorFilterChip("Level 3+", minSkillLevel == 3) {
                            minSkillLevel = if (minSkillLevel == 3) 0 else 3
                        }
                        ContractorFilterChip("40+ Jobs", minVerifiedJobs == 40) {
                            minVerifiedJobs = if (minVerifiedJobs == 40) 0 else 40
                        }
                    }
                }
            }

            // Results Count
            item {
                Text(
                    text = "${filteredWorkers.size} Verified Workers Found",
                    style = MaterialTheme.typography.labelLarge,
                    color = SlateMuted
                )
            }

            items(filteredWorkers, key = { it.id }) { worker ->
                ContractorWorkerCard(
                    worker = worker,
                    language = language,
                    onViewSkillCard = { onViewWorkerSkillCard(worker.id) }
                )
            }

            item { Spacer(Modifier.height(12.dp)) }
        }
    }
}

@Composable
private fun ContractorFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (selected) DeepNavy else SurfaceWhite,
        border = BorderStroke(1.dp, if (selected) DeepNavy else BorderHairline)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) Color.White else CharcoalInk,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
        )
    }
}

@Composable
private fun ContractorWorkerCard(
    worker: WorkerProfile,
    language: AppLanguage,
    onViewSkillCard: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderHairline),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("contractor_worker_card_${worker.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WorkerAvatar(worker = worker, size = 56.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = worker.shortName,
                            style = MaterialTheme.typography.titleLarge,
                            color = DeepNavy
                        )
                        Icon(
                            imageVector = Icons.Rounded.Verified,
                            contentDescription = "Verified",
                            tint = VerifiedGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "${worker.primaryTrade.label(language)} · ${worker.city} · ${worker.experienceYears} yrs exp",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateMuted
                    )
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = ChhaapOrangeSoft
                ) {
                    Text(
                        text = "Level ${worker.skillLevel}",
                        style = MaterialTheme.typography.labelMedium,
                        color = ChhaapOrange,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            // Verified Credentials Metrics Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceBg, RoundedCornerShape(14.dp))
                    .padding(vertical = 10.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${worker.totalVerifiedWorkMarks}", style = MaterialTheme.typography.titleMedium, color = DeepNavy, fontWeight = FontWeight.ExtraBold)
                    Text("Verified Jobs", style = MaterialTheme.typography.labelSmall, color = SlateMuted)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Icon(Icons.Rounded.Shield, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(15.dp))
                        Text("${worker.trustScore}", style = MaterialTheme.typography.titleMedium, color = VerifiedGreen, fontWeight = FontWeight.ExtraBold)
                    }
                    Text("Trust Score", style = MaterialTheme.typography.labelSmall, color = SlateMuted)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${worker.ustaadVouchCount}", style = MaterialTheme.typography.titleMedium, color = DeepNavy, fontWeight = FontWeight.ExtraBold)
                    Text("Ustaad Vouches", style = MaterialTheme.typography.labelSmall, color = SlateMuted)
                }
            }

            Button(
                onClick = onViewSkillCard,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy, contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("view_skill_card_${worker.id}")
            ) {
                Icon(Icons.Rounded.Badge, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("View Skill Card", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}
