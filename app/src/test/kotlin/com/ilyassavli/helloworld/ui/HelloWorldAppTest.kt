package com.ilyassavli.helloworld.ui

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ilyassavli.helloworld.core.designsystem.theme.HelloWorldTheme
import com.ilyassavli.helloworld.core.i18n.R
import com.ilyassavli.helloworld.core.testing.resource.stringResource
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

// A plain Application: the start destination needs no Hilt graph.
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class HelloWorldAppTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun startDestination_isTheGreetingScreen() {
        composeRule.setContent { HelloWorldTheme { HelloWorldApp() } }

        composeRule.onNodeWithText(stringResource(R.string.hello_greeting)).assertIsDisplayed()
    }
}
