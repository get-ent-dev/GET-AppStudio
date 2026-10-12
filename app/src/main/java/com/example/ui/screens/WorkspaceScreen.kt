package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.AppStudioUiState
import com.example.viewmodel.AppStudioViewModel
import com.example.viewmodel.WorkspaceTab

@Composable
fun WorkspaceScreen(
    viewModel: AppStudioViewModel,
    uiState: AppStudioUiState,
    modifier: Modifier = Modifier
) {
    var showNewProjectDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            WorkspaceTopBar(
                userEmail = uiState.userProfile.email,
                vmConnected = uiState.vmConfig.isConnected,
                onSignOut = { viewModel.signOut() }
            )
        },
        bottomBar = {
            WorkspaceBottomNav(
                currentTab = uiState.currentTab,
                onTabSelected = { viewModel.switchTab(it) }
            )
        },
        floatingActionButton = {
            if (uiState.currentTab == WorkspaceTab.PROJECTS) {
                FloatingActionButton(
                    onClick = { showNewProjectDialog = true },
                    containerColor = AppStudioBlue,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("create_project_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create New App")
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                WorkspaceTab.PROJECTS -> ProjectsTabContent(
                    projects = uiState.projects,
                    selectedProjectId = uiState.selectedProjectId,
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    onSelectProject = {
                        viewModel.selectProject(it)
                        viewModel.switchTab(WorkspaceTab.BUILDER)
                    },
                    onTriggerBuild = { viewModel.triggerBuild(it) },
                    onOpenAdmin = { viewModel.openAdminPanel(it) },
                    onDuplicate = { viewModel.duplicateProject(it) },
                    onDelete = { viewModel.deleteProject(it) }
                )
                WorkspaceTab.TEMPLATES -> TemplatesTabContent(
                    templates = uiState.templates,
                    activeFilter = uiState.templateCategoryFilter,
                    onFilterChange = { viewModel.setTemplateCategoryFilter(it) },
                    onUseTemplate = { viewModel.createProjectFromTemplate(it) }
                )
                WorkspaceTab.VM_RUNNER -> VmRunnerTabContent(
                    config = uiState.vmConfig,
                    isBuilding = uiState.isBuilding,
                    buildProgress = uiState.buildProgress,
                    buildStage = uiState.currentBuildStage,
                    logs = uiState.buildLogs,
                    selectedProjectId = uiState.selectedProjectId,
                    projects = uiState.projects,
                    onTriggerBuild = { viewModel.triggerBuild(it) },
                    onClearLogs = { viewModel.clearBuildLogs() },
                    onUpdateConfig = { host, port, gpu, ram, tensor ->
                        viewModel.updateVmConfig(host, port, gpu, ram, tensor)
                    }
                )
                WorkspaceTab.BUILDER -> BuilderTabContent(
                    projects = uiState.projects,
                    selectedProjectId = uiState.selectedProjectId,
                    previewAppName = uiState.previewAppName,
                    previewColorHex = uiState.previewPrimaryColorHex,
                    previewDarkMode = uiState.previewDarkMode,
                    previewScreenIndex = uiState.previewScreenIndex,
                    previewCounter = uiState.previewCounter,
                    onSelectProject = { viewModel.selectProject(it) },
                    onUpdateDetails = { id, name, pkg, color, admob, iap, push ->
                        viewModel.updateProjectDetails(id, name, pkg, color, admob, iap, push)
                    },
                    onToggleDarkMode = { viewModel.setPreviewDarkMode(it) },
                    onSelectPreviewTab = { viewModel.setPreviewScreenIndex(it) },
                    onIncrementCounter = { viewModel.incrementPreviewCounter() },
                    onOpenAdmin = { viewModel.openAdminPanel(uiState.selectedProjectId) }
                )
                WorkspaceTab.PUBLISH -> PublishTabContent(
                    projects = uiState.projects,
                    selectedProjectId = uiState.selectedProjectId,
                    isPremium = uiState.userProfile.isPremium,
                    onSelectProject = { viewModel.selectProject(it) }
                )
            }
        }
    }

    if (showNewProjectDialog) {
        CreateProjectDialog(
            templates = uiState.templates,
            onDismiss = { showNewProjectDialog = false },
            onCreate = { template, name ->
                viewModel.createProjectFromTemplate(template, name)
                showNewProjectDialog = false
            }
        )
    }
}

