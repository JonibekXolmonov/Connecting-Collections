package com.cc.connectingcollection.ui.screen.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cc.connectingcollection.R
import com.cc.connectingcollection.ui.theme.BlueGradient
import com.cc.connectingcollection.utils.common.BackgroundImage
import com.cc.connectingcollection.utils.noRippleClickable

@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    onVibrate: () -> Unit,
    onSound: (Boolean) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    val vibrationEnabled by remember {
        viewModel.vibrationEnabled
    }

    val soundEnabled by remember {
        viewModel.soundEnabled
    }

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundImage()

        Image(
            painter = painterResource(id = R.drawable.menu),
            contentDescription = "menu",
            modifier = Modifier
                .padding(start = 20.dp, top = 24.dp)
                .noRippleClickable { onBackPressed() }

        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Sound(state = soundEnabled, onStateChange = {
                    viewModel.controlSound(it)
                    onVibrate()
                    onSound(soundEnabled)
                })

                Vibration(state = vibrationEnabled, onStateChange = {
                    viewModel.controlVibration(it)
                    onVibrate()
                })
            }
        }
    }
}

@Composable
fun Sound(state: Boolean, onStateChange: (Boolean) -> Unit) {
    OnOff(
        contentTextId = R.string.sound,
        imageId = if (state) R.drawable.on else R.drawable.off,
        state = state,
        onStateChange = onStateChange
    )
}

@Composable
fun Vibration(state: Boolean, onStateChange: (Boolean) -> Unit) {
    OnOff(
        contentTextId = R.string.vibration,
        imageId = if (state) R.drawable.on else R.drawable.off,
        state = state,
        onStateChange = onStateChange
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun OnOff(
    @StringRes contentTextId: Int,
    @DrawableRes imageId: Int,
    state: Boolean,
    onStateChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Text(
            text = stringResource(id = contentTextId),
            style = TextStyle(
                brush = BlueGradient,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(
                    Font(R.font.hansans_regular)
                ),
                fontSize = 48.sp
            ),
        )

        Image(
            painter = painterResource(id = imageId),
            contentDescription = "image",
            modifier = Modifier.noRippleClickable {
                onStateChange(!state)
            })
    }
}