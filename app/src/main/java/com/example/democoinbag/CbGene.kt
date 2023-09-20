package com.example.democoinbag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import java.util.Queue

@Composable
fun StartGeneration(coins: Queue<Coin>) {
    val config = LocalConfiguration.current
    val context = LocalContext.current
    val maxWidth = config.screenWidthDp.toFloat().convertDpToPx(context) / 2
    val maxHeight = 700F
    var queueSize by remember {
        mutableStateOf(coins.size)
    }
    var progress by remember { mutableStateOf(0F) }
    val list = remember { mutableStateListOf<Coin>() }
    LaunchedEffect(queueSize) {
        if (queueSize > 0) {
            delay(100)
            list.clear()
            repeat(5) {
                val coin = coins.poll()
                coin?.let { foundCoin ->
                    list.add(foundCoin)
                } ?: return@repeat
            }
            queueSize -= 5
        }
    }
    Box(modifier = Modifier.fillMaxSize().background(Color.Blue.copy(alpha = 0.55F))) {
        if (queueSize > 0) {
            list.forEach {
                ThrowCoin(coin = it)
            }
        }
    }
//    Canvas(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(700.dp)
//            .background(Color.DarkGray.copy(alpha = 0.4F))
//    ) {
//        drawLine(
//            color = Color.Black,
//            start = Offset(0F, 0F),
//            end = Offset(maxWidth * 2, 0F)
//        )
//        if (queueSize > 0 || true) {
//            list.forEach { coin ->
//                drawCircle(
//                    color = if (coin.type) Color.Green else Color.Red,
//                    radius = 28F,
//                    center = Offset(coin.currentX, coin.currentY)
//                )
//            }
//        }
//    }
}

@Composable
fun ThrowCoin(coin: Coin) {
    var progress by remember { mutableStateOf(0F) }

    LaunchedEffect(key1 = progress) {
        coin.updateCentre(progress.coerceAtMost(1F))
        delay(30)
        progress += 0.01F
    }

    println("offset xId: ${coin.id}" + "offsetX: " + coin.currentX + ",OffsetY: " + coin.currentY)
    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(Color.Gray)
//            .offset(x = coin.currentX.dp, y = coin.currentY.dp)
    ) {
        AsyncImage(model = coin.iconUrl, contentDescription = "coin_icon", modifier = Modifier.size(20.dp))
    }


//    Canvas(modifier = Modifier.background(Color.Blue)) {
//        drawCircle(
//            color = if (coin.type) Color.Green else Color.Red,
//            radius = 28F,
//            center = Offset(coin.currentX, coin.currentY)
//        )
//    }
}