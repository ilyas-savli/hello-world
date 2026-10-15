package com.ilyassavli.helloworld.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Spacing tokens (RES-02). Features use `HelloWorldTheme.spacing.medium` instead of literal dp values. */
@Immutable
data class HelloWorldSpacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    val minTouchTarget: Dp = 48.dp,
)

internal val LocalHelloWorldSpacing = staticCompositionLocalOf { HelloWorldSpacing() }
