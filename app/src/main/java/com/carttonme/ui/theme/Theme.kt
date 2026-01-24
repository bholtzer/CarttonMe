package com.carttonme.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = SmurfBlue,
    secondary = SmurfGold,
    surface = Color.White,
    background = SmurfGray
)

private val DarkColors = darkColorScheme(
    primary = SmurfBlue,
    secondary = SmurfGold
)

@Composable
fun CarttonMeTheme(
    useDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
