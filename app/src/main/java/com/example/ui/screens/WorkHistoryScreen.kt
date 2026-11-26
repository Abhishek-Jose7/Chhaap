package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppLanguage
import com.example.model.ChhaapStrings
import com.example.model.Trade
import com.example.model.WorkMark
import com.example.model.WorkMarkStatus
import com.example.model.WorkerProfile
import com.example.ui.components.EvidenceThumbnail
import com.example.ui.components.VerificationStatusPill
import com.example.ui.components.WorkMarkListCard
import com.example.ui.components.formatInr
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
import com.example.ui.theme.WarmGold

private enum class StatusFilter { ALL, VERIFIED, PENDING }
private enum class DateSort { NEWEST, HIGHEST_AMOUNT }

@Composable
fun WorkHistoryScreen(
    worker: WorkerProfile,
    language: AppLanguage,
    onCreateWorkMark: () -> Unit,
    onVerifyPendingWorkMark: (String) -> Unit
) {
    var statusFilter by remember { mutableStateOf(StatusFilter.ALL) }
    var tradeFilter by remember { mutableStateOf<Trade?>(null) }
    var dateSort by remember { mutableStateOf(DateSort.NEWEST) }
    var inspectingWorkMark by remember { mutableStateOf<WorkMark?>(null) }

    val filteredMarks = remember(worker.workMarks, statusFilter, tradeFilter, dateSort) {
        worker.workMarks
            .filter { wm ->
                when (statusFilter) {
                    StatusFilter.ALL -> true
                    StatusFilter.VERIFIED -> wm.status == WorkMarkStatus.VERIFIED
                    StatusFilter.PENDING -> wm.status == WorkMarkStatus.PENDING
                }
            }
            .filter { wm -> tradeFilter == null || wm.trade == tradeFilter }
            .let { list ->
                when (dateSort) {
                    DateSort.NEWEST -> list.sortedByDescending { it.timestampOrder }
                    DateSort.HIGHEST_AMOUNT -> list.sortedByDescending { it.amountInr }
                }
            }
    }

    val totalEarnings = remember(worker.workMarks) { worker.workMarks.sumOf { it.amountInr } }
    val pendingCount = remember(worker.workMarks) { worker.workMarks.count { it.status == WorkMarkStatus.PENDING } }

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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = ChhaapStrings.t(language, "Work History", "मेरे काम का रिकॉर्ड"),
                            style = MaterialTheme.typography.headlineMedium,
                            color = DeepNavy
                        )
                        Text(
                            text = "${worker.totalVerifiedWorkMarks} Verified · $pendingCount Pending · ${formatInr(totalEarnings)} recent",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateMuted
                        )
                    }
                    Surface(
                        onClick = onCreateWorkMark,
                        shape = RoundedCornerShape(12.dp),
                        color = ChhaapOrange,
                        modifier = Modifier.testTag("history_add_workmark_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Text("New", style = MaterialTheme.typography.labelLarge, color = Color.White)
                        }
                    }
                }
            }

            // Status & Date Sort Filter Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChipPill(
                        label = "All (${worker.workMarks.size})",
                        selected = statusFilter == StatusFilter.ALL,
                        onClick = { statusFilter = StatusFilter.ALL }
                    )
                    FilterChipPill(
                        label = ChhaapStrings.verified(language),
                        selected = statusFilter == StatusFilter.VERIFIED,
                        onClick = { statusFilter = StatusFilter.VERIFIED }
                    )
                    FilterChipPill(
                        label = "${ChhaapStrings.pending(language)} ($pendingCount)",
                        selected = statusFilter == StatusFilter.PENDING,
                        onClick = { statusFilter = StatusFilter.PENDING }
                    )
                    FilterChipPill(
                        label = if (dateSort == DateSort.NEWEST) "Date: Newest" else "Sort: Highest ₹",
                        selected = true,
                        accentOrange = true,
                        onClick = {
                            dateSort = if (dateSort == DateSort.NEWEST) DateSort.HIGHEST_AMOUNT else DateSort.NEWEST
                        }
                    )
                }
            }

            // Trade Filter Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChipPill(
                        label = "All Trades",
                        selected = tradeFilter == null,
                        onClick = { tradeFilter = null }
                    )
                    Trade.entries.take(5).forEach { trade ->
                        FilterChipPill(
                            label = trade.label(language),
                            selected = tradeFilter == trade,
                            onClick = { tradeFilter = if (tradeFilter == trade) null else trade }
                        )
                    }
                }
            }

            if (filteredMarks.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = SurfaceWhite,
                        border = BorderStroke(1.dp, BorderHairline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("No WorkMarks match these filters", style = MaterialTheme.typography.titleMedium, color = DeepNavy)
                            Text("Try switching filters or log a new completed job.", style = MaterialTheme.typography.bodySmall, color = SlateMuted)
                        }
                    }
                }
            } else {
                items(filteredMarks, key = { it.id }) { workMark ->
                    WorkMarkListCard(
                        workMark = workMark,
                        language = language,
                        onClick = { inspectingWorkMark = workMark }
                    )
                }
            }

            item { Spacer(Modifier.height(12.dp)) }
        }
    }

    // WorkMark Evidence & Verification Record Detail Modal
    inspectingWorkMark?.let { wm ->
        WorkMarkDetailDialog(
            workMark = wm,
            language = language,
            onDismiss = { inspectingWorkMark = null },
            onVerifyNow = {
                inspectingWorkMark = null
                onVerifyPendingWorkMark(wm.id)
            }
        )
    }
}

