package com.cc.connectingcollection.ui.screen.game

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc.connectingcollection.R
import com.cc.connectingcollection.ui.theme.Line
import com.cc.connectingcollection.ui.theme.Shape
import com.cc.connectingcollection.utils.Contants
import com.cc.connectingcollection.utils.SharedPref
import com.cc.connectingcollection.utils.model.MyOffset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(private val sharedPref: SharedPref) : ViewModel() {

    private val _score = MutableStateFlow(0)
    val score get() = _score

    private val _prizePuckLocation = MutableStateFlow(Offset(0f, 0f))

    private val _shapes = MutableStateFlow<List<Shape>>(emptyList())
    val shapes: Flow<List<Shape>>
        get() = _shapes

    private val _lines = MutableStateFlow<List<Line>>(emptyList())
    val lines: Flow<List<Line>>
        get() = _lines

    private val _positions = MutableStateFlow<List<MyOffset>>(emptyList())

    private var job: Job? = null

    private var _list = arrayOf(0, 0, 0, 0)

    fun add(shape: Shape) {
        _shapes.value += shape
    }

    fun saveScore(score: Int) {
        viewModelScope.launch {
            val scores = getScores()
            scores[3] = score
            scores.sortDescending()

            scores.forEachIndexed { index, value ->
                when (index) {
                    0 -> sharedPref.saveInt(Contants.FIRST, value)
                    1 -> sharedPref.saveInt(Contants.SECOND, value)
                    2 -> sharedPref.saveInt(Contants.THIRD, value)
                }
            }
        }
    }

    private fun getScores(): Array<Int> {
        _list[0] = (sharedPref.getInt(Contants.FIRST))
        _list[1] = (sharedPref.getInt(Contants.SECOND))
        _list[2] = (sharedPref.getInt(Contants.THIRD))
        return _list
    }

    private fun startGame() {
        packLocationGenerator()
    }

    private fun packLocationGenerator() {
        job = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(2000)
                val position = getPositionToGenerate()
                if (position.x != 0f && position.y != 0f)
                    add(Shape(offset = position))
            }
        }
    }

    fun finishGame() {
        job?.cancel()
    }

    fun addScore(score: Int) {
        _score.value += score
    }

    fun addPuckOutOffTurn() {
        viewModelScope.launch {
            _prizePuckLocation.value =
                Offset(Random.nextInt(100, 900).toFloat(), Random.nextInt(100, 450).toFloat())
            add(Shape(offset = _prizePuckLocation.value))
        }
    }

    fun generateBallAppearancePositions(maxOffset: Offset) {
        if (_positions.value.isEmpty())
            viewModelScope.launch {
                val x = maxOffset.x
                val y = maxOffset.y
                repeat(3) { column ->
                    repeat(4) { row ->
                        _positions.value += MyOffset(
                            x = x / 8 + row * x / 4,
                            y = y / 6 + column * y / 3
                        )
                    }
                }
            }
    }

    private fun getPositionToGenerate() =
        try {
            val myOffset = _positions.value.filter { it.isFree }.random()
            changeStatus(myOffset)
            Offset(myOffset.x, myOffset.y)
        } catch (e: Exception) {
            Offset(0f, 0f)
        }

    private fun changeStatus(myOffset: MyOffset) {
        viewModelScope.launch {
            _positions.value.forEach {
                if (it.id == myOffset.id) {
                    it.isFree = false
                }
            }
        }
    }

    private var dragShape: Shape? = null
    private var dragShapeOffset: Offset = Offset.Zero

    fun startDrag(finger: Offset, size: Float) {
        dragShape = _shapes.value.findAt(finger, size)?.apply {
            dragShapeOffset = finger - offset
        }
        _shapes.value.findAt(finger, size)?.let {

        }
    }

    fun drag(offset: Offset) {
        dragShape?.let { shape ->
            val newShape = shape.copy(offset = offset - dragShapeOffset)
            _shapes.value = _shapes.value - shape + newShape
            dragShape = newShape
        }
    }

    fun endDrag() {
        dragShape = null
    }

    private var lineInProgress: Line? = null
    fun startLine(finger: Offset, size: Float) {
        _shapes.value.findAt(finger, size)?.let { shape ->
            lineInProgress = Line(shape.id).apply {
                _lines.value = _lines.value + this
            }
        }
    }

    fun endLine(finger: Offset, size: Float) {
        lineInProgress?.let { line ->
            _shapes.value.findAt(finger, size)?.let { endShape ->
                changeImage(endShape, line.shape1Id)
                _lines.value = _lines.value - line + line.copy(shape2Id = endShape.id)
            } ?: run {
                _lines.value = _lines.value - line
            }
            lineInProgress = null
        }
    }

    private fun changeImage(endShape: Shape, shape1Id: String) {
        _shapes.value.find { it.id == endShape.id }?.apply {
            resId = resId?.let { up(it) }
        }
        _shapes.value
    }

    private fun up(resId: Int): Int {
        return listOf(
            R.drawable.basketball_ball,
            R.drawable.football_bal,
            R.drawable.soccer_american_ball,
            R.drawable.basketball_ball,
            R.drawable.bowling_ball
        ).random()
    }

    private fun List<Shape>.findAt(offset: Offset, shapeBoxSizePx: Float): Shape? =
        reversed().find { shape ->
            val normalized = offset - shape.offset
            normalized.x >= 0 &&
                    normalized.y >= 0 &&
                    normalized.x <= shapeBoxSizePx &&
                    normalized.y <= shapeBoxSizePx
        }

    init {
        viewModelScope.launch {
            startGame()
        }
    }
}