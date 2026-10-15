package com.ilyassavli.helloworld.core.ui

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ilyassavli.helloworld.core.designsystem.theme.HelloWorldTheme
import com.ilyassavli.helloworld.core.i18n.R
import com.ilyassavli.helloworld.core.testing.resource.stringResource
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ErrorStateTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsMessage_andRetryClickInvokesCallback() {
        var retries = 0
        composeRule.setContent {
            HelloWorldTheme { ErrorState(message = "Something went wrong", onRetry = { retries++ }) }
        }

        composeRule.onNodeWithText("Something went wrong").assertExists()
        composeRule.onNodeWithText(stringResource(R.string.common_retry)).performClick()

        assertEquals(1, retries)
    }
}
