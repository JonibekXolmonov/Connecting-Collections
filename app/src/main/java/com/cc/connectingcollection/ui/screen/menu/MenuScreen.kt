package com.cc.connectingcollection.ui.screen.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cc.connectingcollection.R
import com.cc.connectingcollection.utils.common.BackgroundImage
import com.cc.connectingcollection.utils.noRippleClickable

@Composable
fun MenuScreen(
    onGameStart: () -> Unit, onSettingsClick: () -> Unit, onBestScore: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundImage()

        Row(
            modifier = Modifier.padding(24.dp).fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.best_score),
                contentDescription = "start",
                modifier = Modifier.noRippleClickable {
                    onBestScore()
                }
            )
            Image(
                painter = painterResource(id = R.drawable.start_game),
                contentDescription = "start",
                modifier = Modifier
                    .padding(top = 50.dp)
                    .noRippleClickable { onGameStart() }
            )
            Image(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "start",
                modifier = Modifier
                    .padding(bottom = 120.dp)
                    .noRippleClickable {
                        onSettingsClick()
                    }
            )
        }
    }
}