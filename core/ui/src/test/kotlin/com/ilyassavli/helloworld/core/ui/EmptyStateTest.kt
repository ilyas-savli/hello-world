package com.ilyassavli.helloworld.core.ui

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ilyassavli.helloworld.core.designsystem.theme.HelloWorldTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EmptyStateTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsMessage() {
        composeRule.setContent { HelloWorldTheme { EmptyState(message = "Nothing here yet") } }

        composeRule.onNodeWithText("Nothing here yet").assertExists()
    }
}
