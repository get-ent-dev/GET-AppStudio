package com.example.data

import com.example.model.*

object SampleData {
    val templates = listOf(
        AppTemplate(
            id = "tmpl_aichat",
            name = "AI Chat Studio",
            category = "AI Chat",
            description = "Intelligent conversational assistant powered by LLM models with prompt shortcuts, voice input, and markdown answers.",
            accentColorHex = "#7C3AED",
            badge = "POPULAR",
            features = listOf("Streaming LLM Responses", "Prompt Library", "Code Syntax Highlighting", "Markdown Rendering"),
            screensCount = 5
        ),
        AppTemplate(
            id = "tmpl_movies",
            name = "CineStream Movie Hub",
            category = "Movies",
            description = "Stunning cinema & streaming discovery app with movie trailers, ratings, personalized watchlists and cast details.",
            accentColorHex = "#E11D48",
            badge = "TRENDING",
            features = listOf("High-Res Posters", "Trailers Carousel", "Watchlist Cloud Sync", "Smart Filtering"),
            screensCount = 6
        ),
        AppTemplate(
            id = "tmpl_business",
            name = "Enterprise Business Hub",
            category = "Business",
            description = "Professional corporate profile app for local companies, agencies, service providers and consultant booking.",
            accentColorHex = "#0284C7",
            badge = "RECOMMENDED",
            features = listOf("Service Catalog", "Booking Calendar", "Interactive Map", "WhatsApp/Call Quick Action"),
            screensCount = 5
        ),
        AppTemplate(
            id = "tmpl_store",
            name = "QuickCommerce Store",
            category = "Business",
            description = "Full-featured shopping store with product grids, flash sales, shopping cart, discounts and checkout.",
            accentColorHex = "#10B981",
            badge = "HOT",
            features = listOf("Cart & Wishlist", "Stripe / GPay Checkout", "Order Status Tracking", "Coupon Engine"),
            screensCount = 7
        ),
        AppTemplate(
            id = "tmpl_fitness",
            name = "FitPulse Tracker",
            category = "Health",
            description = "Daily workout companion with calorie tracking, step counter, progress charts and interval timers.",
            accentColorHex = "#F59E0B",
            badge = null,
            features = listOf("Activity Heatmap", "Custom Workout Builder", "Stopwatch & Timer", "Daily Water Logger"),
            screensCount = 6
        ),
        AppTemplate(
            id = "tmpl_creator",
            name = "Creator Portfolio",
            category = "Portfolio",
            description = "Sleek personal brand and photography showcase with masonry gallery, video feed, and commission inquiries.",
            accentColorHex = "#8B5CF6",
            badge = null,
            features = listOf("Fullscreen Photo Viewer", "Social Media Hub", "Contact Form", "Merch Links"),
            screensCount = 4
        )
    )

    val initialProjects = listOf(
        AppProject(
            id = "proj_fitpulse",
            name = "FitPulse Pro",
            packageName = "com.fitpulse.app",
            version = "1.2.0",
            templateId = "tmpl_fitness",
            templateName = "FitPulse Tracker",
            primaryColorHex = "#F59E0B",
            status = ProjectStatus.READY,
            buildProgress = 1.0f,
            updatedAt = "2 hours ago",
            features = listOf("Workout Plans", "Water Tracker", "Analytics"),
            adMobEnabled = true,
            inAppPurchases = true,
            pushNotifications = true,
            totalUsers = 3280,
            totalBuilds = 18
        ),
        AppProject(
            id = "proj_aichat",
            name = "AI ChatMaster",
            packageName = "com.aichatmaster.app",
            version = "1.0.4",
            templateId = "tmpl_aichat",
            templateName = "AI Chat Studio",
            primaryColorHex = "#7C3AED",
            status = ProjectStatus.READY,
            buildProgress = 1.0f,
            updatedAt = "Yesterday",
            features = listOf("Voice Input", "Code Runner", "Custom Personas"),
            adMobEnabled = false,
            inAppPurchases = true,
            pushNotifications = true,
            totalUsers = 8450,
            totalBuilds = 24
        ),
        AppProject(
            id = "proj_cinestream",
            name = "CineStream Mobile",
            packageName = "com.cinestream.guide",
            version = "2.0.1",
            templateId = "tmpl_movies",
            templateName = "CineStream Movie Hub",
            primaryColorHex = "#E11D48",
            status = ProjectStatus.PUBLISHED,
            buildProgress = 1.0f,
            updatedAt = "3 days ago",
            features = listOf("Trailers 4K", "Offline Watchlist", "Critique Reviews"),
            adMobEnabled = true,
            inAppPurchases = false,
            pushNotifications = true,
            totalUsers = 14200,
            totalBuilds = 31
        ),
        AppProject(
            id = "proj_nova",
            name = "Nova Commerce",
            packageName = "com.nova.boutique",
            version = "1.0.0",
            templateId = "tmpl_store",
            templateName = "QuickCommerce Store",
            primaryColorHex = "#10B981",
            status = ProjectStatus.DRAFT,
            buildProgress = 0.0f,
            updatedAt = "5 days ago",
            features = listOf("Product Catalog", "Cart System"),
            adMobEnabled = false,
            inAppPurchases = false,
            pushNotifications = true,
            totalUsers = 0,
            totalBuilds = 2
        )
    )

    val adminUsers = listOf(
        AdminUser("u_101", "sarah.connor@domain.io", "Admin", "Active", "2026-01-14"),
        AdminUser("u_102", "dev.marcus@gmail.com", "Member", "Active", "2026-02-01"),
        AdminUser("u_103", "elena.design@studio.dev", "Editor", "Active", "2026-03-10"),
        AdminUser("u_104", "alex.builder@cloud.com", "Tester", "Pending", "2026-04-05")
    )

    val adminAnnouncements = listOf(
        AdminAnnouncement("a_01", "Spring Update Live!", "New movie trailer components and fast checkout released.", "10 mins ago", 4210),
        AdminAnnouncement("a_02", "Scheduled Maintenance", "VM build server will be updated with latest Android SDK 36 tools.", "2 days ago", 6100)
    )
}
