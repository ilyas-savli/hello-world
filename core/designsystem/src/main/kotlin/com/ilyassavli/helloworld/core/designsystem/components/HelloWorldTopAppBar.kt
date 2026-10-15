package com.ilyassavli.helloworld.core.designsystem.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.ilyassavli.helloworld.core.designsystem.theme.HelloWorldTheme

/** Top app bar with a title. Text comes in as a parameter — designsystem never reads string resources (DS-03). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelloWorldTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
    )
}

@PreviewLightDark
@Composable
private fun HelloWorldTopAppBarPreview() {
    HelloWorldTheme {
        HelloWorldTopAppBar(title = "Hello World")
    }
}
