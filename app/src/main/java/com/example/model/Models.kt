package com.example.model

enum class ProjectStatus(val label: String) {
    DRAFT("Draft"),
    BUILDING("Building on H100 GPU"),
    READY("Ready to Test"),
    PUBLISHED("Live on Google Play")
}

data class AppProject(
    val id: String,
    val name: String,
    val packageName: String,
    val version: String = "1.0.0",
    val templateId: String,
    val templateName: String,
    val primaryColorHex: String = "#0084FF",
    val status: ProjectStatus = ProjectStatus.READY,
    val buildProgress: Float = 1.0f,
    val apkDownloadUrl: String = "https://appstudio.build/apks/release-unsigned.apk",
    val aabDownloadUrl: String = "https://appstudio.build/bundles/release.aab",
    val updatedAt: String = "Just now",
    val features: List<String> = listOf("Dark Mode", "Offline Cache", "Material You"),
    val adMobEnabled: Boolean = false,
    val inAppPurchases: Boolean = false,
    val pushNotifications: Boolean = true,
    val totalUsers: Int = 1420,
    val totalBuilds: Int = 12
)

data class AppTemplate(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val accentColorHex: String,
    val badge: String? = null,
    val features: List<String>,
    val screensCount: Int
)

data class VmRunnerConfig(
    val host: String = "10.0.0.128",
    val port: Int = 8080,
    val vcpuCount: Int = 64,
    val ramGb: Int = 256,
    val osStorageGb: Int = 500,
    val dataStorageTb: Int = 4,
    val gpuModel: String = "NVIDIA H100 PCIe 80GB VRAM Passthrough",
    val isConnected: Boolean = true,
    val isGpuAccelerated: Boolean = true,
    val useRamDiskGradleCache: Boolean = true,
    val parallelDexThreads: Int = 64,
    val tensorRtCodeGen: Boolean = true,
    val statusMessage: String = "Ready for ultra-fast builds"
)

enum class LogLevel {
    INFO, BUILD, GPU, SUCCESS, WARN
}

data class BuildLogEntry(
    val timestamp: String,
    val level: LogLevel,
    val message: String
)

data class UserProfile(
    val name: String,
    val email: String,
    val isPremium: Boolean = true,
    val projectsCount: Int = 4,
    val cloudSyncActive: Boolean = true
)

data class AdminUser(
    val id: String,
    val email: String,
    val role: String,
    val status: String,
    val joinedDate: String
)

data class AdminAnnouncement(
    val id: String,
    val title: String,
    val content: String,
    val sentTime: String,
    val deliveredCount: Int
)
