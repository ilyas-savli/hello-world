package com.ilyassavli.helloworld.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Brand tokens. Replace these with the values from the design source (Figma variables or
// scripts/extract_palette.py for PNGs) during the designsystem task — nowhere else may define colors.
private val Primary = Color(0xFF3B5BA9)
private val OnPrimary = Color(0xFFFFFFFF)
private val PrimaryDark = Color(0xFFB0C6FF)
private val OnPrimaryDark = Color(0xFF00296B)
private val Secondary = Color(0xFF575E71)
private val SecondaryDark = Color(0xFFBFC6DC)
private val Background = Color(0xFFFAF9FD)
private val BackgroundDark = Color(0xFF121318)

// Container surfaces (dialogs, menus, date picker, cards). Without them Material 3 keeps its baseline lavender tint
// whatever the brand colors are — set them from the design's neutral surfaces.
private val SurfaceContainerLowest = Color(0xFFFFFFFF)
private val SurfaceContainerLow = Color(0xFFF7F7F9)
private val SurfaceContainer = Color(0xFFF1F1F4)
private val SurfaceContainerHigh = Color(0xFFECECEF)
private val SurfaceContainerHighest = Color(0xFFE6E6E9)
private val SurfaceContainerLowestDark = Color(0xFF0D0E12)
private val SurfaceContainerLowDark = Color(0xFF1A1B20)
private val SurfaceContainerDark = Color(0xFF1E1F24)
private val SurfaceContainerHighDark = Color(0xFF292A2F)
private val SurfaceContainerHighestDark = Color(0xFF34353A)
private val Error = Color(0xFFBA1A1A)
private val ErrorDark = Color(0xFFFFB4AB)

internal val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    background = Background,
    surface = Background,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    error = Error,
)

internal val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    secondary = SecondaryDark,
    background = BackgroundDark,
    surface = BackgroundDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark,
    error = ErrorDark,
)
