package com.ilyassavli.helloworld.feature.hello

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.ilyassavli.helloworld.core.designsystem.components.HelloWorldTopAppBar
import com.ilyassavli.helloworld.core.designsystem.theme.HelloWorldTheme
import com.ilyassavli.helloworld.core.i18n.R

/** The greeting screen. It has no state of its own, so it needs no ViewModel. */
@Composable
internal fun HelloScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { HelloWorldTopAppBar(title = stringResource(R.string.common_app_name)) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(HelloWorldTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(HelloWorldTheme.spacing.small, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.hello_greeting),
                modifier = Modifier.semantics { heading() },
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.hello_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun HelloScreenPreview() {
    HelloWorldTheme {
        HelloScreen()
    }
}
