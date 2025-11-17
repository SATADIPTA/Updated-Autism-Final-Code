package com.example.firebase.pages

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.R
import java.util.*

data class Fruit(val name: String, val resId: Int)

@Composable
fun FruitsGame(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(0) }
    var gameFinished by remember { mutableStateOf(false) }

    // Shuffle fruits once and remember
    val fullFruitList = remember {
        listOf(
            Fruit("Apple", R.drawable.apple),
            Fruit("Banana", R.drawable.banana),
            Fruit("Grape", R.drawable.grape),
            Fruit("Mango", R.drawable.mango),
            Fruit("Orange", R.drawable.orange),
            Fruit("Pineapple", R.drawable.pineapple),
            Fruit("Watermelon", R.drawable.watermellon),
            Fruit("Pomegranate", R.drawable.pomegranate),
            Fruit("Coconut", R.drawable.coconut),
            Fruit("Strawberry", R.drawable.strawberry),
            Fruit("Lychee", R.drawable.lychee),
            Fruit("Blueberry", R.drawable.blueberry)
        ).shuffled()
    }

    val tts = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
            }
        }
    }

    DisposableEffect(Unit) {
        tts.language = Locale.US
        tts.setSpeechRate(0.9f)
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) {
                if (utteranceId == "correct") {
                    if (currentIndex < fullFruitList.size - 1) {
                        currentIndex += 1
                    } else {
                        gameFinished = true
                    }
                }
            }

            override fun onError(utteranceId: String?) {}
        })

        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun speak(text: String, id: String? = null) {
        if (ttsReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, id)
        }
    }

    if (gameFinished) {
        LaunchedEffect(Unit) {
            speak("Congratulations! You have completed the game. Great job!")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFECB3))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎉 Congratulations! 🎉",
                color = Color(0xFFBF360C),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "🍇 You have completed the fruit game! 🍓",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A)),
                shape = RoundedCornerShape(50),
            ) {
                Text("Back to Home")
            }
            Button(
                onClick = { navController.navigate("FruitsGame") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A)),
                shape = RoundedCornerShape(50),
            ) {
                Text("Play Again")
            }
        }
    } else {
        val correctFruit = fullFruitList[currentIndex]

        // ✅ Correct and fresh shuffle of options
        val imageOptions = remember(currentIndex) {
            val otherFruits = fullFruitList.filter { it.name != correctFruit.name }.shuffled().take(3)
            (otherFruits + correctFruit).shuffled()
        }

        LaunchedEffect(currentIndex) {
            speak("Which fruit is ${correctFruit.name}?")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9C4))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Which fruit is ${correctFruit.name}?",
                color = Color(0xFFD84315),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                imageOptions.chunked(2).forEach { rowFruits ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowFruits.forEach { fruit ->
                            Image(
                                painter = painterResource(id = fruit.resId),
                                contentDescription = fruit.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(140.dp)
                                    .background(Color.White, RoundedCornerShape(16.dp))
                                    .clickable {
                                        if (fruit.name == correctFruit.name) {
                                            speak("It's correct", "correct")
                                        } else {
                                            speak("Oops! It's incorrect")
                                        }
                                    }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { speak("Which fruit is ${correctFruit.name}?") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4DB6AC)),
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("🔊 Speak Again")
            }

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A)),
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Back to Home")
            }
        }
    }
}
