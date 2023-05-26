package com.cc.connectingcollection.utils.model

import androidx.annotation.DrawableRes
import com.cc.connectingcollection.R

data class GameBall(
    @DrawableRes val id: Int = R.drawable.puck,
    val name: String = "puck"
)