// ----------------- Top Bar & Navigation -----------------

@Composable
private fun WorkspaceTopBar(
    userEmail: String,
    vmConnected: Boolean,
    onSignOut: () -> Unit
) {
    Surface(
        color = Color(0xFF0F172A),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // App Logo Badge
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Brush.linearGradient(listOf(AppStudioCyan, AppStudioBlue))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "GET AppStudio Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "GET AppStudio",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (vmConnected) Color(0xFF10B981) else Color(0xFFEF4444))
                        )
                        Text(
                            text = if (vmConnected) "H100 GPU VM Connected" else "VM Offline",
                            color = Color(0xFF38BDF8),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onSignOut,
                    modifier = Modifier.testTag("sign_out_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Sign Out",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkspaceBottomNav(
    currentTab: WorkspaceTab,
    onTabSelected: (WorkspaceTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("workspace_bottom_nav")
    ) {
        val tabs = listOf(
            Triple(WorkspaceTab.PROJECTS, Icons.Default.Apps, "My Apps"),
            Triple(WorkspaceTab.TEMPLATES, Icons.Default.Widgets, "Templates"),
            Triple(WorkspaceTab.VM_RUNNER, Icons.Default.Memory, "VM & H100"),
            Triple(WorkspaceTab.BUILDER, Icons.Default.PhoneAndroid, "Studio"),
            Triple(WorkspaceTab.PUBLISH, Icons.Default.RocketLaunch, "Publish")
        )

        tabs.forEach { (tab, icon, label) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppStudioBlue,
                    selectedTextColor = AppStudioBlue,
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B),
                    indicatorColor = Color(0xFFE0F2FE)
                )
            )
        }
    }
}

// ----------------- Tab 1: Projects -----------------

@Composable
private fun ProjectsTabContent(
    projects: List<AppProject>,
    selectedProjectId: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSelectProject: (String) -> Unit,
    onTriggerBuild: (String) -> Unit,
    onOpenAdmin: (String) -> Unit,
    onDuplicate: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    val filtered = projects.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
        it.templateName.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Banner Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF1E1B4B), Color(0xFF0F172A))
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "My Mobile Apps",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF38BDF8).copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${projects.size} Apps Active",
                                color = Color(0xFF38BDF8),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Build, customize, and deploy Android APK & AAB bundles with GPU-accelerated Gradle speed.",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Search Bar
        item {
            AppStudioInputField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Search projects or templates...",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                },
                testTag = "search_projects_input"
            )
        }

        // Projects List
        items(filtered, key = { it.id }) { proj ->
            ProjectCardItem(
                project = proj,
                isSelected = proj.id == selectedProjectId,
                onOpenStudio = { onSelectProject(proj.id) },
                onTriggerBuild = { onTriggerBuild(proj.id) },
                onOpenAdmin = { onOpenAdmin(proj.id) },
                onDuplicate = { onDuplicate(proj.id) },
                onDelete = { onDelete(proj.id) }
            )
        }
    }
}

