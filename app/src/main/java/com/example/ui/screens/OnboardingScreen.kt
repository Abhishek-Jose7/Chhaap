package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.AppLanguage
import com.example.model.ChhaapStrings
import com.example.model.Trade
import com.example.ui.components.LanguageTogglePill
import com.example.ui.components.tradeIcon
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.ChhaapOrange
import com.example.ui.theme.ChhaapOrangeSoft
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SurfaceBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.VerifiedGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    language: AppLanguage,
    onToggleLanguage: () -> Unit,
    onSpeakOnboardingGuide: () -> Unit,
    onBack: () -> Unit,
    onSkipUseDemo: () -> Unit,
    onSubmitProfile: (
        fullName: String,
        phone: String,
        trade: Trade,
        experienceYears: Int,
        city: String,
        avatarUri: String?
    ) -> Unit
) {
    BackHandler(onBack = onBack)

    var fullName by remember { mutableStateOf("Ramesh Kumar") }
    var phone by remember { mutableStateOf("+91 98204 71832") }
    var selectedTrade by remember { mutableStateOf(Trade.ELECTRICIAN) }
    var experienceYears by remember { mutableIntStateOf(8) }
    var city by remember { mutableStateOf("Thane") }
    var avatarUri by remember { mutableStateOf<String?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            avatarUri = uri.toString()
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 580.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("onboarding_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepNavy
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LanguageTogglePill(language = language, onToggle = onToggleLanguage)
                    TextButton(
                        onClick = onSkipUseDemo,
                        modifier = Modifier.testTag("onboarding_skip_button")
                    ) {
                        Text(
                            text = ChhaapStrings.t(language, "Use Demo", "डेमो उपयोग करें"),
                            color = ChhaapOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Title + Voice Guided Card
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = ChhaapStrings.t(language, "Create Your Skill Profile", "अपना स्किल प्रोफ़ाइल बनाएं"),
                    style = MaterialTheme.typography.headlineLarge,
                    color = DeepNavy
                )
                Text(
                    text = ChhaapStrings.t(
                        language,
                        "Takes under 1 minute. Start collecting verified WorkMarks immediately.",
                        "1 मिनट से कम लगता है। तुरंत सत्यापित वर्कमार्क जोड़ना शुरू करें।"
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateMuted
                )
            }

            // Voice-guided onboarding button
            Surface(
                onClick = onSpeakOnboardingGuide,
                shape = RoundedCornerShape(16.dp),
                color = ChhaapOrangeSoft,
                border = BorderStroke(1.dp, ChhaapOrange.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("voice_guided_onboarding_button")
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
                            .background(ChhaapOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                            contentDescription = "Voice guided onboarding",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ChhaapStrings.t(language, "Voice-Guided Onboarding", "बोलकर सहायता सुनें"),
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepNavy,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = ChhaapStrings.t(
                                language,
                                "Tap to hear step-by-step instructions in ${language.label}",
                                "हिन्दी में आसान निर्देश सुनने के लिए यहाँ दबाएं"
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateMuted
                        )
                    }
                }
            }

            // Profile Photo Card (optional)
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = SurfaceWhite,
                border = BorderStroke(1.dp, BorderHairline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, ChhaapOrange, CircleShape)
                            .clickable {
                                photoPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (avatarUri != null) {
                            AsyncImage(
                                model = Uri.parse(avatarUri),
                                contentDescription = "Profile photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.img_worker_ramesh),
                                contentDescription = "Sample profile photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ChhaapStrings.t(language, "Profile Photo (Optional)", "प्रोफ़ाइल फ़ोटो (वैकल्पिक)"),
                            style = MaterialTheme.typography.titleMedium,
                            color = CharcoalInk
                        )
                        Text(
                            text = ChhaapStrings.t(
                                language,
                                "Helps customers & contractors recognize you",
                                "ग्राहक और ठेकेदार को आपको पहचानने में मदद मिलती है"
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateMuted
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            photoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("onboarding_photo_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CameraAlt,
                            contentDescription = "Choose photo",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Form Fields Card
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
                    val fieldColors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChhaapOrange,
                        unfocusedBorderColor = BorderHairline,
                        focusedLabelColor = ChhaapOrange
                    )

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text(ChhaapStrings.t(language, "Full Name", "पूरा नाम")) },
                        leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null, tint = DeepNavy) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("onboarding_name_input")
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text(ChhaapStrings.t(language, "Phone Number", "मोबाइल नंबर")) },
                        leadingIcon = { Icon(Icons.Rounded.Phone, contentDescription = null, tint = DeepNavy) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("onboarding_phone_input")
                    )

                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text(ChhaapStrings.t(language, "City", "शहर")) },
                        leadingIcon = { Icon(Icons.Rounded.LocationCity, contentDescription = null, tint = DeepNavy) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("onboarding_city_input")
                    )

                    // Primary Trade visual chips
                    Text(
                        text = ChhaapStrings.t(language, "Primary Trade", "मुख्य काम / हुनर"),
                        style = MaterialTheme.typography.titleSmall,
                        color = DeepNavy,
                        fontWeight = FontWeight.Bold
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Trade.entries.forEach { trade ->
                            val selected = trade == selectedTrade
                            Surface(
                                onClick = { selectedTrade = trade },
                                shape = RoundedCornerShape(14.dp),
                                color = if (selected) DeepNavy else SurfaceBg,
                                border = BorderStroke(
                                    width = if (selected) 1.5.dp else 1.dp,
                                    color = if (selected) DeepNavy else BorderHairline
                                ),
                                modifier = Modifier.testTag("trade_chip_${trade.name.lowercase()}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = tradeIcon(trade),
                                        contentDescription = null,
                                        tint = if (selected) ChhaapOrange else SlateMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = trade.label(language),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (selected) Color.White else CharcoalInk
                                    )
                                }
                            }
                        }
                    }

                    // Experience Stepper (Large accessible controls)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceBg)
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = ChhaapStrings.t(language, "Experience (Years)", "अनुभव (वर्षों में)"),
                                style = MaterialTheme.typography.titleSmall,
                                color = DeepNavy
                            )
                            Text(
                                text = "$experienceYears " + ChhaapStrings.t(language, "years hands-on", "साल का अनुभव"),
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateMuted
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                onClick = { if (experienceYears > 1) experienceYears-- },
                                shape = CircleShape,
                                color = SurfaceWhite,
                                border = BorderStroke(1.dp, BorderHairline),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Rounded.Remove, contentDescription = "Decrease years", tint = DeepNavy)
                                }
                            }
                            Text(
                                text = "$experienceYears",
                                style = MaterialTheme.typography.headlineSmall,
                                color = DeepNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                onClick = { if (experienceYears < 40) experienceYears++ },
                                shape = CircleShape,
                                color = ChhaapOrange,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Rounded.Add, contentDescription = "Increase years", tint = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            // Submit Button
            Button(
                onClick = {
                    onSubmitProfile(fullName, phone, selectedTrade, experienceYears, city, avatarUri)
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ChhaapOrange,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("onboarding_submit_button")
            ) {
                Icon(Icons.Rounded.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = ChhaapStrings.t(language, "Continue to Dashboard", "डैशबोर्ड पर जाएं"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}
