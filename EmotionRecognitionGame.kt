package com.example.firebase.pages

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.R
import kotlinx.coroutines.delay
import java.util.*

data class Emotion(val name: String, val imageRes: Int)

@Composable
fun EmotionRecognitionGame(modifier: Modifier = Modifier,navController: NavController) {
    val emotions = listOf(
        Emotion("Happy", R.drawable.happy),
        Emotion("Confused", R.drawable.confused),
        Emotion("Scared", R.drawable.scared),
        Emotion("Sad", R.drawable.sad1),
        Emotion("Angry", R.drawable.angry1),
        Emotion("Surprised", R.drawable.surprised1)
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var gameCompleted by remember { mutableStateOf(false) }
    var showFeedback by remember { mutableStateOf(false) }
    var feedbackText by remember { mutableStateOf("") }
    var delayNext by remember { mutableStateOf(false) }
    var ttsReady by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val tts = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
            }
        }
    }

    DisposableEffect(Unit) {
        tts.language = Locale.US
        tts.setSpeechRate(0.8f)
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    val currentEmotion = if (currentQuestionIndex < emotions.size) emotions[currentQuestionIndex] else null

    LaunchedEffect(currentQuestionIndex, ttsReady) {
        if (ttsReady) {
            currentEmotion?.let {
                tts.speak("Find the emotion: ${it.name}", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    LaunchedEffect(delayNext) {
        if (delayNext) {
            delay(1000)
            if (currentQuestionIndex < emotions.size - 1) {
                currentQuestionIndex++
            } else {
                gameCompleted = true
                if (ttsReady) tts.speak("Congratulations! Game Completed", TextToSpeech.QUEUE_FLUSH, null, null)
            }
            showFeedback = false
            delayNext = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Emotion Recognition Game",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Text(
            text = "🔊",
            fontSize = 32.sp,
            modifier = Modifier
                .clickable {
                    if (ttsReady) {
                        tts.speak("Find the correct Emotion", TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
                .padding(bottom = 16.dp)
        )

        if (gameCompleted) {
            Text(
                text = "🎉 Congratulations!!! Game Completed 🎉",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = {
                currentQuestionIndex = 0
                gameCompleted = false
                showFeedback = false
                feedbackText = ""
                delayNext = false
                if (ttsReady) {
                    tts.speak("Let's play again! Find the emotion: ${emotions[0].name}", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }) {
                Text("Play Again")
            }
        } else {
            currentEmotion?.let {
                Text(
                    text = "Find this Emotion: ${it.name}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(emotions) { emotion ->
                    Image(
                        painter = painterResource(id = emotion.imageRes),
                        contentDescription = emotion.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable {
                                if (!delayNext) {
                                    if (emotion.name == currentEmotion?.name) {
                                        feedbackText = "Yayyy"
                                        if (ttsReady) tts.speak("Yayyy", TextToSpeech.QUEUE_FLUSH, null, null)
                                        showFeedback = true
                                        delayNext = true
                                    } else {
                                        feedbackText = "Ooouuuuu! Try Again"
                                        if (ttsReady) tts.speak("Ooouuuuu! Try Again", TextToSpeech.QUEUE_FLUSH, null, null)
                                        showFeedback = true
                                    }
                                }
                            }
                    )
                }
            }

            if (showFeedback) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = feedbackText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
