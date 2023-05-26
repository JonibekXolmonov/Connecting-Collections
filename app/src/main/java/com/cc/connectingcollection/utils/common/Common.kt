package com.cc.connectingcollection.utils.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.cc.connectingcollection.R

@Composable
fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.main_background),
        contentDescription = "menu_back",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}