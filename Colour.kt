package com.example.firebase.pages

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun Colour(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }

    // Initialize TextToSpeech
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setSpeechRate(1.0f)
                ttsReady = true
                tts?.speak("Identify the colour", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    fun speak(text: String) {
        if (ttsReady) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    val colorList = listOf(
        "Red" to Color.Red,
        "Blue" to Color.Blue,
        "Green" to Color.Green,
        "Yellow" to Color.Yellow,
        "Orange" to Color(0xFFFF9800),
        "Purple" to Color(0xFF9C27B0),
        "Pink" to Color(0xFFE91E63),
        "Brown" to Color(0xFF795548),
        "Black" to Color.Black
    )

    var currentIndex by remember { mutableStateOf(0) }
    var options by remember { mutableStateOf(generateOptions(colorList, currentIndex)) }
    var feedback by remember { mutableStateOf("") }
    var gameFinished by remember { mutableStateOf(false) }
    var isWaiting by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    val correctAnswer = if (currentIndex < colorList.size) colorList[currentIndex].first else ""
    val colorBox = if (currentIndex < colorList.size) colorList[currentIndex].second else Color.Transparent

    // When currentIndex changes
    LaunchedEffect(currentIndex) {
        if (currentIndex < colorList.size) {
            options = generateOptions(colorList, currentIndex)
            feedback = ""
            isWaiting = false
        } else {
            gameFinished = true
            speak("Congratulations! You have completed the game")
        }
    }

    // Wait 2 seconds on correct answer
    LaunchedEffect(isCorrect) {
        if (isCorrect) {
            delay(2000)
            isCorrect = false
            currentIndex++
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC), Color(0xFF81D4FA))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Identify the Colour",
                fontSize = 28.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            IconButton(
                onClick = { speak("Identify the colour") },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, shape = CircleShape)
            ) {
                Icon(Icons.Default.VolumeUp, contentDescription = "Repeat", tint = Color.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!gameFinished) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(colorBox, shape = RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (feedback.isNotEmpty()) {
                Text(text = feedback, fontSize = 20.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (gameFinished) {
                Text(
                    text = "🎉 Congratulations! You have completed the Game! 🏆🥳",
                    fontSize = 22.sp,
                    color = Color(0xFF4CAF50)
                )
                Button(
                    onClick = { navController.navigate("ColourGame") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A)),
                    shape = RoundedCornerShape(50),
                ) {
                    Text("play Again")
                }
            } else {
                options.forEach { option ->
                    Button(
                        onClick = {
                            if (isWaiting) return@Button
                            if (option == correctAnswer) {
                                feedback = "\uD83D\uDE0A Hurray! It's correct!"
                                speak("Hurray! It's correct")
                                isWaiting = true
                                isCorrect = true
                            } else {
                                feedback = "\uD83D\uDE41 Wrong! Try again."
                                speak("Oops! Wrong Try Again")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isWaiting
                    ) {
                        Text(text = option, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

// Utility function: generate options (1 correct + 3 wrong)
fun generateOptions(colorList: List<Pair<String, Color>>, correctIndex: Int): List<String> {
    val correct = colorList[correctIndex].first
    val others = colorList.map { it.first }.filterNot { it == correct }.shuffled().take(3)
    return (others + correct).shuffled()
}
