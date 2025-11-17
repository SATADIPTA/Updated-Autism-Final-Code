package com.example.firebase.pages

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*

data class Colour(val name: String, val colorValue: Color)

@Composable
fun ColoursGame(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(0) }
    var gameFinished by remember { mutableStateOf(false) }

    val colourList = listOf(
        Colour("Red", Color.Red),
        Colour("Green", Color.Green),
        Colour("Blue", Color.Blue),
        Colour("Yellow", Color.Yellow),
        Colour("Pink", Color(0xFFFFC0CB)),
        Colour("Orange", Color(0xFFFF9800)),
        Colour("Purple", Color(0xFF9C27B0)),
        Colour("Cyan", Color.Cyan),
        Colour("Brown", Color(0xFF8B4513)),
        Colour("Gray", Color.Gray),
        Colour("Black", Color.Black),
        Colour("White", Color.White)
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
                    if (currentIndex < colourList.size - 1) {
                        currentIndex++
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
                .background(Color(0xFFE1BEE7))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎉 Congratulations! 🎉",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6A1B9A)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "You have completed the Colours Game!",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFAB91))
            ) {
                Text("Back to Home")
            }
            Button(
                onClick = { navController.navigate("ColoursGame") },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFAB91))
            ) {
                Text("Play Again")
            }
        }
    } else {
        val correctColour = colourList[currentIndex]
        val options = remember(currentIndex) {
            (colourList - correctColour).shuffled().take(3).plus(correctColour).shuffled()
        }

        LaunchedEffect(currentIndex) {
            speak("Which one is ${correctColour.name}?")
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
                text = "Which one is ${correctColour.name}?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD84315)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                options.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { option ->
                            Box(
                                modifier = Modifier
                                    .size(140.dp)
                                    .background(option.colorValue, RoundedCornerShape(16.dp))
                                    .clickable {
                                        if (option.name == correctColour.name) {
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
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { speak("Which one is ${correctColour.name}?") },
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
}


