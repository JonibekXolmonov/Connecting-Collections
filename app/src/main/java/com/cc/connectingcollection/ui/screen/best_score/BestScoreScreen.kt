package com.cc.connectingcollection.ui.screen.best_score

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.cc.connectingcollection.R
import com.cc.connectingcollection.utils.common.BackgroundImage
import com.cc.connectingcollection.utils.noRippleClickable

@Composable
fun BestScoreScreen(
    viewModel: BestScoreViewModel = hiltViewModel(),
    onBack: () -> Unit
) {

    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val topThree by viewModel.topScores.collectAsState(listOf(0, 0, 0))

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        BackgroundImage()

        Image(
            painter = painterResource(id = R.drawable.menu),
            contentDescription = "menu",
            modifier = Modifier
                .padding(start = 20.dp, top = 24.dp)
                .noRippleClickable {
                    onBack()
                }
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(start = 40.dp)
                    .size(
                        width = screenWidth - 250.dp,
                        height = screenHeight - 20.dp
                    )
            ) {
                val (stage, s1, s2, s3) = createRefs()

                Image(
                    painter = painterResource(id = R.drawable.best_score_stage),
                    contentDescription = "stage",
                    modifier = Modifier
                        .constrainAs(stage) {}
                        .fillMaxSize()
                )

                Text(
                    text = topThree[1].toString(), modifier = Modifier.constrainAs(s2) {
                        start.linkTo(parent.start, margin = 48.dp)
                        bottom.linkTo(parent.bottom, margin = 64.dp)
                    },
                    style = TextStyle(
                        fontSize = 36.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(
                            Font(R.font.hansans_regular)
                        )
                    )
                )

                Text(
                    text = topThree[0].toString(), modifier = Modifier.constrainAs(s1) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end, 48.dp)
                        bottom.linkTo(parent.bottom, 78.dp)
                    },
                    style = TextStyle(
                        fontSize = 48.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(
                            Font(R.font.hansans_regular)
                        )
                    )
                )

                Text(
                    text = topThree[2].toString(), modifier = Modifier.constrainAs(s3) {
                        end.linkTo(parent.end, margin = 120.dp)
                        bottom.linkTo(parent.bottom, margin = 44.dp)
                    },
                    style = TextStyle(
                        fontSize = 36.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(
                            Font(R.font.hansans_regular)
                        )
                    )
                )
            }
        }
    }
}