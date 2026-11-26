package com.example.model

import com.example.R

object DemoSeedData {

    fun createRameshWorkMarks(): List<WorkMark> = listOf(
        WorkMark(
            id = "CHH-2026-THN-4208",
            title = "Electrical wiring installation",
            trade = Trade.ELECTRICIAN,
            description = "Installed full concealed 2BHK apartment copper wiring with Finolex FR cables and 3 modular switchboards.",
            customerName = "Sunil Kulkarni",
            customerPhoneMasked = "+91 98204 •••41",
            amountInr = 4800,
            location = "Hiranandani Estate, Thane",
            dateLabel = "2 days ago · 6:15 PM",
            timestampOrder = 4208L,
            status = WorkMarkStatus.VERIFIED,
            rating = 5,
            customerFeedback = "Clean concealed wiring, proper earthing check, and finished on schedule without mess.",
            customerVoiceNote = "Voice feedback · 0:18 (Verified Hindi audio)",
            workerVoiceNote = "2BHK hall aur bedroom ki concealed wiring poori kar di hai, earthing test pass.",
            beforePhoto = EvidenceAttachment("Before work", sampleResId = R.drawable.img_work_before),
            afterPhoto = EvidenceAttachment("After work", sampleResId = R.drawable.img_work_after),
            finishedPhoto = EvidenceAttachment("Finished switchboard", sampleResId = R.drawable.img_work_after),
            verifiedViaOtp = true,
            verificationMethod = "QR Scan + OTP",
            isRepeatCustomer = true
        ),
        WorkMark(
            id = "CHH-2026-THN-4207",
            title = "Ceiling fan installation",
            trade = Trade.ELECTRICIAN,
            description = "Installed 3 BLDC ceiling fans with electronic regulators and balanced blades across bedrooms.",
            customerName = "Meera Joshi",
            customerPhoneMasked = "+91 98192 •••83",
            amountInr = 1200,
            location = "Ghodbunder Road, Thane",
            dateLabel = "5 days ago · 2:40 PM",
            timestampOrder = 4207L,
            status = WorkMarkStatus.VERIFIED,
            rating = 5,
            customerFeedback = "Very polite and quick. Checked voltage stability before handing over.",
            workerVoiceNote = "3 Atomberg BLDC fans aur regulator lagaye hain.",
            beforePhoto = EvidenceAttachment("Before work", sampleResId = R.drawable.img_work_before),
            afterPhoto = EvidenceAttachment("After work", sampleResId = R.drawable.img_work_after),
            verifiedViaOtp = true,
            verificationMethod = "QR Scan + OTP",
            isRepeatCustomer = true
        ),
        WorkMark(
            id = "CHH-2026-THN-4206",
            title = "MCB replacement",
            trade = Trade.ELECTRICIAN,
            description = "Replaced tripping 32A distribution box MCBs with Havells double-pole isolator and RCCB safety trip.",
            customerName = "Rajeshwar Nair",
            customerPhoneMasked = "+91 97690 •••19",
            amountInr = 1650,
            location = "Vasant Vihar, Thane",
            dateLabel = "1 week ago · 11:20 AM",
            timestampOrder = 4206L,
            status = WorkMarkStatus.VERIFIED,
            rating = 5,
            customerFeedback = "Solved our AC tripping problem permanently. Transparent billing.",
            beforePhoto = EvidenceAttachment("Before work", sampleResId = R.drawable.img_work_before),
            afterPhoto = EvidenceAttachment("After work", sampleResId = R.drawable.img_work_after),
            verifiedViaOtp = true,
            verificationMethod = "OTP Confirmed",
            isRepeatCustomer = false
        ),
        WorkMark(
            id = "CHH-2026-THN-4205",
            title = "Lighting repair",
            trade = Trade.ELECTRICIAN,
            description = "Repaired false ceiling COB spotlights and replaced LED strip driver in living room.",
            customerName = "Anjali Deshpande",
            customerPhoneMasked = "+91 98211 •••07",
            amountInr = 950,
            location = "Majiwada, Thane",
            dateLabel = "11 days ago · 5:05 PM",
            timestampOrder = 4205L,
            status = WorkMarkStatus.VERIFIED,
            rating = 4,
            customerFeedback = "Good workmanship, fixed all ceiling lights neatly.",
            afterPhoto = EvidenceAttachment("Finished work", sampleResId = R.drawable.img_work_after),
            verifiedViaOtp = true,
            verificationMethod = "QR Scan + OTP",
            isRepeatCustomer = true
        ),
        WorkMark(
            id = "CHH-2026-THN-4204",
            title = "Inverter backup line wiring",
            trade = Trade.ELECTRICIAN,
            description = "Connected Luminous 900VA inverter backup line to hall fan, Wi-Fi router point, and study lights.",
            customerName = "Prashant Sawant",
            customerPhoneMasked = "+91 99203 •••62",
            amountInr = 2200,
            location = "Kolshet Road, Thane",
            dateLabel = "2 weeks ago · 4:30 PM",
            timestampOrder = 4204L,
            status = WorkMarkStatus.VERIFIED,
            rating = 5,
            customerFeedback = "Tested power cut switchover twice. Very reliable electrician.",
            beforePhoto = EvidenceAttachment("Before work", sampleResId = R.drawable.img_work_before),
            afterPhoto = EvidenceAttachment("After work", sampleResId = R.drawable.img_work_after),
            verifiedViaOtp = true,
            verificationMethod = "QR + Voice Feedback",
            isRepeatCustomer = false
        ),
        WorkMark(
            id = "CHH-2026-THN-4203",
            title = "Kitchen chimney & geyser power socket",
            trade = Trade.ELECTRICIAN,
            description = "Installed dedicated 16A heavy-duty modular socket with earthing for instant geyser and chimney.",
            customerName = "Kavita Shetty",
            customerPhoneMasked = "+91 98331 •••54",
            amountInr = 1350,
            location = "Naupada, Thane",
            dateLabel = "3 weeks ago · 1:15 PM",
            timestampOrder = 4203L,
            status = WorkMarkStatus.PENDING,
            beforePhoto = EvidenceAttachment("Before work", sampleResId = R.drawable.img_work_before),
            afterPhoto = EvidenceAttachment("After work", sampleResId = R.drawable.img_work_after),
            verifiedViaOtp = false,
            verificationMethod = "Awaiting Customer",
            isRepeatCustomer = false
        )
    )

