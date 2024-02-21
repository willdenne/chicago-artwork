package will.denne.artwork.ui.shared.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import will.denne.artwork.R

private val LightColors = lightColorScheme(
    primary = LightBlue,
    secondary = BackgroundBlue,
    background = BackgroundBlue,
    error = Red
)

private val DarkColors = darkColorScheme(
    primary = Black,
    secondary = DarkGray,
    background = DarkGray,
    error = Red
)

val latoFontFamily = FontFamily(
    Font(R.font.lato_regular),
    Font(R.font.lato_bold, FontWeight.Bold),
    Font(resId = R.font.lato_italic, style = FontStyle.Italic)
)

@Composable
fun Theme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colors = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (useDarkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        useDarkTheme -> DarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = useDarkTheme
        }
    }

    val chicagoArtTypography = Typography(
        labelSmall = typography.labelSmall.copy(fontFamily = latoFontFamily),
        labelMedium = typography.labelMedium.copy(fontFamily = latoFontFamily),
        labelLarge = typography.labelLarge.copy(fontFamily = latoFontFamily),
        bodySmall = typography.bodySmall.copy(fontFamily = latoFontFamily),
        bodyMedium = typography.bodyMedium.copy(fontFamily = latoFontFamily),
        bodyLarge = typography.bodyLarge.copy(fontFamily = latoFontFamily),
        headlineLarge = typography.headlineLarge.copy(fontFamily = latoFontFamily),
        headlineMedium = typography.headlineMedium.copy(fontFamily = latoFontFamily),
        headlineSmall = typography.headlineSmall.copy(fontFamily = latoFontFamily)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = chicagoArtTypography,
        shapes = shapes,
        content = content
    )
}
