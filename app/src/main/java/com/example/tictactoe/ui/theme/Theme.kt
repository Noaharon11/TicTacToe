package com.example.tictactoe.ui.theme

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
    primary = Color(0xFFE91E63), // ורוד כהה
    secondary = Color(0xFFF48FB1), // ורוד בהיר
    tertiary = Color(0xFFFFC1E3) // ורוד נוסף
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE91E63), // ורוד כהה
    secondary = Color(0xFFF48FB1), // ורוד בהיר
    tertiary = Color(0xFFFFC1E3), // ורוד נוסף
    background = Color(0xFFFFEBEE), // רקע ורוד בהיר
    surface = Color(0xFFFCE4EC), // רקע הכפתורים
    onPrimary = Color.White,
    onSecondary = Color.Black
)

@Composable
fun TicTacToeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
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
