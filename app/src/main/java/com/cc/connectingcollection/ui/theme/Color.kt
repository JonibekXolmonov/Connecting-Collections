package com.cc.connectingcollection.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val BlueTop = Color(0xFF77B8F1)
val BlueBottom = Color(0xFF4E96D7)
val BlueScreenTop = Color(0xFFE1F5BF)
val BlueScreenBottom = Color(0xFFA2E7F4)

val BlueGradient = Brush.verticalGradient(
    colors = listOf(
        BlueTop,
        BlueBottom
    )
)

val ScreenBack = Brush.verticalGradient(
    colors = listOf(
        BlueScreenTop,
        BlueScreenBottom
    )
)