@Composable
private fun ProjectCardItem(
    project: AppProject,
    isSelected: Boolean,
    onOpenStudio: () -> Unit,
    onTriggerBuild: () -> Unit,
    onOpenAdmin: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("project_card_${project.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Name & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(android.graphics.Color.parseColor(project.primaryColorHex))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = project.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = project.packageName,
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // Status Badge
                val statusBg = when (project.status) {
                    ProjectStatus.PUBLISHED -> Color(0xFFD1FAE5)
                    ProjectStatus.BUILDING -> Color(0xFFFEF3C7)
                    ProjectStatus.READY -> Color(0xFFE0F2FE)
                    ProjectStatus.DRAFT -> Color(0xFFF1F5F9)
                }
                val statusText = when (project.status) {
                    ProjectStatus.PUBLISHED -> Color(0xFF047857)
                    ProjectStatus.BUILDING -> Color(0xFFB45309)
                    ProjectStatus.READY -> Color(0xFF0369A1)
                    ProjectStatus.DRAFT -> Color(0xFF475569)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = project.status.label,
                        color = statusText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Metrics row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetricSmall(label = "Template", value = project.templateName)
                MetricSmall(label = "Version", value = "v${project.version}")
                MetricSmall(label = "Users", value = "${project.totalUsers}")
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onOpenStudio,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppStudioBlue),
                    modifier = Modifier.weight(1f).height(42.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Studio", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onOpenAdmin,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).height(42.dp)
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Admin", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = onTriggerBuild,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFEF3C7))
                        .size(42.dp)
                ) {
                    Icon(Icons.Default.FlashOn, contentDescription = "Compile", tint = Color(0xFFD97706))
                }

                IconButton(
                    onClick = onDuplicate,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF1F5F9))
                        .size(42.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Duplicate", tint = Color(0xFF475569))
                }
            }
        }
    }
}

@Composable
private fun MetricSmall(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 10.sp, color = Color(0xFF94A3B8))
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
    }
}

// ----------------- Tab 2: Templates -----------------

@Composable
private fun TemplatesTabContent(
    templates: List<AppTemplate>,
    activeFilter: String,
    onFilterChange: (String) -> Unit,
    onUseTemplate: (AppTemplate) -> Unit
) {
    val categories = listOf("All", "AI Chat", "Movies", "Business", "Health", "Portfolio")
    val filtered = if (activeFilter == "All") templates else templates.filter { it.category == activeFilter }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Start with Templates",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "Choose from professionally designed templates and launch in minutes. Fully customizable in your admin panel.",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 20.sp
                )
            }
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = cat == activeFilter
                    FilterChip(
                        selected = isSelected,
                        onClick = { onFilterChange(cat) },
                        label = { Text(cat, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppStudioBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Templates List
        items(filtered) { template ->
            TemplateCardItem(template = template, onUse = { onUseTemplate(template) })
        }
    }
}

@Composable
private fun TemplateCardItem(
    template: AppTemplate,
    onUse: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(android.graphics.Color.parseColor(template.accentColorHex))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (template.category) {
                                "AI Chat" -> Icons.Default.Chat
                                "Movies" -> Icons.Default.Movie
                                "Business" -> Icons.Default.Storefront
                                "Health" -> Icons.Default.FitnessCenter
                                else -> Icons.Default.Palette
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = template.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "${template.category} • ${template.screensCount} Pre-built Screens",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                if (template.badge != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFFEDD5))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = template.badge,
                            color = Color(0xFFEA580C),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = template.description,
                color = Color(0xFF334155),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Feature Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                template.features.take(3).forEach { feat ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = feat, fontSize = 11.sp, color = Color(0xFF475569))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            AppStudioPrimaryButton(
                text = "USE THIS TEMPLATE",
                onClick = onUse,
                modifier = Modifier.height(48.dp),
                backgroundColor = Color(android.graphics.Color.parseColor(template.accentColorHex)),
                testTag = "use_template_${template.id}"
            )
        }
    }
}

// ----------------- Tab 3: VM Runner & H100 GPU -----------------

