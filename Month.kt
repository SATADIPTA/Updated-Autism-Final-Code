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
fun Month(modifier: Modifier = Modifier, navController: NavController) {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    var currentMonthIndex by remember { mutableStateOf(0) }
    var ttsReady by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    val context = navController.context

    val tts = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
            }
        }
    }

    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }

    DisposableEffect(Unit) {
        tts.language = Locale.US
        tts.setSpeechRate(0.5f) // Slow rate for children

        onDispose {
            tts.stop()
            tts.shutdown()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    fun speakMonthWithSpelling(month: String) {
        val spelled = month.uppercase().toCharArray().joinToString(", ")
        val fullSpeech = "$month. Now spell: $spelled"
        tts.speak(fullSpeech, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun playSong() {
        mediaPlayer = MediaPlayer.create(context, R.raw.monthsplay).apply {
            setOnCompletionListener {
                isPlaying = false
            }
            start()
            isPlaying = true
        }
    }

    LaunchedEffect(currentMonthIndex, ttsReady) {
        if (ttsReady) {
            speakMonthWithSpelling(months[currentMonthIndex])
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.month),
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
                text = months[currentMonthIndex],
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                //color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                if (ttsReady) {
                    speakMonthWithSpelling(months[currentMonthIndex])
                }
            }) {
                Text(text = "Repeat Again")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Button(onClick = {
                    currentMonthIndex =
                        if (currentMonthIndex == 0) months.lastIndex else currentMonthIndex - 1
                    if (ttsReady) {
                        speakMonthWithSpelling(months[currentMonthIndex])
                    }
                }) {
                    Text(text = "Previous")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = {
                    currentMonthIndex = (currentMonthIndex + 1) % months.size
                    if (ttsReady) {
                        speakMonthWithSpelling(months[currentMonthIndex])
                    }
                }) {
                    Text(text = "Next")
                }
            }

            // Show play button after last month
            if (currentMonthIndex == months.lastIndex && !isPlaying) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    playSong()
                }) {
                    Text(text = "Play Month Song")
                }
            }
        }
    }
}
