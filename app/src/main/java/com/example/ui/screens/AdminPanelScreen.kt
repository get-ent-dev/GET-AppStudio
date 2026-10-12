package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AdminAnnouncement
import com.example.model.AdminUser
import com.example.model.AppProject
import com.example.ui.components.AppStudioInputField
import com.example.ui.components.AppStudioPrimaryButton
import com.example.ui.theme.AppStudioBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    project: AppProject?,
    users: List<AdminUser>,
    announcements: List<AdminAnnouncement>,
    onBack: () -> Unit,
    onSendAnnouncement: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var announceTitle by remember { mutableStateOf("") }
    var announceBody by remember { mutableStateOf("") }
    var selectedSection by remember { mutableStateOf("Overview") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Admin Panel",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Controlling: ${project?.name ?: "All Apps"}",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF0F172A)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Metrics Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Live App Control Panel",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Manage users, broadcast instant push alerts, and update app settings in real time.",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AdminMetric(label = "Total App Installs", value = "14,280")
                            AdminMetric(label = "Active Today", value = "1,894")
                            AdminMetric(label = "Push Open Rate", value = "42.8%")
                        }
                    }
                }
            }

            // Push Notification Broadcaster
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE0F2FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Campaign, contentDescription = null, tint = AppStudioBlue, modifier = Modifier.size(20.dp))
                            }
                            Text(
                                text = "Send Live Push Notification",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        AppStudioInputField(
                            value = announceTitle,
                            onValueChange = { announceTitle = it },
                            placeholder = "Notification Title (e.g. Flash Deal!)",
                            testTag = "admin_push_title_input"
                        )

                        AppStudioInputField(
                            value = announceBody,
                            onValueChange = { announceBody = it },
                            placeholder = "Notification Message Body...",
                            testTag = "admin_push_body_input"
                        )

                        AppStudioPrimaryButton(
                            text = "BROADCAST TO ALL USERS",
                            onClick = {
                                onSendAnnouncement(announceTitle, announceBody)
                                announceTitle = ""
                                announceBody = ""
                            },
                            enabled = announceTitle.isNotBlank() && announceBody.isNotBlank(),
                            testTag = "send_broadcast_button"
                        )
                    }
                }
            }

            // User Management Roster
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Registered App Users & Roles",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            users.forEach { u ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF8FAFC))
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(AppStudioBlue),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = u.email.take(1).uppercase(),
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Column {
                                            Text(text = u.email, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                            Text(text = "Joined: ${u.joinedDate}", fontSize = 10.sp, color = Color(0xFF94A3B8))
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (u.role == "Admin") Color(0xFFEDE9FE) else Color(0xFFE2E8F0))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = u.role,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (u.role == "Admin") Color(0xFF6B21A8) else Color(0xFF475569)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Code Editor & Android Manifest Inspector
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Generated Android Manifest & Config",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Icon(Icons.Default.Code, contentDescription = null, tint = Color(0xFF38BDF8))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF030712), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = """
                                <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                                    package="${project?.packageName ?: "com.appstudio.custom"}">
                                    <application
                                        android:label="${project?.name ?: "My App"}"
                                        android:theme="@style/Theme.Material3">
                                        <activity android:name=".MainActivity" android:exported="true">
                                            <intent-filter>
                                                <action android:name="android.intent.action.MAIN" />
                                                <category android:name="android.intent.category.LAUNCHER" />
                                            </intent-filter>
                                        </activity>
                                    </application>
                                </manifest>
                                """.trimIndent(),
                                color = Color(0xFF38BDF8),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminMetric(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 10.sp, color = Color(0xFF94A3B8))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}
