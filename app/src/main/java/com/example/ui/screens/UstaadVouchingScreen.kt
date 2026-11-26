package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MilitaryTech
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppLanguage
import com.example.model.ChhaapStrings
import com.example.model.UstaadVouch
import com.example.model.WorkerProfile
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UstaadVouchingScreen(
    worker: WorkerProfile,
    language: AppLanguage,
    onAddVouch: (
        ustaadName: String,
        ustaadTitle: String,
        yearsExperience: Int,
        relationship: String,
        skills: List<String>,
        note: String
    ) -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    var showVouchDialog by remember { mutableStateOf(false) }

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
            // Top Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("ustaad_back_button")) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = DeepNavy)
                        }
                        Column {
                            Text(
                                text = ChhaapStrings.t(language, "Ustaad Vouching", "उस्ताद समर्थन"),
                                style = MaterialTheme.typography.headlineMedium,
                                color = DeepNavy
                            )
                            Text(
                                text = "${worker.ustaadVouchCount} senior trade endorsements",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateMuted
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = PendingAmberSoft
                    ) {
                        Text(
                            text = "${worker.ustaadVouchCount} Vouches",
                            style = MaterialTheme.typography.labelMedium,
                            color = PendingAmber,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Credibility Policy Note
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = DeepNavy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Rounded.Info, contentDescription = null, tint = ChhaapOrange, modifier = Modifier.size(22.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Mentor Endorsement System",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Senior Ustaads vouch for hands-on trade mastery. Vouches strengthen credibility (10% of Trust Score), but never replace customer-verified WorkMarks.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.82f)
                            )
                        }
                    }
                }
            }

            // Vouch CTA
            item {
                Button(
                    onClick = { showVouchDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ChhaapOrange, contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("vouch_for_skill_button")
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = ChhaapStrings.t(language, "Vouch for Skill (Ustaad Demo)", "कौशल के लिए समर्थन जोड़ें"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            items(worker.vouches, key = { it.id }) { vouch ->
                UstaadVouchCard(vouch = vouch)
            }

            item { Spacer(Modifier.height(10.dp)) }
        }
    }

    if (showVouchDialog) {
        AddUstaadVouchDialog(
            workerName = worker.shortName,
            onDismiss = { showVouchDialog = false },
            onSubmit = { name, title, yrs, rel, skills, note ->
                onAddVouch(name, title, yrs, rel, skills, note)
                showVouchDialog = false
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UstaadVouchCard(vouch: UstaadVouch) {
    val initials = vouch.ustaadName.split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderHairline),
        shadowElevation = 1.dp,
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
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PendingAmberSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.titleMedium,
                        color = PendingAmber,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = vouch.ustaadName,
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepNavy,
                            fontWeight = FontWeight.Bold
                        )
                        if (vouch.verifiedMaster) {
                            Icon(
                                imageVector = Icons.Rounded.Verified,
                                contentDescription = "Verified Ustaad",
                                tint = VerifiedGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "${vouch.ustaadTitle} · ${vouch.yearsExperience} years experience",
                        style = MaterialTheme.typography.bodySmall,
                        color = CharcoalInk,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Relationship: ${vouch.relationship}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateMuted
                    )
                }
            }

            Text(
                text = "“${vouch.note}”",
                style = MaterialTheme.typography.bodyMedium,
                color = CharcoalInk
            )

            Text(
                text = "Endorsed Skills",
                style = MaterialTheme.typography.labelSmall,
                color = SlateMuted,
                fontWeight = FontWeight.SemiBold
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                vouch.endorsedSkills.forEach { skill ->
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = VerifiedGreenSoft
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Rounded.MilitaryTech, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(14.dp))
                            Text(
                                text = skill,
                                style = MaterialTheme.typography.labelSmall,
                                color = VerifiedGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddUstaadVouchDialog(
    workerName: String,
    onDismiss: () -> Unit,
    onSubmit: (String, String, Int, String, List<String>, String) -> Unit
) {
    var name by remember { mutableStateOf("Raghavendra Kulkarni") }
    var title by remember { mutableStateOf("Master Electrician") }
    var years by remember { mutableStateOf("20") }
    var relationship by remember { mutableStateOf("Senior Contractor & Site Mentor") }
    var note by remember { mutableStateOf("Clean earthing, accurate fault diagnosis, and zero rework across apartment sites.") }
    val allSkills = listOf("Residential wiring", "Electrical safety", "Fault diagnosis", "MCB & Switchboard", "Inverter Backup")
    var selectedSkills by remember { mutableStateOf(setOf("Residential wiring", "Electrical safety")) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = SurfaceWhite,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Vouch for $workerName", style = MaterialTheme.typography.titleLarge, color = DeepNavy)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = SlateMuted)
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Ustaad Name") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Trade Title") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.4f)
                    )
                    OutlinedTextField(
                        value = years,
                        onValueChange = { years = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Exp (Yrs)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(0.7f)
                    )
                }
                OutlinedTextField(
                    value = relationship,
                    onValueChange = { relationship = it },
                    label = { Text("Relationship with Worker") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Select Skills to Endorse", style = MaterialTheme.typography.labelMedium, color = DeepNavy)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    allSkills.forEach { skill ->
                        val active = skill in selectedSkills
                        Surface(
                            onClick = {
                                selectedSkills = if (active) selectedSkills - skill else selectedSkills + skill
                            },
                            shape = RoundedCornerShape(50),
                            color = if (active) ChhaapOrangeSoft else SurfaceBg,
                            border = BorderStroke(1.dp, if (active) ChhaapOrange else BorderHairline)
                        ) {
                            Text(
                                text = skill,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (active) ChhaapOrange else CharcoalInk,
                                fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Endorsement Note") },
                    minLines = 2,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        onSubmit(
                            name,
                            title,
                            years.toIntOrNull() ?: 20,
                            relationship,
                            selectedSkills.toList(),
                            note
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen, contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_ustaad_vouch_button")
                ) {
                    Text("Submit Ustaad Vouch", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
