package com.example.firebase.pages

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import java.util.*

data class GKQuestion(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String
)

@Composable
fun GeneralKnowledge(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var currentIndex by remember { mutableStateOf(0) }
    var gameCompleted by remember { mutableStateOf(false) }
    var feedback by remember { mutableStateOf("") }
    var ttsReady by remember { mutableStateOf(false) }

    val questions = listOf(
        GKQuestion("What to take if you are sick?", listOf("Medicine", "Toy", "Book", "Ice Cream"), "Medicine"),
        GKQuestion("Where do you should sleep?", listOf("Bed", "Chair", "Table", "Floor Mat"), "Bed"),
        GKQuestion("What you should do after waking up in the morning?", listOf("Brush your teeth", "Sleep again", "Watch TV", "Cry"), "Brush your teeth"),
        GKQuestion("How to behave with your parents?", listOf("Be polite", "Shout", "Ignore", "Fight"), "Be polite"),
        GKQuestion("What to do before eating?", listOf("Wash hands", "Jump", "Sing a song", "Sleep"), "Wash hands"),
        GKQuestion("What to do if it is raining outside?", listOf("Use umbrella", "Wear cap", "Wear sunglasses", "Take off shoes"), "Use umbrella"),
        GKQuestion("What is the color of the sky?", listOf("Blue", "Green", "Red", "Black"), "Blue"),
        GKQuestion("Which one is for seeing time?", listOf("Watch", "Cup", "Bag", "Bottle"), "Watch"),
        GKQuestion("Which animal lives in water?", listOf("Fish", "Tiger", "Lion", "Cow"), "Fish"),
        GKQuestion("What do we drink?", listOf("Juice", "Stone", "Shoe", "Book"), "Juice")
    )


    val tts: TextToSpeech? = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
            }
        }
    }

    DisposableEffect(Unit) {
        tts?.language = Locale.US
        tts?.setSpeechRate(0.9f)
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}

            override fun onDone(utteranceId: String?) {
                if (utteranceId == "correct") {
                    if (currentIndex < questions.lastIndex) {
                        currentIndex++
                        feedback = ""
                    } else {
                        gameCompleted = true
                    }
                }
            }

            override fun onError(utteranceId: String?) {}
        })

        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    fun speak(text: String, id: String = "default") {
        if (ttsReady) {
            val params = Bundle()
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, id)
        }
    }

    val question = questions.getOrNull(currentIndex)

    if (gameCompleted) {
        LaunchedEffect(Unit) {
            speak("Congratulations! You have completed the game.")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFDCEDC8))
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🎉 Congratulations! 🎉", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            Spacer(modifier = Modifier.height(16.dp))
            Text("You have completed the game!", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    currentIndex = 0
                    gameCompleted = false
                    speak(questions[0].questionText, "question")
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6))
            ) {
                Text("Play Again")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A65))
            ) {
                Text("Back to Home")
            }
        }
    } else {
        LaunchedEffect(currentIndex) {
            speak(question?.questionText ?: "", "question")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF20B2AA))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Q${currentIndex + 1}: ${question?.questionText}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))

            question?.options?.shuffled()?.forEach { option ->
                Button(
                    onClick = {
                        if (option == question.correctAnswer) {
                            feedback = "Hurray! It's Correct"
                            speak(feedback, "correct")
                        } else {
                            feedback = "Oops! Wrong. Try Again"
                            speak(feedback, "incorrect")
                        }
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF9C4))
                ) {
                    Text(option, fontSize = 18.sp, color=Color.Black, fontWeight = FontWeight.Medium)
                }
            }

            if (feedback.isNotEmpty()) {
                Text(
                    text = feedback,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (feedback.contains("Wrong")) Color.Red else Color(0xFF2E7D32)
                )
            }

            Button(
                onClick = { speak(question?.questionText ?: "", "question") },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
            ) {
                Text("🔊 Speak Again")
            }
        }
    }
}

