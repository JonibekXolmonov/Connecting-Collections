package com.cc.connectingcollection.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.cc.connectingcollection.R
import java.util.*

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

sealed interface ToolType
object DrawLine : ToolType
object Select : ToolType
sealed interface ShapeType : ToolType
object Circle : ShapeType
object Square : ShapeType
object Triangle : ShapeType
object Image : ShapeType

@Immutable
data class Shape(
    val id: String = UUID.randomUUID().toString(),
    var resId: Int? = R.drawable.puck,
    val shapeType: ShapeType = Image,
    val offset: Offset
)

@Immutable
data class Line(
    val shape1Id: String,
    val shape2Id: String? = null
)