    fun createRameshVouches(): List<UstaadVouch> = listOf(
        UstaadVouch(
            id = "vouch-1",
            ustaadName = "Mahesh Patil",
            ustaadTitle = "Master Electrician",
            yearsExperience = 22,
            city = "Thane",
            relationship = "Former Site Supervisor & Mentor (2019–2023)",
            endorsedSkills = listOf("Residential wiring", "Electrical safety", "Fault diagnosis"),
            note = "Ramesh handled 15+ residential rewiring sites under my supervision. Follows strict ISI color coding and earthing discipline.",
            dateLabel = "Verified Mentor · Jan 2026"
        ),
        UstaadVouch(
            id = "vouch-2",
            ustaadName = "Prakash Deshmukh",
            ustaadTitle = "Senior Electrical Contractor",
            yearsExperience = 18,
            city = "Mumbai & Thane",
            relationship = "Commercial Fit-out Lead (2023–2025)",
            endorsedSkills = listOf("MCB & Switchboard", "Load balancing", "Lighting Systems"),
            note = "Dependable on apartment handover deadlines. Zero safety incidents across 40+ flat switchboard installations.",
            dateLabel = "Verified Mentor · Nov 2025"
        ),
        UstaadVouch(
            id = "vouch-3",
            ustaadName = "Harishankar Verma",
            ustaadTitle = "Master Technician (ITI Certified)",
            yearsExperience = 25,
            city = "Navi Mumbai",
            relationship = "Trade Guild Senior",
            endorsedSkills = listOf("Fan Installation", "Inverter & Backup", "Safety Testing"),
            note = "Strong practical fundamentals and always keeps neat job documentation for homeowners.",
            dateLabel = "Verified Mentor · Aug 2025"
        )
    )

