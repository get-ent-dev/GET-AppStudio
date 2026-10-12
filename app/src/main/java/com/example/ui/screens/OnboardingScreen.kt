package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    currentStep: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar with Skip Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clickable(onClick = onSkip)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("skip_button")
                ) {
                    Text(
                        text = "Skip",
                        color = Color(0xFF94A3B8),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Animated Step Content
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                modifier = Modifier.weight(1f),
                label = "onboarding_step_content"
            ) { step ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Illustration for current step
                    when (step) {
                        0 -> IllustrationThreePhones(modifier = Modifier.padding(bottom = 24.dp))
                        1 -> IllustrationAdminPanel(modifier = Modifier.padding(bottom = 24.dp))
                        2 -> IllustrationTemplates(modifier = Modifier.padding(bottom = 24.dp))
                        3 -> IllustrationPublishPlay(modifier = Modifier.padding(bottom = 24.dp))
                    }

                    // Headline Title
                    Text(
                        text = when (step) {
                            0 -> "Build Android apps\nwithout coding."
                            1 -> "Customize your app\nfully."
                            2 -> "Start with templates\n—Business, Movies, AI\nChat."
                            3 -> "Publish to Google Play—\nunlock with Premium."
                            else -> ""
                        },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description Subtitle
                    Text(
                        text = when (step) {
                            0 -> "Turn your ideas into reality with our android app builder. No programming knowledge needed. Anyone can create and publish an award winning app!"
                            1 -> "Get your own admin panel to control everything! Add content, change app name, edit code, manage users, update settings, and so much more. Full creative control."
                            2 -> "Choose from professionally designed templates and launch in minutes. We're always expanding! New templates added regularly and existing ones updated with fresh features.\nAppStudio Is ALIVE!"
                            3 -> "Get your app on the Play Store and start earning! Premium unlocks publishing rights plus monetization features like ads, in-app purchases, and paid downloads."
                            else -> ""
                        },
                        fontSize = 15.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    // Step 3 specific yellow Premium Badge
                    if (step == 3) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(PremiumYellow)
                                .padding(vertical = 14.dp, horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "🚀 PREMIUM",
                                    color = Color(0xFFD97706),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Google Play Publishing",
                                    color = Color(0xFF0F172A),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Navigation Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Progress Indicator Bar
                OnboardingStepIndicator(
                    currentStep = currentStep,
                    totalSteps = 4
                )

                // Main CTA Button
                AppStudioPrimaryButton(
                    text = if (currentStep == 3) "START BUILDING" else "NEXT",
                    onClick = onNext,
                    testTag = if (currentStep == 3) "start_building_button" else "next_step_button"
                )
            }
        }
    }
}
