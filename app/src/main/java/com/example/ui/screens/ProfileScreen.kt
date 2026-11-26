package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.BusinessCenter
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.CloudDone
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MilitaryTech
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.ChhaapStrings
import com.example.model.Trade
import com.example.model.WorkerProfile
import com.example.ui.components.LanguageTogglePill
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

@Composable
fun ProfileScreen(
    worker: WorkerProfile,
    language: AppLanguage,
    voiceAssistEnabled: Boolean,
    offlineMode: Boolean,
    lastSyncedLabel: String,
    onToggleLanguage: () -> Unit,
    onToggleVoiceAssist: (Boolean) -> Unit,
    onTestVoiceAssist: () -> Unit,
    onToggleOfflineMode: (Boolean) -> Unit,
    onSyncNow: () -> Unit,
    onUpdateProfile: (String, String, String, Trade, Int) -> Unit,
    onOpenContractorView: () -> Unit,
    onOpenTrustScore: () -> Unit,
    onOpenUstaadVouching: () -> Unit,
    onResetDemo: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

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
            item {
                Text(
                    text = ChhaapStrings.t(language, "Profile & Settings", "प्रोफ़ाइल और सेटिंग्स"),
                    style = MaterialTheme.typography.headlineMedium,
                    color = DeepNavy
                )
            }

            // Worker Identity Card
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceWhite,
                    border = BorderStroke(1.dp, BorderHairline),
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        WorkerAvatar(worker = worker, size = 62.dp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = worker.fullName,
                                style = MaterialTheme.typography.titleLarge,
                                color = DeepNavy
                            )
                            Text(
                                text = "${worker.primaryTrade.label(language)} · ${worker.experienceYears} yrs · ${worker.city}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SlateMuted
                            )
                            Text(
                                text = worker.phoneNumber,
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateMuted
                            )
                        }
                        Surface(
                            onClick = { showEditDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            color = SurfaceBg,
                            modifier = Modifier.testTag("profile_edit_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = "Edit profile",
                                tint = DeepNavy,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            // Accessibility & Language Card
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceWhite,
                    border = BorderStroke(1.dp, BorderHairline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = ChhaapStrings.t(language, "Language & Voice Accessibility", "भाषा और आवाज़ सहायता"),
                            style = MaterialTheme.typography.titleSmall,
                            color = DeepNavy,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Rounded.Translate, contentDescription = null, tint = ChhaapOrange)
                                Column {
                                    Text("App Language", style = MaterialTheme.typography.titleSmall, color = CharcoalInk)
                                    Text("Current: ${language.label}", style = MaterialTheme.typography.bodySmall, color = SlateMuted)
                                }
                            }
                            LanguageTogglePill(language = language, onToggle = onToggleLanguage)
                        }

                        HorizontalDivider(color = BorderHairline)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.AutoMirrored.Rounded.VolumeUp, contentDescription = null, tint = DeepNavy)
                                Column {
                                    Text("Voice Assistance", style = MaterialTheme.typography.titleSmall, color = CharcoalInk)
                                    Text("Spoken instructions in ${language.label}", style = MaterialTheme.typography.bodySmall, color = SlateMuted)
                                }
                            }
                            Switch(
                                checked = voiceAssistEnabled,
                                onCheckedChange = onToggleVoiceAssist,
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ChhaapOrange),
                                modifier = Modifier.testTag("profile_voice_switch")
                            )
                        }

                        OutlinedButton(
                            onClick = onTestVoiceAssist,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.AutoMirrored.Rounded.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Play Voice Guide Preview")
                        }
                    }
                }
            }

            // Offline & Sync Card
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceWhite,
                    border = BorderStroke(1.dp, BorderHairline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Offline Mode & Sync",
                            style = MaterialTheme.typography.titleSmall,
                            color = DeepNavy,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = if (offlineMode) Icons.Rounded.CloudOff else Icons.Rounded.CloudDone,
                                    contentDescription = null,
                                    tint = if (offlineMode) PendingAmber else VerifiedGreen
                                )
                                Column {
                                    Text(
                                        text = if (offlineMode) "Offline Mode Active" else "Online · Auto-Sync On",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = CharcoalInk
                                    )
                                    Text(
                                        text = "Log jobs even on weak site network",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SlateMuted
                                    )
                                }
                            }
                            Switch(
                                checked = offlineMode,
                                onCheckedChange = onToggleOfflineMode,
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PendingAmber),
                                modifier = Modifier.testTag("profile_offline_switch")
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (offlineMode) PendingAmberSoft else VerifiedGreenSoft,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = lastSyncedLabel,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CharcoalInk,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(
                                    onClick = onSyncNow,
                                    modifier = Modifier.testTag("profile_sync_now_button")
                                ) {
                                    Icon(Icons.Rounded.Sync, contentDescription = null, modifier = Modifier.size(16.dp), tint = DeepNavy)
                                    Spacer(Modifier.width(4.dp))
                                    Text("Sync", color = DeepNavy, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Navigation Links Card (Contractor View, Trust, Ustaad, Help, Privacy)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceWhite,
                    border = BorderStroke(1.dp, BorderHairline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        ProfileMenuRow(
                            icon = Icons.Rounded.BusinessCenter,
                            iconTint = ChhaapOrange,
                            title = "Contractor / Employer View",
                            subtitle = "Search & inspect verified workers",
                            onClick = onOpenContractorView,
                            testTag = "profile_contractor_row"
                        )
                        HorizontalDivider(color = BorderHairline, modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileMenuRow(
                            icon = Icons.Rounded.Shield,
                            iconTint = VerifiedGreen,
                            title = "Trust Score & Anti-Fraud",
                            subtitle = "Score ${worker.trustScore}/100 · Transparent breakdown",
                            onClick = onOpenTrustScore,
                            testTag = "profile_trust_row"
                        )
                        HorizontalDivider(color = BorderHairline, modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileMenuRow(
                            icon = Icons.Rounded.MilitaryTech,
                            iconTint = PendingAmber,
                            title = "Ustaad Vouching",
                            subtitle = "${worker.ustaadVouchCount} mentor endorsements",
                            onClick = onOpenUstaadVouching,
                            testTag = "profile_ustaad_row"
                        )
                        HorizontalDivider(color = BorderHairline, modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileMenuRow(
                            icon = Icons.AutoMirrored.Rounded.HelpOutline,
                            iconTint = DeepNavy,
                            title = "Help & Worker Support",
                            subtitle = "How WorkMarks, OTP & Skill Cards work",
                            onClick = { showHelpDialog = true },
                            testTag = "profile_help_row"
                        )
                        HorizontalDivider(color = BorderHairline, modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileMenuRow(
                            icon = Icons.Rounded.Lock,
                            iconTint = DeepNavy,
                            title = "Privacy & Data Ownership",
                            subtitle = "Your Skill Card belongs to you forever",
                            onClick = { showPrivacyDialog = true },
                            testTag = "profile_privacy_row"
                        )
                    }
                }
            }

            // Reset Demo Data Button (Useful for live judge demos)
            item {
                OutlinedButton(
                    onClick = onResetDemo,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, BorderHairline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("reset_demo_button")
                ) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null, tint = SlateMuted, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Reset Hackathon Demo State", color = SlateMuted)
                }
            }

            item { Spacer(Modifier.height(12.dp)) }
        }
    }

    if (showEditDialog) {
        var name by remember { mutableStateOf(worker.fullName) }
        var phone by remember { mutableStateOf(worker.phoneNumber) }
        var city by remember { mutableStateOf(worker.city) }
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Worker Profile", color = DeepNavy) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, singleLine = true)
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, singleLine = true)
                    OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(name, phone, city, worker.primaryTrade, worker.experienceYears)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ChhaapOrange)
                ) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("Help & Support", color = DeepNavy) },
            text = {
                Text(
                    "1. Complete any job and tap '+ Create WorkMark'.\n" +
                        "2. Add before/after photos or record a voice note.\n" +
                        "3. Show the generated QR code to your customer — they can verify via OTP without installing any app.\n" +
                        "4. Every verified WorkMark updates your portable Chhaap Skill Card automatically."
                )
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) { Text("Got it", color = ChhaapOrange) }
            }
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy & Credential Ownership", color = DeepNavy) },
            text = {
                Text(
                    "Chhaap credentials belong to the worker — never locked to a single platform or contractor. " +
                        "Customer phone numbers are always masked on public Skill Cards, and you control when to share your verified link or QR."
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) { Text("Close", color = DeepNavy) }
            }
        )
    }
}

@Composable
private fun ProfileMenuRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, color = CharcoalInk, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = SlateMuted)
            }
            Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = SlateMuted)
        }
    }
}