    fun createRameshProfile(): WorkerProfile = WorkerProfile(
        id = "worker-ramesh",
        fullName = "Ramesh Kumar",
        shortName = "Ramesh K.",
        phoneNumber = "+91 98204 71832",
        primaryTrade = Trade.ELECTRICIAN,
        experienceYears = 8,
        city = "Thane",
        state = "India",
        skillLevel = 3,
        trustScore = 86,
        totalVerifiedWorkMarks = 42,
        ustaadVouchCount = 9,
        repeatCustomers = 14,
        averageRating = 4.9,
        publicProfileUrl = "chhaap.in/w/ramesh-k-thn",
        skillBadges = listOf(
            "Residential Wiring",
            "Fan Installation",
            "MCB & Switchboard",
            "Lighting Systems"
        ),
        avatarResId = R.drawable.img_worker_ramesh,
        workMarks = createRameshWorkMarks(),
        vouches = createRameshVouches()
    )

    fun createDirectoryWorkers(ramesh: WorkerProfile): List<WorkerProfile> = listOf(
        ramesh,
        WorkerProfile(
            id = "worker-suresh",
            fullName = "Suresh Yadav",
            shortName = "Suresh Y.",
            phoneNumber = "+91 98501 44219",
            primaryTrade = Trade.PLUMBER,
            experienceYears = 10,
            city = "Pune",
            state = "India",
            skillLevel = 4,
            trustScore = 91,
            totalVerifiedWorkMarks = 58,
            ustaadVouchCount = 12,
            repeatCustomers = 21,
            averageRating = 4.9,
            publicProfileUrl = "chhaap.in/w/suresh-y-pnq",
            skillBadges = listOf("CPVC Concealed Piping", "Bathroom Fittings", "Leak Detection", "Overhead Tank Setup"),
            workMarks = listOf(
                WorkMark(
                    id = "CHH-2026-PNQ-5802",
                    title = "Complete bathroom CPVC pipeline fitting",
                    trade = Trade.PLUMBER,
                    description = "Replaced rusted GI pipes with Ashirvad CPVC concealed lines and pressure-tested all joints.",
                    customerName = "Abhijit Gokhale",
                    customerPhoneMasked = "+91 98220 •••15",
                    amountInr = 6500,
                    location = "Kothrud, Pune",
                    dateLabel = "3 days ago",
                    timestampOrder = 5802L,
                    status = WorkMarkStatus.VERIFIED,
                    rating = 5,
                    customerFeedback = "Zero leakage after pressure test. Very experienced plumber.",
                    verifiedViaOtp = true,
                    isRepeatCustomer = true
                ),
                WorkMark(
                    id = "CHH-2026-PNQ-5801",
                    title = "Solar water heater line & diverter",
                    trade = Trade.PLUMBER,
                    description = "Installed Jaquar wall mixer diverter and insulated hot-water supply line.",
                    customerName = "Neha Kulkarni",
                    customerPhoneMasked = "+91 97654 •••88",
                    amountInr = 2400,
                    location = "Baner, Pune",
                    dateLabel = "1 week ago",
                    timestampOrder = 5801L,
                    status = WorkMarkStatus.VERIFIED,
                    rating = 5,
                    customerFeedback = "Clean tile cutting and neat finish.",
                    verifiedViaOtp = true
                )
            ),
            vouches = listOf(
                UstaadVouch(
                    id = "v-suresh-1",
                    ustaadName = "Babanrao Shinde",
                    ustaadTitle = "Master Plumber",
                    yearsExperience = 26,
                    city = "Pune",
                    relationship = "Guild Mentor",
                    endorsedSkills = listOf("CPVC Concealed Piping", "Pressure Testing"),
                    note = "Precision layout and zero callback rate.",
                    dateLabel = "Verified Mentor · Dec 2025"
                )
            )
        ),
        WorkerProfile(
            id = "worker-imran",
            fullName = "Imran Shaikh",
            shortName = "Imran S.",
            phoneNumber = "+91 98217 30912",
            primaryTrade = Trade.CARPENTER,
            experienceYears = 7,
            city = "Mumbai",
            state = "India",
            skillLevel = 3,
            trustScore = 84,
            totalVerifiedWorkMarks = 36,
            ustaadVouchCount = 7,
            repeatCustomers = 11,
            averageRating = 4.8,
            publicProfileUrl = "chhaap.in/w/imran-s-bom",
            skillBadges = listOf("Modular Kitchens", "Wardrobe Hinges", "Main Door Polish", "Plywood Framing"),
            workMarks = listOf(
                WorkMark(
                    id = "CHH-2026-BOM-3604",
                    title = "Modular kitchen soft-close cabinet installation",
                    trade = Trade.CARPENTER,
                    description = "Fabricated and fitted BWP marine ply kitchen cabinets with Hettich soft-close channels.",
                    customerName = "Farida Merchant",
                    customerPhoneMasked = "+91 98201 •••74",
                    amountInr = 9500,
                    location = "Andheri West, Mumbai",
                    dateLabel = "4 days ago",
                    timestampOrder = 3604L,
                    status = WorkMarkStatus.VERIFIED,
                    rating = 5,
                    customerFeedback = "Exact millimeter alignment on drawers. Highly recommended.",
                    verifiedViaOtp = true,
                    isRepeatCustomer = true
                )
            ),
            vouches = listOf(
                UstaadVouch(
                    id = "v-imran-1",
                    ustaadName = "Salim Mistry",
                    ustaadTitle = "Master Furniture Maker",
                    yearsExperience = 24,
                    city = "Mumbai",
                    relationship = "Workshop Ustaad",
                    endorsedSkills = listOf("Modular Kitchens", "Laminate Finishing"),
                    note = "Clean joinery and punctual delivery.",
                    dateLabel = "Verified Mentor · Oct 2025"
                )
            )
        ),
        WorkerProfile(
            id = "worker-deepak",
            fullName = "Deepak Pawar",
            shortName = "Deepak P.",
            phoneNumber = "+91 99672 51804",
            primaryTrade = Trade.MECHANIC,
            experienceYears = 6,
            city = "Navi Mumbai",
            state = "India",
            skillLevel = 3,
            trustScore = 82,
            totalVerifiedWorkMarks = 31,
            ustaadVouchCount = 6,
            repeatCustomers = 13,
            averageRating = 4.8,
            publicProfileUrl = "chhaap.in/w/deepak-p-nvm",
            skillBadges = listOf("Two-Wheeler Engine", "EV Diagnostics", "Brake & Clutch", "AC Compressor"),
            workMarks = listOf(
                WorkMark(
                    id = "CHH-2026-NVM-3109",
                    title = "Scooter engine overhaul & clutch assembly",
                    trade = Trade.MECHANIC,
                    description = "Completed full piston ring replacement, variator belt service, and carburettor tuning.",
                    customerName = "Siddharth Rane",
                    customerPhoneMasked = "+91 98198 •••20",
                    amountInr = 2850,
                    location = "Vashi Sector 17, Navi Mumbai",
                    dateLabel = "2 days ago",
                    timestampOrder = 3109L,
                    status = WorkMarkStatus.VERIFIED,
                    rating = 5,
                    customerFeedback = "Smooth pickup restored and returned old parts transparently.",
                    verifiedViaOtp = true,
                    isRepeatCustomer = true
                )
            ),
            vouches = emptyList()
        ),
        WorkerProfile(
            id = "worker-santosh",
            fullName = "Santosh Mane",
            shortName = "Santosh M.",
            phoneNumber = "+91 98690 66211",
            primaryTrade = Trade.PAINTER,
            experienceYears = 9,
            city = "Thane",
            state = "India",
            skillLevel = 2,
            trustScore = 79,
            totalVerifiedWorkMarks = 24,
            ustaadVouchCount = 5,
            repeatCustomers = 8,
            averageRating = 4.7,
            publicProfileUrl = "chhaap.in/w/santosh-m-thn",
            skillBadges = listOf("Interior Emulsion", "Waterproofing", "Wall Putty", "Texture Accent"),
            workMarks = listOf(
                WorkMark(
                    id = "CHH-2026-THN-2401",
                    title = "2BHK Royale interior repainting & damp-proof coat",
                    trade = Trade.PAINTER,
                    description = "Applied Dr. Fixit damp-proof base, 2 coats Birla putty, and Asian Paints Royale finish.",
                    customerName = "Vivek Phadke",
                    customerPhoneMasked = "+91 98207 •••91",
                    amountInr = 14500,
                    location = "Panchpakhadi, Thane",
                    dateLabel = "6 days ago",
                    timestampOrder = 2401L,
                    status = WorkMarkStatus.VERIFIED,
                    rating = 5,
                    customerFeedback = "Masked all furniture carefully and left spotless floors.",
                    verifiedViaOtp = true
                )
            ),
            vouches = emptyList()
        )
    )

