package com.example

import com.example.model.ProjectStatus
import com.example.viewmodel.AppStudioViewModel
import com.example.viewmodel.ScreenState
import com.example.viewmodel.WorkspaceTab
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {
    private lateinit var viewModel: AppStudioViewModel

    @Before
    fun setup() {
        viewModel = AppStudioViewModel()
    }

    @Test
    fun onboarding_advancesCorrectly() {
        assertEquals(0, viewModel.uiState.value.onboardingStep)
        viewModel.nextOnboardingStep()
        assertEquals(1, viewModel.uiState.value.onboardingStep)
        viewModel.nextOnboardingStep()
        assertEquals(2, viewModel.uiState.value.onboardingStep)
        viewModel.nextOnboardingStep()
        assertEquals(3, viewModel.uiState.value.onboardingStep)
        viewModel.nextOnboardingStep()
        assertEquals(ScreenState.NOTIFICATIONS, viewModel.uiState.value.currentScreen)
    }

    @Test
    fun skip_jumpsToAuth() {
        viewModel.skipOnboarding()
        assertEquals(ScreenState.AUTH, viewModel.uiState.value.currentScreen)
    }

    @Test
    fun signIn_authenticatesUserAndNavigatesToWorkspace() {
        viewModel.updateAuthEmail("admin@getappstudio.io")
        viewModel.signIn()
        assertTrue(viewModel.uiState.value.isLoggedIn)
        assertEquals(ScreenState.MAIN_WORKSPACE, viewModel.uiState.value.currentScreen)
        assertEquals("admin@getappstudio.io", viewModel.uiState.value.userProfile.email)
    }

    @Test
    fun vmConfig_hasUserH100HardwareSpecs() {
        val vm = viewModel.uiState.value.vmConfig
        assertEquals(64, vm.vcpuCount)
        assertEquals(256, vm.ramGb)
        assertEquals(4, vm.dataStorageTb)
        assertTrue(vm.gpuModel.contains("H100"))
        assertTrue(vm.isGpuAccelerated)
        assertTrue(vm.useRamDiskGradleCache)
    }

    @Test
    fun projectCreation_andCustomizationWork() {
        val tmpl = viewModel.uiState.value.templates.first()
        viewModel.createProjectFromTemplate(tmpl, "Test AI Studio App")
        val created = viewModel.uiState.value.projects.first()
        assertEquals("Test AI Studio App", created.name)
        assertEquals(WorkspaceTab.BUILDER, viewModel.uiState.value.currentTab)
    }
}