@Composable
private fun VmRunnerTabContent(
    config: VmRunnerConfig,
    isBuilding: Boolean,
    buildProgress: Float,
    buildStage: String,
    logs: List<BuildLogEntry>,
    selectedProjectId: String,
    projects: List<AppProject>,
    onTriggerBuild: (String) -> Unit,
    onClearLogs: () -> Unit,
    onUpdateConfig: (String, Int, Boolean, Boolean, Boolean) -> Unit
) {
    var showConfigDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // High-Performance VM Hardware Spec Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF7C3AED)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Memory,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            Column {
                                Text(
                                    text = "Dedicated Build VM",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${config.host}:${config.port} • ONLINE",
                                    color = Color(0xFF10B981),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        IconButton(onClick = { showConfigDialog = true }) {
                            Icon(Icons.Default.Tune, contentDescription = "Configure", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4 Core Specs Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        VmSpecBadge(title = "CPU", value = "${config.vcpuCount} vCPUs", sub = "64 Threads")
                        VmSpecBadge(title = "RAM", value = "${config.ramGb} GB", sub = "RAM-Disk Cache")
                        VmSpecBadge(title = "NVMe", value = "${config.dataStorageTb} TB Data", sub = "${config.osStorageGb}GB OS")
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // GPU Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF064E3B), Color(0xFF065F46))
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = Color(0xFF34D399),
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = config.gpuModel,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "TensorRT LLM Code Synthesis & CUDA 12.4 Acceleration Active",
                                    color = Color(0xFFA7F3D0),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Build Trigger & Progress Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Real-Time VM Build Pipeline",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isBuilding) buildStage else "Ready to compile selected app using 64 vCPU & H100 GPU.",
                        fontSize = 13.sp,
                        color = if (isBuilding) AppStudioBlue else Color(0xFF64748B),
                        fontWeight = if (isBuilding) FontWeight.Bold else FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isBuilding) {
                        LinearProgressIndicator(
                            progress = { buildProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = AppStudioBlue,
                            trackColor = Color(0xFFE2E8F0)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${(buildProgress * 100).toInt()}% completed",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.align(Alignment.End)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    AppStudioPrimaryButton(
                        text = if (isBuilding) "COMPILING ON H100..." else "TRIGGER BUILD ON H100 GPU",
                        onClick = { onTriggerBuild(selectedProjectId) },
                        enabled = !isBuilding,
                        testTag = "trigger_h100_build_button"
                    )
                }
            }
        }

        // Live Build Terminal Console
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0A0F1D))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF10B981)))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Build Console & Telemetry",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        TextButton(onClick = onClearLogs) {
                            Text("Clear", color = Color(0xFF38BDF8), fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 160.dp, max = 260.dp)
                            .background(Color(0xFF030712), RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        logs.takeLast(12).forEach { log ->
                            val color = when (log.level) {
                                LogLevel.INFO -> Color(0xFF94A3B8)
                                LogLevel.BUILD -> Color(0xFF38BDF8)
                                LogLevel.GPU -> Color(0xFF34D399)
                                LogLevel.SUCCESS -> Color(0xFF4ADE80)
                                LogLevel.WARN -> Color(0xFFFBBF24)
                            }
                            Text(
                                text = "[${log.timestamp}] [${log.level}] ${log.message}",
                                color = color,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }

    if (showConfigDialog) {
        VmConfigDialog(
            config = config,
            onDismiss = { showConfigDialog = false },
            onSave = { host, port, gpu, ram, tensor ->
                onUpdateConfig(host, port, gpu, ram, tensor)
                showConfigDialog = false
            }
        )
    }
}

@Composable
private fun VmSpecBadge(title: String, value: String, sub: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E293B))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = title, fontSize = 10.sp, color = Color(0xFF94A3B8))
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = sub, fontSize = 9.sp, color = Color(0xFF38BDF8))
    }
}

// ----------------- Tab 4: Builder Studio & Live Phone Preview -----------------

