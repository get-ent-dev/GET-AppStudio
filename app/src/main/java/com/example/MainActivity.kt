package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.WorkspaceScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppStudioViewModel
import com.example.viewmodel.ScreenState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppStudioMainApp()
            }
        }
    }
}

@Composable
fun AppStudioMainApp(viewModel: AppStudioViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.statusSnackbarMessage) {
        val msg = uiState.statusSnackbarMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissSnackbar()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = uiState.currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "app_studio_screen_transition"
            ) { screen ->
                when (screen) {
                    ScreenState.ONBOARDING -> OnboardingScreen(
                        currentStep = uiState.onboardingStep,
                        onNext = { viewModel.nextOnboardingStep() },
                        onSkip = { viewModel.skipOnboarding() }
                    )
                    ScreenState.NOTIFICATIONS -> NotificationsScreen(
                        onPermissionResult = { granted ->
                            viewModel.proceedFromNotifications(granted)
                        }
                    )
                    ScreenState.AUTH -> AuthScreen(
                        isSignUp = uiState.isSignUpMode,
                        email = uiState.authEmail,
                        pass = uiState.authPass,
                        name = uiState.authName,
                        isPasswordVisible = uiState.isPasswordVisible,
                        onEmailChange = { viewModel.updateAuthEmail(it) },
                        onPassChange = { viewModel.updateAuthPassword(it) },
                        onNameChange = { viewModel.updateAuthName(it) },
                        onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() },
                        onToggleAuthMode = { viewModel.setAuthMode(it) },
                        onSignIn = { viewModel.signIn() },
                        onGoogleSignIn = { viewModel.signInWithGoogle() }
                    )
                    ScreenState.MAIN_WORKSPACE,
                    ScreenState.BUILD_CONSOLE -> WorkspaceScreen(
                        viewModel = viewModel,
                        uiState = uiState
                    )
                    ScreenState.ADMIN_PANEL -> {
                        val selProj = uiState.projects.find { it.id == uiState.selectedProjectId }
                        AdminPanelScreen(
                            project = selProj,
                            users = uiState.adminUsers,
                            announcements = uiState.adminAnnouncements,
                            onBack = { viewModel.navigateBackToWorkspace() },
                            onSendAnnouncement = { title, content ->
                                viewModel.sendAdminAnnouncement(title, content)
                            }
                        )
                    }
                }
            }
        }
    }
}
