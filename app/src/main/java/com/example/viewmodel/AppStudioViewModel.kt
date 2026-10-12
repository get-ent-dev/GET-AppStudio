package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ScreenState {
    ONBOARDING,
    NOTIFICATIONS,
    AUTH,
    MAIN_WORKSPACE,
    ADMIN_PANEL,
    BUILD_CONSOLE
}

enum class WorkspaceTab(val title: String) {
    PROJECTS("My Apps"),
    TEMPLATES("Templates"),
    VM_RUNNER("VM & H100"),
    BUILDER("App Studio"),
    PUBLISH("Google Play")
}

data class AppStudioUiState(
    val currentScreen: ScreenState = ScreenState.ONBOARDING,
    val onboardingStep: Int = 0,
    val currentTab: WorkspaceTab = WorkspaceTab.PROJECTS,
    val isLoggedIn: Boolean = false,
    val isSignUpMode: Boolean = false,
    val authEmail: String = "gadgelectronit2@gmail.com",
    val authPass: String = "••••••••••••",
    val authName: String = "Gadg Electron",
    val isPasswordVisible: Boolean = false,
    val notificationsAllowed: Boolean = false,
    val userProfile: UserProfile = UserProfile(
        name = "Gadg Electron",
        email = "gadgelectronit2@gmail.com",
        isPremium = true,
        projectsCount = 4,
        cloudSyncActive = true
    ),
    val projects: List<AppProject> = SampleData.initialProjects,
    val selectedProjectId: String = "proj_fitpulse",
    val templates: List<AppTemplate> = SampleData.templates,
    val templateCategoryFilter: String = "All",
    val searchQuery: String = "",
    val vmConfig: VmRunnerConfig = VmRunnerConfig(),
    val isBuilding: Boolean = false,
    val buildProgress: Float = 0f,
    val currentBuildStage: String = "Idle",
    val buildLogs: List<BuildLogEntry> = emptyList(),
    val lastBuiltApkName: String = "app-release.apk",
    // Live Preview State
    val previewAppName: String = "FitPulse Pro",
    val previewPrimaryColorHex: String = "#0084FF",
    val previewDarkMode: Boolean = false,
    val previewScreenIndex: Int = 0,
    val previewCounter: Int = 5,
    // Admin Panel State
    val adminUsers: List<AdminUser> = SampleData.adminUsers,
    val adminAnnouncements: List<AdminAnnouncement> = SampleData.adminAnnouncements,
    val statusSnackbarMessage: String? = null
)

class AppStudioViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppStudioUiState())
    val uiState: StateFlow<AppStudioUiState> = _uiState.asStateFlow()

    private var buildJob: Job? = null

    init {
        // Initial build logs
        addLog(LogLevel.INFO, "GET AppStudio system initialized.")
        addLog(LogLevel.GPU, "NVIDIA H100 80GB VRAM PCIe detected. CUDA 12.4 driver ready.")
        addLog(LogLevel.INFO, "Host VM specs: 64 vCPUs, 256GB RAM, 4TB Data NVMe storage connected.")
    }

    // --- Navigation Actions ---
    fun nextOnboardingStep() {
        val current = _uiState.value.onboardingStep
        if (current < 3) {
            _uiState.update { it.copy(onboardingStep = current + 1) }
        } else {
            // Reached last step ("Start Building") -> go to Notifications screen
            _uiState.update { it.copy(currentScreen = ScreenState.NOTIFICATIONS) }
        }
    }

    fun skipOnboarding() {
        _uiState.update { it.copy(currentScreen = ScreenState.AUTH) }
    }

    fun proceedFromNotifications(allowed: Boolean) {
        _uiState.update {
            it.copy(
                notificationsAllowed = allowed,
                currentScreen = ScreenState.AUTH
            )
        }
    }

    fun setAuthMode(isSignUp: Boolean) {
        _uiState.update { it.copy(isSignUpMode = isSignUp) }
    }

    fun updateAuthEmail(email: String) {
        _uiState.update { it.copy(authEmail = email) }
    }

    fun updateAuthPassword(pass: String) {
        _uiState.update { it.copy(authPass = pass) }
    }

    fun updateAuthName(name: String) {
        _uiState.update { it.copy(authName = name) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun signIn() {
        val email = _uiState.value.authEmail.ifBlank { "developer@getappstudio.io" }
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                currentScreen = ScreenState.MAIN_WORKSPACE,
                userProfile = it.userProfile.copy(email = email),
                statusSnackbarMessage = "Signed in as $email"
            )
        }
    }

    fun signInWithGoogle() {
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                currentScreen = ScreenState.MAIN_WORKSPACE,
                statusSnackbarMessage = "Connected via Google Account (${it.userProfile.email})"
            )
        }
    }

    fun signOut() {
        _uiState.update {
            it.copy(
                isLoggedIn = false,
                currentScreen = ScreenState.AUTH,
                statusSnackbarMessage = "Signed out"
            )
        }
    }

    fun switchTab(tab: WorkspaceTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun selectProject(projectId: String) {
        val proj = _uiState.value.projects.find { it.id == projectId } ?: return
        _uiState.update {
            it.copy(
                selectedProjectId = projectId,
                previewAppName = proj.name,
                previewPrimaryColorHex = proj.primaryColorHex
            )
        }
    }

    fun openAdminPanel(projectId: String) {
        selectProject(projectId)
        _uiState.update { it.copy(currentScreen = ScreenState.ADMIN_PANEL) }
    }

    fun openBuildConsole() {
        _uiState.update { it.copy(currentScreen = ScreenState.BUILD_CONSOLE) }
    }

    fun navigateBackToWorkspace() {
        _uiState.update { it.copy(currentScreen = ScreenState.MAIN_WORKSPACE) }
    }

    // --- Project Operations ---
    fun createProjectFromTemplate(template: AppTemplate, customName: String? = null) {
        val name = customName ?: "${template.name} Pro"
        val cleanPkg = name.lowercase().replace(" ", "").replace("-", "")
        val newProj = AppProject(
            id = "proj_${System.currentTimeMillis()}",
            name = name,
            packageName = "com.appstudio.$cleanPkg",
            version = "1.0.0",
            templateId = template.id,
            templateName = template.name,
            primaryColorHex = template.accentColorHex,
            status = ProjectStatus.READY,
            buildProgress = 1.0f,
            updatedAt = "Just now",
            features = template.features,
            adMobEnabled = true,
            inAppPurchases = true,
            pushNotifications = true
        )
        _uiState.update {
            it.copy(
                projects = listOf(newProj) + it.projects,
                selectedProjectId = newProj.id,
                previewAppName = newProj.name,
                previewPrimaryColorHex = newProj.primaryColorHex,
                currentTab = WorkspaceTab.BUILDER,
                statusSnackbarMessage = "Created \"${newProj.name}\" from template!"
            )
        }
    }

    fun updateProjectDetails(
        projectId: String,
        name: String,
        packageName: String,
        primaryColorHex: String,
        adMob: Boolean,
        iap: Boolean,
        push: Boolean
    ) {
        _uiState.update { state ->
            val updated = state.projects.map { p ->
                if (p.id == projectId) {
                    p.copy(
                        name = name,
                        packageName = packageName,
                        primaryColorHex = primaryColorHex,
                        adMobEnabled = adMob,
                        inAppPurchases = iap,
                        pushNotifications = push,
                        updatedAt = "Just now"
                    )
                } else p
            }
            state.copy(
                projects = updated,
                previewAppName = name,
                previewPrimaryColorHex = primaryColorHex,
                statusSnackbarMessage = "Saved project changes"
            )
        }
    }

    fun duplicateProject(projectId: String) {
        val orig = _uiState.value.projects.find { it.id == projectId } ?: return
        val copy = orig.copy(
            id = "proj_${System.currentTimeMillis()}",
            name = "${orig.name} (Copy)",
            updatedAt = "Just now"
        )
        _uiState.update {
            it.copy(
                projects = listOf(copy) + it.projects,
                statusSnackbarMessage = "Duplicated project as \"${copy.name}\""
            )
        }
    }

    fun deleteProject(projectId: String) {
        _uiState.update { state ->
            val remaining = state.projects.filter { it.id != projectId }
            val nextSelected = remaining.firstOrNull()?.id ?: ""
            state.copy(
                projects = remaining,
                selectedProjectId = nextSelected,
                statusSnackbarMessage = "Project deleted"
            )
        }
    }

    // --- VM Runner & H100 GPU Build Engine ---
    fun updateVmConfig(
        host: String,
        port: Int,
        gpuAccelerated: Boolean,
        ramDisk: Boolean,
        tensorRt: Boolean
    ) {
        _uiState.update {
            it.copy(
                vmConfig = it.vmConfig.copy(
                    host = host,
                    port = port,
                    isGpuAccelerated = gpuAccelerated,
                    useRamDiskGradleCache = ramDisk,
                    tensorRtCodeGen = tensorRt
                ),
                statusSnackbarMessage = "VM configuration updated"
            )
        }
    }

    fun triggerBuild(projectId: String) {
        val proj = _uiState.value.projects.find { it.id == projectId } ?: return
        if (_uiState.value.isBuilding) return

        buildJob?.cancel()
        buildJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isBuilding = true,
                    buildProgress = 0.05f,
                    currentBuildStage = "Connecting to VM Runner...",
                    lastBuiltApkName = "${proj.name.lowercase().replace(" ", "_")}-release.apk"
                )
            }
            addLog(LogLevel.INFO, ">>> Build job triggered for [${proj.name}] (${proj.packageName})")
            addLog(LogLevel.INFO, "Target VM: ${_uiState.value.vmConfig.host}:${_uiState.value.vmConfig.port} (64 vCPU, 256GB RAM)")
            delay(500)

            _uiState.update { it.copy(buildProgress = 0.18f, currentBuildStage = "Allocating 32GB RAM-Disk Gradle Daemon...") }
            addLog(LogLevel.BUILD, ":app:preBuild UP-TO-DATE")
            addLog(LogLevel.BUILD, ":app:generateReleaseBuildConfig")
            delay(600)

            _uiState.update { it.copy(buildProgress = 0.38f, currentBuildStage = "NVIDIA H100 TensorRT Code Synthesis...") }
            addLog(LogLevel.GPU, "[H100 80GB VRAM] Dispatching 16,896 CUDA cores for Jetpack Compose AST layout pass...")
            addLog(LogLevel.GPU, "[H100 TensorRT] Generated zero-overhead Compose bytecode graphs.")
            delay(700)

            _uiState.update { it.copy(buildProgress = 0.58f, currentBuildStage = "Compiling Kotlin & KSP with 64 parallel threads...") }
            addLog(LogLevel.BUILD, ":app:kspReleaseKotlin (64 worker threads active)")
            addLog(LogLevel.BUILD, ":app:compileReleaseKotlin [Compose Compiler 2.2.10 - 100% successful]")
            delay(700)

            _uiState.update { it.copy(buildProgress = 0.78f, currentBuildStage = "AAPT2 resource linking & R8 ProGuard minify...") }
            addLog(LogLevel.BUILD, ":app:processReleaseResources (AAPT2 optimized)")
            addLog(LogLevel.BUILD, ":app:minifyReleaseWithR8 (dead code removed: 4.8MB saved)")
            delay(600)

            _uiState.update { it.copy(buildProgress = 0.92f, currentBuildStage = "Signing APK & AAB with Google Play v3 schema...") }
            addLog(LogLevel.BUILD, ":app:packageReleaseBundle [release.aab]")
            addLog(LogLevel.BUILD, ":app:packageReleaseApk [${_uiState.value.lastBuiltApkName}]")
            delay(500)

            _uiState.update {
                val updatedProjects = it.projects.map { p ->
                    if (p.id == projectId) p.copy(
                        status = ProjectStatus.READY,
                        buildProgress = 1.0f,
                        totalBuilds = p.totalBuilds + 1,
                        updatedAt = "Just now"
                    ) else p
                }
                it.copy(
                    isBuilding = false,
                    buildProgress = 1.0f,
                    currentBuildStage = "Build Successful!",
                    projects = updatedProjects,
                    statusSnackbarMessage = "Build Succeeded on H100 GPU! (Build time: 3.6s)"
                )
            }
            addLog(LogLevel.SUCCESS, "BUILD SUCCESSFUL in 3.6s! Generated ${_uiState.value.lastBuiltApkName}")
            addLog(LogLevel.SUCCESS, "AAB ready for Google Play Store upload.")
        }
    }

    private fun addLog(level: LogLevel, message: String) {
        val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
        val entry = BuildLogEntry(
            timestamp = sdf.format(Date()),
            level = level,
            message = message
        )
        _uiState.update { it.copy(buildLogs = it.buildLogs + entry) }
    }

    fun clearBuildLogs() {
        _uiState.update { it.copy(buildLogs = emptyList()) }
        addLog(LogLevel.INFO, "Build console cleared.")
    }

    // --- Live Preview Interactions ---
    fun setPreviewDarkMode(dark: Boolean) {
        _uiState.update { it.copy(previewDarkMode = dark) }
    }

    fun setPreviewScreenIndex(idx: Int) {
        _uiState.update { it.copy(previewScreenIndex = idx) }
    }

    fun incrementPreviewCounter() {
        _uiState.update { it.copy(previewCounter = it.previewCounter + 1) }
    }

    // --- Admin Panel Actions ---
    fun sendAdminAnnouncement(title: String, content: String) {
        if (title.isBlank() || content.isBlank()) return
        val newAnnounce = AdminAnnouncement(
            id = "a_${System.currentTimeMillis()}",
            title = title,
            content = content,
            sentTime = "Just now",
            deliveredCount = 1420
        )
        _uiState.update {
            it.copy(
                adminAnnouncements = listOf(newAnnounce) + it.adminAnnouncements,
                statusSnackbarMessage = "Broadcast sent to 1,420 users!"
            )
        }
    }

    fun dismissSnackbar() {
        _uiState.update { it.copy(statusSnackbarMessage = null) }
    }

    fun setTemplateCategoryFilter(category: String) {
        _uiState.update { it.copy(templateCategoryFilter = category) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}
