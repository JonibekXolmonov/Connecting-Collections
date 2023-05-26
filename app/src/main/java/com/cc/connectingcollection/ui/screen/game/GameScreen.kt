package com.cc.connectingcollection.ui.screen.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.cc.connectingcollection.R
import com.cc.connectingcollection.ui.screen.score.ScoreDialog
import com.cc.connectingcollection.ui.theme.*
import com.cc.connectingcollection.utils.Handlers
import com.cc.connectingcollection.utils.noRippleClickable

@Composable
fun GameScreen(
    onBack: () -> Unit,
    onVibrate: () -> Unit,
    gameViewModel: GameViewModel = hiltViewModel()
) {

    val score by gameViewModel.score.collectAsState(initial = 0)
    var isGameFinish by remember { mutableStateOf(false) }

    if (isGameFinish)
        ScoreDialog(score = score) {
            isGameFinish = false
            onVibrate()
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBack)
    ) {
        UI(gameViewModel, score, onBack = onBack)
    }
}

@Composable
fun UI(viewModel: GameViewModel, score: Int, onBack: () -> Unit) {

    val shapes by viewModel.shapes.collectAsState(initial = emptyList())
    val lines by viewModel.lines.collectAsState(initial = emptyList())

    val handlers = remember(viewModel) {
        Handlers(
            onAddShape = { viewModel.add(it) },
            onToolChange = { },
            onDragStart = { finger, size -> viewModel.startDrag(finger, size) },
            onDrag = { offset -> viewModel.drag(offset) },
            onDragEnd = { viewModel.endDrag() },
            onLineStart = { finger, size ->
                viewModel.startLine(finger, size)
            },
            onLineEnd = { finger, size ->
                viewModel.endLine(finger, size)
            }
        )
    }

    Row(modifier = Modifier.fillMaxSize()) {

        BackScoreAddOutOfTurn(
            onBack = onBack,
            addOutOfTurnBall = viewModel::addPuckOutOffTurn,
            score = score
        )


        Graph(
            shapeSizeDp = 36.dp,
            shapeOutlineWidthDp = 3.dp,
            shapeBoxSizeDp = 48.dp,
            shapes = shapes,
            lines = lines,
            selectedTool = Select,
            handlers = handlers,
            modifier = Modifier
                .fillMaxSize()
        ) { maxOffset ->
            viewModel.generateBallAppearancePositions(maxOffset)
        }
    }

}

@Composable
fun Graph(
    modifier: Modifier,
    shapeSizeDp: Dp,
    shapeOutlineWidthDp: Dp,
    shapeBoxSizeDp: Dp,
    shapes: List<Shape>,
    lines: List<Line>,
    selectedTool: ToolType,
    handlers: Handlers,
    context: Context = LocalContext.current,
    onMaxOffsetFound: (Offset) -> Unit
) {
    with(LocalDensity.current) {
        val shapeSizePx = shapeSizeDp.toPx()
        val shapeOutlineWidthPx = shapeOutlineWidthDp.toPx()
        val shapeBoxSizePx = shapeBoxSizeDp.toPx()
        val shapeOffsetPx = (shapeBoxSizePx - shapeSizePx) / 2
        val halfShapeBoxOffset = remember(shapeBoxSizePx) {
            Offset(shapeBoxSizePx / 2, shapeBoxSizePx / 2)
        }

        var finger by remember { mutableStateOf(Offset.Zero) }

        fun Shape.getCenter() = offset + halfShapeBoxOffset

        Box(modifier = modifier) {
            Canvas(
                modifier = modifier
                    .pointerInput(selectedTool) {

                        detectTapDragGestures(
                            onDragStart = { offset ->
                                handlers.onDragStart(offset, shapeBoxSizePx)
                            },
                            onDrag = { change, _ ->
                                handlers.onDrag(change.position)
                                finger = change.position
                            },
                            onDragEnd = {
                                handlers.onDragEnd()
//                                handlers.onLineEnd(finger,shapeBoxSizePx)
                            },
                            onDragCancel = {
                                handlers.onDragEnd()
                            }
                        )

//                        detectTapDragGestures(
//                            onDragStart = {
//                                handlers.onLineStart(it, shapeBoxSizePx)
//                            },
//                            onDrag = { change, _ ->
//                                finger = change.position
//                            },
//                            onDragEnd = { handlers.onLineEnd(finger, shapeBoxSizePx) },
//                            onDragCancel = {
//                                handlers.onLineEnd(
//                                    finger,
//                                    shapeBoxSizePx
//                                )
//                            }
//                        )
                    }
            ) {

                lines.forEach { line ->
                    drawLine(
                        color = Color.Transparent, strokeWidth = shapeOutlineWidthPx,
                        start = shapes.findWithId(line.shape1Id)!!.getCenter(),
                        end = line.shape2Id?.let { shapes.findWithId(it)!! }?.getCenter()
                            ?: finger
                    )
                }

                shapes.forEach {
                    translate(
                        it.offset.x + shapeOffsetPx,
                        it.offset.y + shapeOffsetPx
                    ) {
                        it.resId?.let {
                            drawImage(image = getBitMap(id = it, context = context))
                        }
                    }
                }
            }

            Box(modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(50.dp)
                .onGloballyPositioned {
                    val maxOffset = Offset(
                        it.positionInParent().x,
                        it.positionInParent().y
                    )
                    onMaxOffsetFound(maxOffset)
                })
        }
    }
}

private fun getBitMap(context: Context, @DrawableRes id: Int): ImageBitmap {
    val db = ContextCompat.getDrawable(context, id)
    val bit = Bitmap.createBitmap(
        db!!.intrinsicWidth, db.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bit)
    db.setBounds(0, 0, canvas.width, canvas.height)
    db.draw(canvas)
    return bit.asImageBitmap()
}

fun List<Shape>.findWithId(id: String) =
    firstOrNull { it.id == id }

suspend fun PointerInputScope.detectTapDragGestures(
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit = { _, _ -> }
) {
    forEachGesture {
        awaitPointerEventScope {
            val down = awaitFirstDown(requireUnconsumed = false)
            var drag: PointerInputChange?
            var overSlop = Offset.Zero
            do {
                drag = awaitTouchSlopOrCancellation(down.id) { change, over ->
                    change.consumePositionChange()
                    overSlop = over
                }
            } while (drag != null && !drag.positionChangeConsumed())
            if (drag != null) {
                onDragStart.invoke(down.position) // CHANGED TO down instead of drag
                onDrag(drag, overSlop)
                if (
                    !drag(drag.id) {
                        onDrag(it, it.positionChange())
                        it.consumePositionChange()
                    }
                ) {
                    onDragCancel()
                } else {
                    onDragEnd()
                }
            }
        }
    }
}

@Composable
fun BackScoreAddOutOfTurn(onBack: () -> Unit, addOutOfTurnBall: () -> Unit, score: Int) {
    Row(
        modifier = Modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(top = 18.dp, start = 4.dp, bottom = 3.dp)
                .width(125.dp)
                .fillMaxHeight()
        ) {

            Image(
                painter = painterResource(id = R.drawable.replay),
                contentDescription = "replay",
                modifier = Modifier
                    .noRippleClickable {
                        onBack()
                    }
            )

            Box(
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = score.toString(),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 48.sp,
                        fontFamily = FontFamily(Font(R.font.hansans_regular)),
                        textAlign = TextAlign.Center
                    )
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .clickable { addOutOfTurnBall() }
                    .padding(horizontal = 4.dp, vertical = 4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_shayba),
                        contentDescription = "puck"
                    )
                    Image(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = "plus"
                    )
                }
            }
        }
    }
}



