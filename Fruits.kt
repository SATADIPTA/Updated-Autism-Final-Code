package com.example.firebase.pages

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

data class Frruit(val name: String, val resId: Int)

@Composable
fun Fruits(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(0) }
    var gameCompleted by remember { mutableStateOf(false) }

    val fruitList = listOf(
        Frruit("Apple", R.drawable.apple),
        Frruit("Banana", R.drawable.banana),
        Frruit("Grape", R.drawable.grape),
        Frruit("Mango", R.drawable.mango),
        Frruit("Orange", R.drawable.orange),
        Frruit("Pineapple", R.drawable.pineapple),
        Frruit("Watermelon", R.drawable.watermellon),
        Frruit("Pomegranate", R.drawable.pomegranate),
        Frruit("Coconut", R.drawable.coconut),
        Frruit("Strawberry", R.drawable.strawberry),
        Frruit("Lychee", R.drawable.lychee),
        Frruit("Blueberry", R.drawable.blueberry)
    )

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
                    if (currentIndex < fruitList.size - 1) {
                        currentIndex++
                    } else {
                        gameCompleted = true

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

    if (gameCompleted) {

        LaunchedEffect(Unit) {
            speak("Congratulations! You have completed the game. Well Done!")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFDCE775)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🎉 Congratulations!", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    currentIndex = 0
                    gameCompleted = false
                    speak("Guess the fruit!", "restart")
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
            ) {
                Text("Play Again")
            }
        }
        return
    }

    val currentFruit = fruitList[currentIndex]
    val options = remember(currentIndex) {
        val otherFruits = fruitList.filter { it != currentFruit }.shuffled().take(3)
        (otherFruits + currentFruit).shuffled()
    }

    LaunchedEffect(currentIndex) {
        speak("Guess the fruit!")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF673AB7))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("🍎 Guess the Fruit!", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Image(
            painter = painterResource(id = currentFruit.resId),
            contentDescription = currentFruit.name,
            modifier = Modifier
                .size(200.dp)
                .background(Color.White, RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Fit
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                Button(
                    onClick = {
                        if (option.name == currentFruit.name) {
                            speak("It's correct", "correct")
                        } else {
                            speak("Oops! It's incorrect")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF80DEEA)),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text(option.name, fontSize = 18.sp,color=Color.Black)
                }
            }
        }

        Button(
            onClick = { speak("Guess the fruit!") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4DB6AC)),
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(top = 16.dp)
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



