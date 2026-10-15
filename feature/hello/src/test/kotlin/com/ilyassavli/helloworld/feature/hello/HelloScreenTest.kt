package com.ilyassavli.helloworld.feature.hello

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ilyassavli.helloworld.core.designsystem.theme.HelloWorldTheme
import com.ilyassavli.helloworld.core.i18n.R
import com.ilyassavli.helloworld.core.testing.resource.stringResource
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HelloScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsGreetingAsHeading_andSubtitle_andAppNameInTopBar() {
        composeRule.setContent { HelloWorldTheme { HelloScreen() } }

        composeRule.onNodeWithText(stringResource(R.string.hello_greeting)).assertIsDisplayed()
        composeRule.onNode(isHeading()).assertIsDisplayed()
        composeRule.onNodeWithText(stringResource(R.string.hello_subtitle)).assertIsDisplayed()
        composeRule.onNodeWithText(stringResource(R.string.common_app_name)).assertIsDisplayed()
    }
}
