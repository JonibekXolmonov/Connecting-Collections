package com.cc.connectingcollection.utils.model

import java.util.UUID

data class MyOffset(
    val id: UUID = UUID.randomUUID(),
    val x: Float,
    val y: Float,
    var isFree: Boolean = true
)