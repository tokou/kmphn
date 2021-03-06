package com.github.tokou.common.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.tokou.common.platform.Font

@Composable
fun sourceSansPro() = FontFamily(
    Font(
        name = "Sources Sans Pro Regular",
        res = "sourcesanspro_regular",
        weight = FontWeight.W400,
        style = FontStyle.Normal
    ),
    Font(
        name = "Sources Sans Pro Light",
        res = "sourcesanspro_light",
        weight = FontWeight.W300,
        style = FontStyle.Normal
    ),
)

@Composable
fun AppTypography() = Typography(
    defaultFontFamily = sourceSansPro(),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.15.sp
    )
)
