package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = RoseBlossom,
    onPrimary = CanvasDark,
    primaryContainer = CardDark,
    onPrimaryContainer = SoftRosePetal,
    secondary = RadiantGoldLight,
    onSecondary = CanvasDark,
    secondaryContainer = CardHighDark,
    onSecondaryContainer = RadiantGoldLight,
    tertiary = SereneSageLight,
    onTertiary = Color.White,
    background = CanvasDark,
    onBackground = TextPrimaryDark,
    surface = CardDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = CardHighDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = VelvetWine,
    onPrimary = Color.White,
    primaryContainer = SoftRosePetal,
    onPrimaryContainer = VelvetWine,
    secondary = RadiantGold,
    onSecondary = Color.White,
    secondaryContainer = RadiantGoldContainer,
    onSecondaryContainer = VelvetWine,
    tertiary = SereneSage,
    onTertiary = Color.White,
    background = CanvasLight,
    onBackground = TextPrimaryLight,
    surface = CardPearlWhite,
    onSurface = TextPrimaryLight,
    surfaceVariant = CardTintedLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderRoseSubtle
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep intentional devotional aesthetic
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
