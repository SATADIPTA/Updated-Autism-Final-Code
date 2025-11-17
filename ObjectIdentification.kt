package com.example.firebase.pages

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

data class ObjectMatchQuestion(
    val activityImage: Int,
    val questionText: String,
    val correctObject: Int,
    val options: List<Int>
)

@Composable
fun ObjectIdentification(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var currentIndex by remember { mutableStateOf(0) }
    var ttsReady by remember { mutableStateOf(false) }
    var gameCompleted by remember { mutableStateOf(false) }

    val questions = remember {
        listOf(
            ObjectMatchQuestion(
                activityImage = R.drawable.brushing,
                questionText = "Which is used for brushing?",
                correctObject = R.drawable.toothbrush,
                options = listOf(
                    R.drawable.toothbrush,
                    R.drawable.spoon,
                    R.drawable.pencil,
                    R.drawable.socks
                )
            ),
            ObjectMatchQuestion(
                activityImage = R.drawable.eating,
                questionText = "Which is used for eating?",
                correctObject = R.drawable.spoon,
                options = listOf(
                    R.drawable.pencil,
                    R.drawable.socks,
                    R.drawable.spoon,
                    R.drawable.toothbrush
                )
            ),
            ObjectMatchQuestion(
                activityImage = R.drawable.writing,
                questionText = "Which is used for writing?",
                correctObject = R.drawable.pencil,
                options = listOf(
                    R.drawable.toothbrush,
                    R.drawable.pencil,
                    R.drawable.socks,
                    R.drawable.spoon
                )
            ),
            ObjectMatchQuestion(
                activityImage = R.drawable.wearing,
                questionText = "What is the boy wearing on foot?",
                correctObject = R.drawable.socks,
                options = listOf(
                    R.drawable.socks,
                    R.drawable.pencil,
                    R.drawable.toothbrush,
                    R.drawable.spoon
                )
            ),
            ObjectMatchQuestion(
                activityImage = R.drawable.bathing,
                questionText = "Which is used for bathing?",
                correctObject = R.drawable.soap,
                options = listOf(
                    R.drawable.soap,
                    R.drawable.spoon,
                    R.drawable.pencil,
                    R.drawable.socks
                )
            ),
            ObjectMatchQuestion(
                activityImage = R.drawable.drinking,
                questionText = "Which is used for drinking water?",
                correctObject = R.drawable.glass,
                options = listOf(
                    R.drawable.glass,
                    R.drawable.toothbrush,
                    R.drawable.pencil,
                    R.drawable.socks
                )
            ),
            ObjectMatchQuestion(
                activityImage = R.drawable.sleeping,
                questionText = "Which is used for sleeping?",
                correctObject = R.drawable.pillow,
                options = listOf(
                    R.drawable.pillow,
                    R.drawable.spoon,
                    R.drawable.shoes,
                    R.drawable.pen
                )
            ),
            ObjectMatchQuestion(
                activityImage = R.drawable.combing,
                questionText = "Which is used for combing hair?",
                correctObject = R.drawable.comb,
                options = listOf(
                    R.drawable.comb,
                    R.drawable.pencil,
                    R.drawable.socks,
                    R.drawable.toothbrush
                )
            ),
            ObjectMatchQuestion(
                activityImage = R.drawable.cutting,
                questionText = "Which is used for cutting paper?",
                correctObject = R.drawable.scissors,
                options = listOf(
                    R.drawable.scissors,
                    R.drawable.pen,
                    R.drawable.glass,
                    R.drawable.spoon
                )
            ),
            ObjectMatchQuestion(
                activityImage = R.drawable.rain,
                questionText = "Which is used in rain?",
                correctObject = R.drawable.umbrella1,
                options = listOf(
                    R.drawable.umbrella1,
                    R.drawable.socks,
                    R.drawable.toothbrush,
                    R.drawable.comb
                )
            ),
            ObjectMatchQuestion(
                activityImage = R.drawable.school,
                questionText = "What is carried to school?",
                correctObject = R.drawable.schoolbag,
                options = listOf(
                    R.drawable.schoolbag,
                    R.drawable.pillow,
                    R.drawable.spoon,
                    R.drawable.socks
                )
            )
        ).shuffled()
    }

    var tts: TextToSpeech? = remember {
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
                    if (currentIndex < questions.size - 1) {
                        currentIndex += 1
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

    if (gameCompleted) {
        LaunchedEffect(Unit) {
            speak("Congratulations! You have completed the game. Well Done!")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD1C4E9))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🎉 Congratulations! 🎉", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A148C))
            Spacer(modifier = Modifier.height(16.dp))
            Text("🏆 You have completed the game! 😊", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    currentIndex = 0
                    gameCompleted = false
                    speak(questions[0].questionText, "question")
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
            ) {
                Text("Restart")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A))
            ) {
                Text("Back to Home")
            }
        }
        return
    }

    val question = questions[currentIndex]
    val shuffledOptions = remember(currentIndex) { question.options.shuffled() }

    LaunchedEffect(currentIndex) {
        speak(question.questionText, "question")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF8BBD0))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text("🎯 Match the Object Function", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))

        Image(
            painter = painterResource(id = question.activityImage),
            contentDescription = null,
            modifier = Modifier
                .size(220.dp)
                .background(Color.White, RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = question.questionText,
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            shuffledOptions.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowItems.forEach { optionResId ->
                        Image(
                            painter = painterResource(id = optionResId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(180.dp)
                                .background(Color(0xFFBBDEFB), RoundedCornerShape(16.dp))
                                .clickable {
                                    if (optionResId == question.correctObject) {
                                        speak("Yes! It's correct", "correct")
                                    } else {
                                        speak("Oops! It's incorrect", "incorrect")
                                    }
                                },
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { speak(question.questionText, "question") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(50)
        ) {
            Text("🔊 Speak Again")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A)),
            shape = RoundedCornerShape(50)
        ) {
            Text("Back to Home")
        }
    }
}
