package com.cc.connectingcollection.utils

import androidx.compose.ui.geometry.Offset
import com.cc.connectingcollection.ui.theme.Shape
import com.cc.connectingcollection.ui.theme.ToolType

data class Handlers(
    val onAddShape: (Shape) -> Unit,
    val onToolChange: (ToolType) -> Unit,
    val onDragStart: (finger: Offset, size: Float) -> Unit,
    val onDrag: (Offset) -> Unit,
    val onDragEnd: () -> Unit,
    val onLineStart: (finger: Offset, size: Float) -> Unit,
    val onLineEnd: (finger: Offset, size: Float) -> Unit,
)