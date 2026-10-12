package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InteractivePhoneMockup(
    appName: String,
    primaryColor: Color,
    isDarkMode: Boolean,
    currentScreen: Int,
    counterValue: Int,
    onTabSelected: (Int) -> Unit,
    onIncrementCounter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val phoneBg = if (isDarkMode) Color(0xFF0F172A) else Color(0xFFF8FAFC)
    val phoneTextPrimary = if (isDarkMode) Color(0xFFF8FAFC) else Color(0xFF0F172A)
    val phoneCardBg = if (isDarkMode) Color(0xFF1E293B) else Color.White
    val phoneBorder = if (isDarkMode) Color(0xFF334155) else Color(0xFFE2E8F0)

    // Outer Phone Frame
    Box(
        modifier = modifier
            .width(290.dp)
            .height(560.dp)
            .shadow(24.dp, RoundedCornerShape(36.dp))
            .clip(RoundedCornerShape(36.dp))
            .background(Color(0xFF0A0F1D))
            .border(6.dp, Color(0xFF1E293B), RoundedCornerShape(36.dp))
            .padding(4.dp)
    ) {
        // Inner Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(30.dp))
                .background(phoneBg)
        ) {
            // Status Bar & Camera Notch
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(phoneBg)
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "9:41",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = phoneTextPrimary
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SignalCellular4Bar,
                            contentDescription = null,
                            tint = phoneTextPrimary,
                            modifier = Modifier.size(11.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = null,
                            tint = phoneTextPrimary,
                            modifier = Modifier.size(11.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.BatteryChargingFull,
                            contentDescription = null,
                            tint = phoneTextPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Camera pill notch
                Box(
                    modifier = Modifier
                        .size(60.dp, 10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.Black)
                )
            }

            // Top Bar of the Mockup App
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(primaryColor)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = appName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Screen Content Body
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                when (currentScreen) {
                    0 -> MockupHomeScreen(
                        phoneCardBg = phoneCardBg,
                        phoneTextPrimary = phoneTextPrimary,
                        phoneBorder = phoneBorder,
                        primaryColor = primaryColor,
                        counterValue = counterValue,
                        onIncrement = onIncrementCounter
                    )
                    1 -> MockupExploreScreen(
                        phoneCardBg = phoneCardBg,
                        phoneTextPrimary = phoneTextPrimary,
                        phoneBorder = phoneBorder,
                        primaryColor = primaryColor
                    )
                    2 -> MockupChatScreen(
                        phoneCardBg = phoneCardBg,
                        phoneTextPrimary = phoneTextPrimary,
                        phoneBorder = phoneBorder,
                        primaryColor = primaryColor
                    )
                    else -> MockupProfileScreen(
                        phoneCardBg = phoneCardBg,
                        phoneTextPrimary = phoneTextPrimary,
                        phoneBorder = phoneBorder,
                        primaryColor = primaryColor
                    )
                }
            }

            // Bottom Navigation Bar in Phone Mockup
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(phoneCardBg)
                    .border(0.5.dp, phoneBorder, RoundedCornerShape(0.dp))
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MockupNavIcon(
                        icon = Icons.Default.Home,
                        label = "Home",
                        isSelected = currentScreen == 0,
                        activeColor = primaryColor,
                        onClick = { onTabSelected(0) }
                    )
                    MockupNavIcon(
                        icon = Icons.Default.Explore,
                        label = "Explore",
                        isSelected = currentScreen == 1,
                        activeColor = primaryColor,
                        onClick = { onTabSelected(1) }
                    )
                    MockupNavIcon(
                        icon = Icons.Default.ChatBubble,
                        label = "AI Chat",
                        isSelected = currentScreen == 2,
                        activeColor = primaryColor,
                        onClick = { onTabSelected(2) }
                    )
                    MockupNavIcon(
                        icon = Icons.Default.Person,
                        label = "Profile",
                        isSelected = currentScreen == 3,
                        activeColor = primaryColor,
                        onClick = { onTabSelected(3) }
                    )
                }
            }

            // Home Bar Indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .background(phoneCardBg),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp, 3.dp)
                        .clip(RoundedCornerShape(1.5.dp))
                        .background(if (isDarkMode) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f))
                )
            }
        }
    }
}

@Composable
private fun MockupNavIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) activeColor else Color(0xFF94A3B8),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) activeColor else Color(0xFF94A3B8)
        )
    }
}

@Composable
private fun MockupHomeScreen(
    phoneCardBg: Color,
    phoneTextPrimary: Color,
    phoneBorder: Color,
    primaryColor: Color,
    counterValue: Int,
    onIncrement: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Hero card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(primaryColor)
                .padding(10.dp)
        ) {
            Column {
                Text(
                    text = "Welcome to Your App!",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Powered by GET AppStudio",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 9.sp
                )
            }
        }

        // Interactive counter widget card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(phoneCardBg)
                .border(1.dp, phoneBorder, RoundedCornerShape(10.dp))
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Active Clicks",
                        fontSize = 10.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "$counterValue interactions",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = phoneTextPrimary
                    )
                }

                Button(
                    onClick = onIncrement,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("+ Tap", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Mini Feature items
        for (i in 1..2) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(phoneCardBg)
                    .border(1.dp, phoneBorder, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(primaryColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (i == 1) Icons.Default.Star else Icons.Default.Check,
                            contentDescription = null,
                            tint = primaryColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Column {
                        Text(
                            text = if (i == 1) "Featured Module #1" else "Real-time Cloud Sync",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = phoneTextPrimary
                        )
                        Text(
                            text = "Instant updates from Admin Panel",
                            fontSize = 8.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MockupExploreScreen(
    phoneCardBg: Color,
    phoneTextPrimary: Color,
    phoneBorder: Color,
    primaryColor: Color
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Catalog & Content",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = phoneTextPrimary
        )
        for (item in listOf("Product Showcase", "Special Deals", "Recommended for You")) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(phoneCardBg)
                    .border(1.dp, phoneBorder, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = item, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = phoneTextPrimary)
                        Text(text = "4.9 ★ (180 reviews)", fontSize = 8.sp, color = Color(0xFFF59E0B))
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MockupChatScreen(
    phoneCardBg: Color,
    phoneTextPrimary: Color,
    phoneBorder: Color,
    primaryColor: Color
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            // Bot bubble
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(primaryColor.copy(alpha = 0.15f))
                    .padding(8.dp)
            ) {
                Text(
                    text = "Hello! I am your AI assistant in GET AppStudio. How can I help you build today?",
                    fontSize = 9.sp,
                    color = phoneTextPrimary
                )
            }

            // User bubble
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(primaryColor)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Generate Android release bundle!",
                    fontSize = 9.sp,
                    color = Color.White
                )
            }
        }

        // Mock input bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(phoneCardBg)
                .border(1.dp, phoneBorder, RoundedCornerShape(14.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Type a prompt...", fontSize = 9.sp, color = Color(0xFF94A3B8))
                Icon(Icons.Default.Send, contentDescription = null, tint = primaryColor, modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
private fun MockupProfileScreen(
    phoneCardBg: Color,
    phoneTextPrimary: Color,
    phoneBorder: Color,
    primaryColor: Color
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(primaryColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Text("App User #142", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = phoneTextPrimary)
        Text("VIP Subscriber", fontSize = 9.sp, color = Color(0xFF10B981))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(phoneCardBg)
                .border(1.dp, phoneBorder, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("App Version: 1.0.0-Release", fontSize = 8.sp, color = Color(0xFF64748B))
                Text("Build Target: Android 15 (API 36)", fontSize = 8.sp, color = Color(0xFF64748B))
            }
        }
    }
}