    fun buildTrustFactors(worker: WorkerProfile): List<TrustFactor> {
        val verifiedScore = (82 + (worker.totalVerifiedWorkMarks - 42).coerceAtLeast(0) * 3).coerceAtMost(98)
        val customerScore = (88 + (worker.totalVerifiedWorkMarks - 42).coerceAtLeast(0) * 2).coerceAtMost(99)
        val repeatScore = (84 + (worker.repeatCustomers - 14).coerceAtLeast(0) * 2).coerceAtMost(97)
        val vouchScore = (85 + (worker.ustaadVouchCount - 9).coerceAtLeast(0) * 3).coerceAtMost(98)
        val evidenceScore = (90 + (worker.totalVerifiedWorkMarks - 42).coerceAtLeast(0) * 2).coerceAtMost(99)
        return listOf(
            TrustFactor(
                titleEn = "Verified Work",
                titleHi = "सत्यापित काम",
                weightPercent = 35,
                achievedPercent = verifiedScore,
                detail = "${worker.totalVerifiedWorkMarks} OTP/QR-verified jobs logged over ${worker.experienceYears} yrs"
            ),
            TrustFactor(
                titleEn = "Customer Trust",
                titleHi = "ग्राहक विश्वास",
                weightPercent = 25,
                achievedPercent = customerScore,
                detail = "${worker.averageRating}★ avg across unique verified homeowners"
            ),
            TrustFactor(
                titleEn = "Repeat Customers",
                titleHi = "दोबारा बुलाने वाले ग्राहक",
                weightPercent = 20,
                achievedPercent = repeatScore,
                detail = "${worker.repeatCustomers} customers hired ${worker.shortName} more than once"
            ),
            TrustFactor(
                titleEn = "Ustaad Vouches",
                titleHi = "उस्ताद समर्थन",
                weightPercent = 10,
                achievedPercent = vouchScore,
                detail = "${worker.ustaadVouchCount} endorsements from senior trade masters"
            ),
            TrustFactor(
                titleEn = "Evidence Quality",
                titleHi = "फ़ोटो और सबूत गुणवत्ता",
                weightPercent = 10,
                achievedPercent = evidenceScore,
                detail = "Before/after photos & voice notes attached to 92% of jobs"
            )
        )
    }