@Composable
private fun BuilderTabContent(
    projects: List<AppProject>,
    selectedProjectId: String,
    previewAppName: String,
    previewColorHex: String,
    previewDarkMode: Boolean,
    previewScreenIndex: Int,
    previewCounter: Int,
    onSelectProject: (String) -> Unit,
    onUpdateDetails: (String, String, String, String, Boolean, Boolean, Boolean) -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    onSelectPreviewTab: (Int) -> Unit,
    onIncrementCounter: () -> Unit,
    onOpenAdmin: () -> Unit
) {
    val currentProj = projects.find { it.id == selectedProjectId } ?: projects.firstOrNull()

    var nameInput by remember(selectedProjectId) { mutableStateOf(currentProj?.name ?: "") }
    var pkgInput by remember(selectedProjectId) { mutableStateOf(currentProj?.packageName ?: "") }
    var colorHex by remember(selectedProjectId) { mutableStateOf(currentProj?.primaryColorHex ?: "#0084FF") }
    var adMob by remember(selectedProjectId) { mutableStateOf(currentProj?.adMobEnabled ?: false) }
    var iap by remember(selectedProjectId) { mutableStateOf(currentProj?.inAppPurchases ?: false) }
    var push by remember(selectedProjectId) { mutableStateOf(currentProj?.pushNotifications ?: true) }

    val primaryColor = try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (e: Exception) {
        AppStudioBlue
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Switcher Dropdown
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Active App in Studio",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(projects) { p ->
                            val isSel = p.id == selectedProjectId
                            FilterChip(
                                selected = isSel,
                                onClick = { onSelectProject(p.id) },
                                label = { Text(p.name, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AppStudioBlue,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // Live Phone Mockup Preview (Centerpiece)
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Live Interactive Preview",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tap on tabs and interact with buttons directly on screen",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(12.dp))

                InteractivePhoneMockup(
                    appName = nameInput.ifBlank { "My Mobile App" },
                    primaryColor = primaryColor,
                    isDarkMode = previewDarkMode,
                    currentScreen = previewScreenIndex,
                    counterValue = previewCounter,
                    onTabSelected = onSelectPreviewTab,
                    onIncrementCounter = onIncrementCounter
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Preview controls row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onOpenAdmin,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open Admin Panel", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { onToggleDarkMode(!previewDarkMode) },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = if (previewDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (previewDarkMode) "Light" else "Dark", fontSize = 12.sp)
                    }
                }
            }
        }

        // App Customization Form Sheet
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "App Identity & Theme Settings",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    // App Name
                    Column {
                        Text("App Display Name", fontSize = 12.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        AppStudioInputField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            placeholder = "e.g. FitPulse Pro",
                            testTag = "builder_name_input"
                        )
                    }

                    // Package Name
                    Column {
                        Text("Android Package ID", fontSize = 12.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        AppStudioInputField(
                            value = pkgInput,
                            onValueChange = { pkgInput = it },
                            placeholder = "com.company.app",
                            testTag = "builder_pkg_input"
                        )
                    }

                    // Color Palette Chooser
                    Column {
                        Text("Primary Brand Color", fontSize = 12.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        val colors = listOf("#0084FF", "#7C3AED", "#10B981", "#E11D48", "#F59E0B", "#00E5FF")
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            colors.forEach { cHex ->
                                val isSelected = colorHex.equals(cHex, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(android.graphics.Color.parseColor(cHex)))
                                        .border(
                                            width = if (isSelected) 3.dp else 0.dp,
                                            color = Color(0xFF0F172A),
                                            shape = CircleShape
                                        )
                                        .clickable { colorHex = cHex },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }

                    // Features Toggles
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Text("Features & Monetization", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Google AdMob Ads", fontSize = 13.sp)
                        Switch(checked = adMob, onCheckedChange = { adMob = it })
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("In-App Purchases (IAP)", fontSize = 13.sp)
                        Switch(checked = iap, onCheckedChange = { iap = it })
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Push Notifications Engine", fontSize = 13.sp)
                        Switch(checked = push, onCheckedChange = { push = it })
                    }

                    // Save Changes Button
                    AppStudioPrimaryButton(
                        text = "SAVE APP CONFIGURATION",
                        onClick = {
                            if (currentProj != null) {
                                onUpdateDetails(currentProj.id, nameInput, pkgInput, colorHex, adMob, iap, push)
                            }
                        },
                        testTag = "save_builder_changes_button"
                    )
                }
            }
        }
    }
}

// ----------------- Tab 5: Publish & Premium -----------------

