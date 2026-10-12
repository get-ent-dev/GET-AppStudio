package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    isSignUp: Boolean,
    email: String,
    pass: String,
    name: String,
    isPasswordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleAuthMode: (Boolean) -> Unit,
    onSignIn: () -> Unit,
    onGoogleSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val headerGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF8B2FC9),
            Color(0xFF6B21A8),
            Color(0xFF3B82F6),
            Color(0xFF0084FF)
        )
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Black,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black)
        ) {
            // AppStudio Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color.Black)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "AppStudio",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }

            // Main Scrollable Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(headerGradient)
                    .verticalScroll(scrollState)
            ) {
                // Purple Gradient Banner Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Illustration
                    IllustrationAuthHeader()

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isSignUp) "Create Account" else "Welcome Back",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isSignUp) "Start creating powerful Android apps" else "Sign in to continue building amazing apps",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // White Card Sheet
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(Color.White)
                        .padding(horizontal = 24.dp, vertical = 28.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Name Field (if in Sign Up mode)
                        if (isSignUp) {
                            AppStudioInputField(
                                value = name,
                                onValueChange = onNameChange,
                                placeholder = "Full name",
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = Color(0xFF6366F1),
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                testTag = "auth_name_input"
                            )
                        }

                        // Email Field (Orange envelope icon)
                        AppStudioInputField(
                            value = email,
                            onValueChange = onEmailChange,
                            placeholder = "Email address",
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFFFEDD5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = Color(0xFFEA580C),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            testTag = "auth_email_input"
                        )

                        // Password Field (Blue padlock & eye toggle)
                        AppStudioInputField(
                            value = pass,
                            onValueChange = onPassChange,
                            placeholder = "Password",
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFE0F2FE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = Color(0xFF0284C7),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = onTogglePasswordVisibility,
                                    modifier = Modifier.testTag("toggle_password_visibility")
                                ) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password visibility",
                                        tint = Color(0xFF64748B)
                                    )
                                }
                            },
                            testTag = "auth_password_input"
                        )

                        // Forgot Password Link
                        if (!isSignUp) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .clickable { }
                                    .padding(vertical = 4.dp)
                                    .testTag("forgot_password_button")
                            ) {
                                Text(
                                    text = "Forgot Password?",
                                    color = AppStudioBlue,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Primary Action Button ("SIGN IN" / "CREATE ACCOUNT")
                        AppStudioPrimaryButton(
                            text = if (isSignUp) "CREATE ACCOUNT" else "SIGN IN",
                            onClick = onSignIn,
                            backgroundColor = AppStudioBlue,
                            testTag = "submit_auth_button"
                        )

                        // "or continue with" Divider
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                            Text(
                                text = "  or continue with  ",
                                color = Color(0xFF94A3B8),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                        }

                        // Google Sign-In Button
                        OutlinedButton(
                            onClick = onGoogleSignIn,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("continue_with_google_button"),
                            shape = RoundedCornerShape(28.dp),
                            border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFE2E8F0)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color(0xFFF8FAFC),
                                contentColor = Color(0xFF0F172A)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "G",
                                    color = Color(0xFFEA4335),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Continue with Google",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Switch between Sign In & Sign Up
                        Text(
                            text = if (isSignUp) "Already have an account?" else "Don't have an account?",
                            color = Color(0xFF64748B),
                            fontSize = 14.sp
                        )

                        AppStudioOutlinedButton(
                            text = if (isSignUp) "Sign In to Existing Account" else "Create New Account",
                            onClick = { onToggleAuthMode(!isSignUp) },
                            borderColor = Color(0xFF0F172A),
                            textColor = Color(0xFF0F172A),
                            testTag = "toggle_auth_mode_button"
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Footer (from Screenshot 6/7)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                text = "Build • Customize • Publish",
                                color = AppStudioBlue,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "AppStudio App Builder v1.0",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
