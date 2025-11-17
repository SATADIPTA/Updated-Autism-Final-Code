package com.example.firebase.pages

import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.R
import java.util.*

@Composable
fun Day(modifier: Modifier = Modifier, navController: NavController) {
    val days = listOf(
        "Sunday", "Monday", "Tuesday", "Wednesday",
        "Thursday", "Friday", "Saturday"
    )
    var currentDayIndex by remember { mutableStateOf(0) }
    var ttsReady by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    val context = navController.context
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }

    val tts = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
            }
        }
    }

    DisposableEffect(Unit) {
        tts.language = Locale.US
        tts.setSpeechRate(0.6f)
        onDispose {
            tts.stop()
            tts.shutdown()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    fun speakDayWithSpelling(day: String) {
        val spelledOut = day.uppercase().toCharArray().joinToString(", ")
        val sentence = "$day. Now spell: $spelledOut"
        tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun playSong() {
        mediaPlayer = MediaPlayer.create(context, R.raw.week).apply {
            setOnCompletionListener {
                isPlaying = false
            }
            start()
            isPlaying = true
        }
    }

    // 🔁 Speak the day every time it changes
    LaunchedEffect(currentDayIndex, ttsReady) {
        if (ttsReady) {
            speakDayWithSpelling(days[currentDayIndex])
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.day),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = days[currentDayIndex],
                //color= Color.Blue,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                //color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                if (ttsReady) {
                    speakDayWithSpelling(days[currentDayIndex])
                }
            }) {
                Text(text = "Repeat Again")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Button(onClick = {
                    currentDayIndex = if (currentDayIndex == 0) days.lastIndex else currentDayIndex - 1
                }) {
                    Text(text = "Previous")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = {
                    currentDayIndex = (currentDayIndex + 1) % days.size
                }) {
                    Text(text = "Next")
                }
            }

            if (currentDayIndex == days.lastIndex && !isPlaying) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { playSong() }) {
                    Text(text = "Play Days Song")
                }
            }
        }
    }
}
