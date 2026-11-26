package com.example.model

import java.net.URLEncoder

enum class SkillCardQrMode(val label: String, val helperText: String) {
    FULL_CARD(
        label = "Full Skill Card",
        helperText = "Scan to immediately see all verified skills, Trust Score, WorkMarks, Ustaad vouches & contact."
    ),
    WHATSAPP_HIRE(
        label = "WhatsApp Card",
        helperText = "Works on iPhone & Android — opens WhatsApp with full verified skills & credentials."
    ),
    CONTACT_CARD(
        label = "Save Contact",
        helperText = "Saves Ramesh to Contacts with trade, Trust Score & skills right in the contact card."
    )
}

enum class WorkMarkQrMode(val label: String, val helperText: String) {
    WHATSAPP_VERIFY(
        label = "WhatsApp Verify",
        helperText = "Scan with any phone camera to confirm & verify this WorkMark in 1 tap on WhatsApp."
    ),
    SMS_VERIFY(
        label = "SMS Verify",
        helperText = "Works offline! Scan to open a pre-filled verification SMS confirmation."
    ),
    CERTIFICATE_TEXT(
        label = "Proof Text",
        helperText = "Displays full WorkMark details in any QR reader or opens inside Chhaap."
    )
}

object QrPayloadBuilder {

    private fun digitsOnlyPhone(rawPhone: String): String {
        val digits = rawPhone.filter { it.isDigit() }
        return when {
            digits.length == 10 -> "91$digits"
            digits.startsWith("91") && digits.length >= 12 -> digits.take(12)
            digits.isNotBlank() -> digits
            else -> "919820471832"
        }
    }

    private fun urlEncode(value: String): String =
        URLEncoder.encode(value, "UTF-8").replace("+", "%20")

    fun buildSkillCardPayload(
        worker: WorkerProfile,
        mode: SkillCardQrMode = SkillCardQrMode.FULL_CARD
    ): String {
        val intlDigits = digitsOnlyPhone(worker.phoneNumber)
        val skillsBullets = worker.skillBadges.joinToString("\n") { "• $it" }
        val skillsInline = worker.skillBadges.joinToString(", ")
        val topUstaad = worker.vouches.firstOrNull()?.let {
            "${it.ustaadName} (${it.yearsExperience} yrs)"
        } ?: "Verified Trade Masters"

        return when (mode) {
            SkillCardQrMode.FULL_CARD -> buildString {
                appendLine("CHHAAP VERIFIED SKILL CARD")
                appendLine("${worker.fullName} — ${worker.primaryTrade.labelEn}")
                appendLine("${worker.city}, ${worker.state} · ${worker.experienceYears} yrs exp")
                appendLine("Skill Level: ${worker.skillLevel} | Trust Score: ${worker.trustScore}/100")
                appendLine("Verified WorkMarks: ${worker.totalVerifiedWorkMarks} | Rating: ${worker.averageRating}/5")
                appendLine("Ustaad Vouches: ${worker.ustaadVouchCount} (incl. $topUstaad)")
                appendLine("VERIFIED SKILLS:")
                appendLine(skillsBullets)
                append("Contact: +$intlDigits")
            }

            SkillCardQrMode.WHATSAPP_HIRE -> {
                val msg = "CHHAAP VERIFIED SKILL CARD ✓\n" +
                    "${worker.fullName} (${worker.primaryTrade.labelEn} · ${worker.city})\n" +
                    "Skill Level ${worker.skillLevel} | Trust Score: ${worker.trustScore}/100\n" +
                    "${worker.totalVerifiedWorkMarks} Verified WorkMarks | ${worker.ustaadVouchCount} Ustaad Vouches\n" +
                    "Verified Skills: $skillsInline\n\n" +
                    "Hi ${worker.fullName.split(" ").first()}, I scanned your Chhaap Skill Card and would like to discuss a job."
                "https://wa.me/$intlDigits?text=${urlEncode(msg)}"
            }

            SkillCardQrMode.CONTACT_CARD -> buildString {
                appendLine("BEGIN:VCARD")
                appendLine("VERSION:3.0")
                appendLine("FN:${worker.fullName} | ${worker.primaryTrade.labelEn} (Lvl ${worker.skillLevel}, Trust ${worker.trustScore})")
                appendLine("N:${worker.fullName};;;;")
                appendLine("ORG:Skills: $skillsInline")
                appendLine("TITLE:${worker.totalVerifiedWorkMarks} Verified WorkMarks | ${worker.ustaadVouchCount} Ustaad Vouches")
                appendLine("TEL;TYPE=CELL:+$intlDigits")
                appendLine("ADR;TYPE=WORK:;;${worker.city};;${worker.state};;India")
                appendLine("NOTE:Chhaap Verified Skill Card | Trade: ${worker.primaryTrade.labelEn} | Level ${worker.skillLevel} | Trust ${worker.trustScore}/100 | Verified WorkMarks: ${worker.totalVerifiedWorkMarks} | Skills: $skillsInline")
                append("END:VCARD")
            }
        }
    }

    fun buildWorkMarkPayload(
        worker: WorkerProfile,
        workMark: WorkMark,
        mode: WorkMarkQrMode = WorkMarkQrMode.WHATSAPP_VERIFY
    ): String {
        val intlDigits = digitsOnlyPhone(worker.phoneNumber)
        return when (mode) {
            WorkMarkQrMode.WHATSAPP_VERIFY -> {
                val msg = "CHHAAP WORK VERIFIED ✓\n" +
                    "WorkMark ID: ${workMark.id}\n" +
                    "Worker: ${worker.fullName} (${workMark.trade.labelEn})\n" +
                    "Job: ${workMark.title}\n" +
                    "Location: ${workMark.location}\n" +
                    "Amount: Rs.${workMark.amountInr}\n" +
                    "Status: Confirmed Completed (5/5 ★)"
                "https://wa.me/$intlDigits?text=${urlEncode(msg)}"
            }

            WorkMarkQrMode.SMS_VERIFY -> {
                val body = "CHHAAP VERIFY ${workMark.id}: Confirmed ${worker.shortName} completed '${workMark.title}' (Rs.${workMark.amountInr}) in ${workMark.location}. Rating: 5/5"
                "SMSTO:+$intlDigits:$body"
            }

            WorkMarkQrMode.CERTIFICATE_TEXT -> {
                "CHHAAP WORKMARK | ID: ${workMark.id} | Worker: ${worker.fullName} (${workMark.trade.labelEn}) | Job: ${workMark.title} | Amount: Rs.${workMark.amountInr} | Location: ${workMark.location} | Open: chhaap://verify/${workMark.id}"
            }
        }
    }
}
