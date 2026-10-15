package com.ilyassavli.helloworld.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.ilyassavli.helloworld.core.designsystem.components.HelloWorldButton
import com.ilyassavli.helloworld.core.designsystem.theme.HelloWorldTheme
import com.ilyassavli.helloworld.core.i18n.R

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(HelloWorldTheme.spacing.large),
        verticalArrangement = Arrangement.spacedBy(HelloWorldTheme.spacing.medium, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        HelloWorldButton(
            text = stringResource(R.string.common_retry),
            onClick = onRetry,
        )
    }
}

@PreviewLightDark
@Composable
private fun ErrorStatePreview() {
    HelloWorldTheme {
        ErrorState(message = "No internet connection.", onRetry = {})
    }
}
