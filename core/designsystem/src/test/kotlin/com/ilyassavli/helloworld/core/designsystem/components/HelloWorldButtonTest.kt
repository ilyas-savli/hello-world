package com.ilyassavli.helloworld.core.designsystem.components

import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ilyassavli.helloworld.core.designsystem.theme.HelloWorldTheme
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HelloWorldButtonTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun click_invokesCallback_andMeetsTouchTarget() {
        var clicks = 0
        composeRule.setContent { HelloWorldTheme { HelloWorldButton(text = "Retry", onClick = { clicks++ }) } }

        composeRule.onNodeWithText("Retry").assertHeightIsAtLeast(48.dp).performClick()

        assertEquals(1, clicks)
    }
}
