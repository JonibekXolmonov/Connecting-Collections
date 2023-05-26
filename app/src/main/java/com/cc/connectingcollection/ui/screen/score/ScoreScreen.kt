package com.cc.connectingcollection.ui.screen.score

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cc.connectingcollection.R
import com.cc.connectingcollection.ui.theme.ScreenBack
import com.cc.connectingcollection.utils.noRippleClickable

@Composable
fun ScoreDialog(
    score: Int,
    onBack: () -> Unit
) {

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        val context = LocalContext.current
        val windowManager =
            remember { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

        val metrics = DisplayMetrics().apply {
            windowManager.defaultDisplay.getRealMetrics(this)
        }
        val (width, height) = with(LocalDensity.current) {
            Pair(metrics.widthPixels.toDp(), (metrics.heightPixels).toDp())
        }

        ScoreScreen(
            width,
            height,
            score,
            onBack
        )
    }
}

@Composable
fun ScoreScreen(
    width: Dp,
    height: Dp,
    score: Int,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .requiredSize(width, height)
            .background(ScreenBack)
    ) {

        Image(
            painter = painterResource(id = R.drawable.menu),
            contentDescription = "menu",
            modifier = Modifier
                .padding(24.dp)
                .noRippleClickable {
                    onBack()
                }
        )

        Box(
            modifier = Modifier.align(Alignment.Center),
            contentAlignment = Alignment.BottomCenter
        ) {

            Image(
                painter = painterResource(id = R.drawable.stage_image),
                contentDescription = "stage",
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )

            Text(
                text = score.toString(),
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.hansans_regular)),
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 86.dp)
            )
        }
    }
}