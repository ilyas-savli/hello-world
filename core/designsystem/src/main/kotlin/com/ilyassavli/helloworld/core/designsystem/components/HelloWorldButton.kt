package com.ilyassavli.helloworld.core.designsystem.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.ilyassavli.helloworld.core.designsystem.theme.HelloWorldTheme

/** Primary button. Text comes in as a parameter — designsystem never reads string resources (DS-03). */
@Composable
fun HelloWorldButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = HelloWorldTheme.spacing.minTouchTarget),
        enabled = enabled,
    ) {
        Text(text = text)
    }
}

@PreviewLightDark
@Composable
private fun HelloWorldButtonPreview() {
    HelloWorldTheme {
        HelloWorldButton(text = "Retry", onClick = {})
    }
}
