package com.ilyassavli.helloworld.core.designsystem.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ilyassavli.helloworld.core.designsystem.theme.HelloWorldTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HelloWorldTopAppBarTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsTitle() {
        composeRule.setContent { HelloWorldTheme { HelloWorldTopAppBar(title = "Title") } }

        composeRule.onNodeWithText("Title").assertIsDisplayed()
    }
}