@Composable
private fun FilterChipPill(
    label: String,
    selected: Boolean,
    accentOrange: Boolean = false,
    onClick: () -> Unit
) {
    val bg = when {
        selected && accentOrange -> ChhaapOrangeSoft
        selected -> DeepNavy
        else -> SurfaceWhite
    }
    val textColor = when {
        selected && accentOrange -> ChhaapOrange
        selected -> Color.White
        else -> CharcoalInk
    }
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = bg,
        border = BorderStroke(1.dp, if (selected && accentOrange) ChhaapOrange else if (selected) DeepNavy else BorderHairline)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun WorkMarkDetailDialog(
    workMark: WorkMark,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onVerifyNow: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = SurfaceWhite,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = workMark.id,
                            style = MaterialTheme.typography.labelMedium.copy(fontFamily = JetBrainsMono),
                            color = ChhaapOrange,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "WorkMark Verification Record",
                            style = MaterialTheme.typography.titleLarge,
                            color = DeepNavy
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = SlateMuted)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    VerificationStatusPill(status = workMark.status, language = language)
                    Text(
                        text = formatInr(workMark.amountInr),
                        style = MaterialTheme.typography.headlineSmall,
                        color = DeepNavy,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Text(
                    text = workMark.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = CharcoalInk,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = workMark.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateMuted
                )

                HorizontalDivider(color = BorderHairline)

                DetailRecordRow("Trade", workMark.trade.label(language))
                DetailRecordRow("Location", workMark.location)
                DetailRecordRow("Date", workMark.dateLabel)
                DetailRecordRow("Customer", "${workMark.customerName} (${workMark.customerPhoneMasked})")
                DetailRecordRow("Verification", workMark.verificationMethod)

                if (workMark.beforePhoto?.hasPhoto == true || workMark.afterPhoto?.hasPhoto == true) {
                    Text("Submitted Evidence", style = MaterialTheme.typography.labelLarge, color = DeepNavy)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        workMark.beforePhoto?.let {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                EvidenceThumbnail(it, Modifier.fillMaxWidth().aspectRatio(4f / 3f))
                                Text("Before", style = MaterialTheme.typography.labelSmall, color = SlateMuted)
                            }
                        }
                        workMark.afterPhoto?.let {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                EvidenceThumbnail(it, Modifier.fillMaxWidth().aspectRatio(4f / 3f))
                                Text("After", style = MaterialTheme.typography.labelSmall, color = VerifiedGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                if (!workMark.workerVoiceNote.isNullOrBlank()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceBg,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.GraphicEq, contentDescription = null, tint = ChhaapOrange)
                            Text(
                                text = "Worker Voice Note: “${workMark.workerVoiceNote}”",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalInk
                            )
                        }
                    }
                }

                if (!workMark.customerFeedback.isNullOrBlank()) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = VerifiedGreenSoft,
                        border = BorderStroke(1.dp, VerifiedGreenBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Rounded.Verified, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(16.dp))
                                Text("Customer Feedback", style = MaterialTheme.typography.labelMedium, color = VerifiedGreen, fontWeight = FontWeight.Bold)
                                if (workMark.rating != null) {
                                    Spacer(Modifier.width(6.dp))
                                    Icon(Icons.Rounded.Star, contentDescription = null, tint = WarmGold, modifier = Modifier.size(14.dp))
                                    Text("${workMark.rating}.0", style = MaterialTheme.typography.labelSmall, color = DeepNavy, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text("“${workMark.customerFeedback}”", style = MaterialTheme.typography.bodySmall, color = CharcoalInk)
                        }
                    }
                }

                if (workMark.status == WorkMarkStatus.PENDING) {
                    Button(
                        onClick = onVerifyNow,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ChhaapOrange, contentColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("detail_verify_now_button")
                    ) {
                        Icon(Icons.Rounded.QrCode2, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Open Customer Verification", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRecordRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = SlateMuted)
        Text(value, style = MaterialTheme.typography.bodySmall, color = CharcoalInk, fontWeight = FontWeight.SemiBold)
    }
}