    val antiFraudSignals: List<AntiFraudSignal> = listOf(
        AntiFraudSignal(
            titleEn = "Duplicate job detection",
            titleHi = "डुप्लिकेट काम जांच",
            description = "Checks photo hashes, timestamps, and overlapping job entries.",
            statusLabel = "Clear"
        ),
        AntiFraudSignal(
            titleEn = "Suspicious customer relationships",
            titleHi = "संदिग्ध ग्राहक पहचान",
            description = "Flags self-verifications or recycled customer phone numbers.",
            statusLabel = "No flags"
        ),
        AntiFraudSignal(
            titleEn = "GPS / time consistency",
            titleHi = "लोकेशन और समय मिलान",
            description = "Matches job city/locality with verification timestamp window.",
            statusLabel = "Consistent"
        ),
        AntiFraudSignal(
            titleEn = "Trust-weighted endorsements",
            titleHi = "भरोसेमंद उस्ताद वेटेज",
            description = "Vouches from high-trust senior Ustaads carry higher weight and never replace customer proof.",
            statusLabel = "Verified"
        ),
        AntiFraudSignal(
            titleEn = "Unusual verification patterns",
            titleHi = "असामान्य सत्यापन पैटर्न",
            description = "Detects sudden bulk bursts of verifications in short intervals.",
            statusLabel = "Normal pace"
        )
    )
}
