package com.example.democoinbag

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.LinkedList
import java.util.Random
import kotlin.math.pow

@Composable
fun CoinBagShower() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val config = LocalConfiguration.current
        val context = LocalContext.current
        val maxWidth = config.screenWidthDp.toFloat().convertDpToPx(context) / 2
        val maxHeight = 700F
        var progress by remember { mutableStateOf(0F) }
        var generated by remember { mutableStateOf(0F) }
//        val coins = List(10) {
//            Coin(
//                type = listOf(true, false).random(),
//                maxHorizontalDisplacement = maxWidth * randomInRange(-0.9F, 0.9F),
//                id = it,
//                maxVerticalDisplacement = maxHeight,
//                startX = maxWidth.toInt(),
//                startY = 0
//            )
//        }
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(600.dp)
//
//                .background(Color.Red.copy(alpha = 0.3F)),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Canvas(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight()
//            ) {
//                coins.take((generated * coins.size).toInt()).forEach {
//                    CoinPath(progress, drawScope = this, coin = it)
//                }
//            }
//        }

//        Slider(
//            modifier = Modifier.width(300.dp),
//            value = progress, onValueChange = { progress = it })
//
//        Slider(
//            modifier = Modifier.width(300.dp),
//            value = generated, onValueChange = { generated = it })
        val queue = LinkedList<Coin>().apply {
            addAll(
                List(1500) {
                    Coin(
                        type = listOf(true, false).random(),
                        maxHorizontalDisplacement = maxWidth * randomInRange(-0.9F, 0.9F),
                        id = it,
                        maxVerticalDisplacement = maxHeight,
                        startX = maxWidth.toInt(),
                        startY = 0,
                        iconUrl = "https://cdn.sharechat.com/react-native-assets/virtual-gifting/coin-active.png"
                    )
                }
            )
        }
        StartGeneration(coins = queue)
    }
}


fun CoinPath(progress: Float, drawScope: DrawScope, coin: Coin) {
    drawScope.apply {
        drawCircle(
            color = if (coin.type) Color.Green else Color.Red,
            radius = 28F,
            center = Offset(coin.currentX, coin.currentY)
        )
    }
    coin.updateCentre(progress)
}

@Composable
fun Shower(progress: Float) {
    val config = LocalConfiguration.current
    val context = LocalContext.current
    val maxWidth = config.screenWidthDp.toFloat().convertDpToPx(context) / 2
    val maxHeight = 700F
    val velocity = 2 * maxHeight
    val acceleration = (-2) * velocity
    val verticalDisp = 0.5 * acceleration * progress.pow(2)

    val particles = remember {
        List(10) {
            Coin(
                type = listOf(true, false).random(),
                maxHorizontalDisplacement = maxWidth * randomInRange(-0.9F, 0.9F),
                id = it,
                maxVerticalDisplacement = maxHeight,
                startX = maxWidth.toInt(),
                startY = 0,
                iconUrl = "https://cdn.sharechat.com/react-native-assets/virtual-gifting/coin-active.png"
            )
        }
    }

    particles.forEach {
        it.updateCentre(progress)
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        drawLine(
            color = Color.Black,
            start = Offset(config.screenWidthDp.toFloat().convertDpToPx(context) / 2, 0F),
            end = Offset(config.screenWidthDp.toFloat().convertDpToPx(context) / 2 + maxWidth, 0F),
            strokeWidth = 4F
        )

        particles.forEach {
            drawCircle(
                color = if (it.type) Color.Green else Color.Red,
                radius = 28F,
                center = Offset(it.currentX, it.currentY)
            )
        }

//        drawCircle(
//            color = Color.Red,
//            radius = 54F,
//            center = Offset(
//                x = config.screenWidthDp.toFloat()
//                    .convertDpToPx(context) / 2 + progress * maxWidth * horizontalDisplacementMultiplier,
//                y = (-verticalDisp).toFloat()
//            )
//        )
    }
}

fun Float.convertDpToPx(context: Context): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return this * (metrics.densityDpi / 160F)
}

data class Coin(
    val type: Boolean,
    val iconUrl: String,
    val maxHorizontalDisplacement: Float,
    val maxVerticalDisplacement: Float,
    val id: Int,
    val startX: Int,
    val startY: Int
) {
    private val velocity = maxVerticalDisplacement * 2
    private val acceleration = velocity * (2)
    var currentX = 0F
    var currentY = 0F
    fun updateCentre(progress: Float) {
        val verticalDisp = 0.5 * acceleration * progress.pow(2)
        currentX = startX + maxHorizontalDisplacement * progress
        currentY = startY + verticalDisp.toFloat()
    }
}

private val random = Random()
fun Float.randomTillZero() = this * random.nextFloat()
fun randomInRange(min: Float, max: Float) = min + (max - min).randomTillZero()