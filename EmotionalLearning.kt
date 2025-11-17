package com.example.firebase.pages

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.R
import java.util.*

data class EmotionSlide(val name: String, val imageRes: Int)

@Composable
fun EmotionLearning(modifier: Modifier = Modifier, navController: NavController) {
    val emotionSlides = listOf(
        EmotionSlide("Happy", R.drawable.happy),
        EmotionSlide("Confused", R.drawable.confused),
        EmotionSlide("Scared", R.drawable.scared),
        EmotionSlide("Sad", R.drawable.sad1),
        EmotionSlide("Angry", R.drawable.angry1),
        EmotionSlide("Surprised", R.drawable.surprised1)
    )

    var currentIndex by remember { mutableStateOf(0) }
    val currentEmotion = emotionSlides[currentIndex]

    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }

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
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    LaunchedEffect(currentIndex, ttsReady) {
        if (ttsReady) {
            tts.speak(
                "This emoji shows ${currentEmotion.name}",
                TextToSpeech.QUEUE_FLUSH,
                null,
                null
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC)) // Light blue background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Emotion Learning",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Image(
                painter = painterResource(id = currentEmotion.imageRes),
                contentDescription = currentEmotion.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "This emoji shows: ${currentEmotion.name}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (currentIndex < emotionSlides.size - 1) {
                        currentIndex++
                    }
                },
                enabled = currentIndex < emotionSlides.size - 1
            ) {
                Text(text = if (currentIndex < emotionSlides.size - 1) "Next" else "End of Learning")
            }
        }
    }
}