@Composable
private fun PublishTabContent(
    projects: List<AppProject>,
    selectedProjectId: String,
    isPremium: Boolean,
    onSelectProject: (String) -> Unit
) {
    val currentProj = projects.find { it.id == selectedProjectId } ?: projects.firstOrNull()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Premium Header (Matches Screenshot 3)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(PremiumYellow)
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🚀 PREMIUM UNLOCKED",
                            color = Color(0xFFB45309),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFF59E0B))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("ACTIVE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Google Play Publishing Rights",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Publish apps to the Google Play Store, monetize with ads and subscriptions, and build at lightning speed on NVIDIA H100.",
                        fontSize = 13.sp,
                        color = Color(0xFF78350F),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Checklist for Play Store Release
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Play Store Release Checklist: ${currentProj?.name ?: "App"}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ChecklistRowItem(title = "Production Keystore Signed", status = "Ready (SHA-256 v3)")
                        ChecklistRowItem(title = "Android App Bundle (.aab)", status = "Generated & Optimized")
                        ChecklistRowItem(title = "Target SDK 36 (Android 15)", status = "100% Policy Compliant")
                        ChecklistRowItem(title = "Zero Prohibited Permissions", status = "Safe Drawing & Photo Picker")
                        ChecklistRowItem(title = "Privacy Policy & Data Safety", status = "Auto-Generated URL Ready")
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    AppStudioPrimaryButton(
                        text = "DOWNLOAD PRODUCTION .AAB BUNDLE",
                        onClick = { },
                        testTag = "download_aab_bundle_button"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    AppStudioOutlinedButton(
                        text = "DOWNLOAD TEST .APK (DEBUG/RELEASE)",
                        onClick = { },
                        testTag = "download_test_apk_button"
                    )
                }
            }
        }
    }
}

@Composable
private fun ChecklistRowItem(title: String, status: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
        }
        Text(text = status, fontSize = 11.sp, color = Color(0xFF059669), fontWeight = FontWeight.Bold)
    }
}

// ----------------- Dialogs -----------------

@Composable
private fun CreateProjectDialog(
    templates: List<AppTemplate>,
    onDismiss: () -> Unit,
    onCreate: (AppTemplate, String) -> Unit
) {
    var selectedTemplate by remember { mutableStateOf(templates.first()) }
    var projectName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Create New Android App", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Select Starting Template:", fontSize = 13.sp, color = Color(0xFF64748B))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(templates) { t ->
                        val isSel = t.id == selectedTemplate.id
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedTemplate = t },
                            label = { Text(t.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AppStudioBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                AppStudioInputField(
                    value = projectName,
                    onValueChange = { projectName = it },
                    placeholder = "App Name (e.g. My Next Store)",
                    testTag = "new_project_name_input"
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalName = projectName.ifBlank { "${selectedTemplate.name} App" }
                    onCreate(selectedTemplate, finalName)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppStudioBlue)
            ) {
                Text("Create App")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun VmConfigDialog(
    config: VmRunnerConfig,
    onDismiss: () -> Unit,
    onSave: (String, Int, Boolean, Boolean, Boolean) -> Unit
) {
    var host by remember { mutableStateOf(config.host) }
    var portText by remember { mutableStateOf(config.port.toString()) }
    var gpu by remember { mutableStateOf(config.isGpuAccelerated) }
    var ramDisk by remember { mutableStateOf(config.useRamDiskGradleCache) }
    var tensorRt by remember { mutableStateOf(config.tensorRtCodeGen) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("VM Runner Connection", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Connect directly to your 64 vCPU, 256GB RAM, H100 GPU workstation.", fontSize = 12.sp, color = Color(0xFF64748B))

                AppStudioInputField(value = host, onValueChange = { host = it }, placeholder = "Host IP / Domain")
                AppStudioInputField(value = portText, onValueChange = { portText = it }, placeholder = "Port (8080)")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("NVIDIA H100 GPU Acceleration", fontSize = 12.sp)
                    Switch(checked = gpu, onCheckedChange = { gpu = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("32GB RAM-Disk Gradle Daemon", fontSize = 12.sp)
                    Switch(checked = ramDisk, onCheckedChange = { ramDisk = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("TensorRT AST Code Optimization", fontSize = 12.sp)
                    Switch(checked = tensorRt, onCheckedChange = { tensorRt = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = portText.toIntOrNull() ?: 8080
                    onSave(host, p, gpu, ramDisk, tensorRt)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppStudioBlue)
            ) {
                Text("Save & Connect")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
