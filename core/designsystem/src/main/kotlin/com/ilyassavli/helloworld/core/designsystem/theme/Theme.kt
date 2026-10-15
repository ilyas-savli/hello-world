package com.ilyassavli.helloworld.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

/**
 * App theme. Dynamic color is off by default so the brand colors from the design are used;
 * turn it on only if the spec asks for Material You.
 */
@Composable
fun HelloWorldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    CompositionLocalProvider(LocalHelloWorldSpacing provides HelloWorldSpacing()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = HelloWorldTypography,
            content = content,
        )
    }
}

/** Access to the app's non-Material tokens, e.g. `HelloWorldTheme.spacing.medium`. */
object HelloWorldTheme {
    val spacing: HelloWorldSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalHelloWorldSpacing.current
}
