package ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TerminalTheme(
    appTheme: AppTheme,
    content: @Composable () -> Unit
) {
    val colors = if (appTheme == AppTheme.DARK) {
        darkColors(
            primary = Color(0xFF0043ce), // selektiert Text
            primaryVariant = Color(0xFF0f62fe), // selektiert Icon
            onPrimary = Color(0xFFffffff), // normaler Text
            secondary = Color(0xFF005d5d), // nicht selektiert
            secondaryVariant = Color(0xFF007d79), // nicht selektiert
            onSecondary = Color(0xFFffffff),
            background = Color(0xFFffffff),
            surface = Color(0xFFffffff), // Buttonfarbe
            onBackground = Color(0xFF161616), // Button farbe
            onSurface = Color(0xFF161616),
            error = Color(0xFFda1e28),
            onError = Color(0xFFffffff)
        )
    } else {
        lightColors(
            primary = Color(0xFF0043ce), // selektiert Text
            primaryVariant = Color(0xFF36557), // selektiert Icon
            onPrimary = Color(0xFF000000), // normaler Text
            secondary = Color(0xFF005d5d), // nicht selektiert
            secondaryVariant = Color(0xFFBFF29B), // nicht selektiert
            onSecondary = Color(0xFF000000),
            background = Color(0xFFffffff),
            surface = Color(0xFFffffff), // Buttonfarbe
            onBackground = Color(0xFF000000), // Button farbe
            onSurface = Color(0xFF000000),
            error = Color(0xFFEA361F),
            onError = Color(0xFFffffff)
        )
    }


    val ibmCarbonFont = FontFamily(
        Font("font/IBMPlexSans-Bold.ttf", weight = FontWeight.Bold, style = FontStyle.Normal),
        Font("font/IBMPlexSans-Italic.ttf", style = FontStyle.Italic),
        Font("font/IBMPlexSans-Regular.ttf", style = FontStyle.Normal)
    )

    val typography = Typography(
        h1 = TextStyle(
            fontFamily = ibmCarbonFont,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        ),
        body1 = TextStyle(
            fontFamily = ibmCarbonFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )

    val shapes = Shapes(
        small = RoundedCornerShape(30.dp),
        medium = RoundedCornerShape(10.dp),
        large = RoundedCornerShape(0.dp)
    )

    CompositionLocalProvider(LocalSpacing provides Spacing(), LocalFontSize provides FontSize()) {
        MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }

}