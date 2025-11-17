package com.example.firebase.pages

import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.firebase.R
import java.util.*

@Composable
fun Numbers(modifier: Modifier, navController: NavController) {
    val context = navController.context
    var currentNumberIndex by remember { mutableStateOf(0) }
    var ttsReady by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }

    val numberList = (1..10).map { it.toString() }

    val numberImages = mapOf(
        "1" to R.drawable.object_1,
        "2" to R.drawable.object_2,
        "3" to R.drawable.object_3,
        "4" to R.drawable.object_4,
        "5" to R.drawable.object_5,
        "6" to R.drawable.object_6,
        "7" to R.drawable.object_7,
        "8" to R.drawable.object_8,
        "9" to R.drawable.object_9,
        "10" to R.drawable.object_10
    )

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
        tts.setSpeechRate(0.5f)
        onDispose {
            tts.stop()
            tts.shutdown()
            mediaPlayer?.release()
        }
    }




    fun speakText(text: String, ready: Boolean, tts: TextToSpeech) {
        if (ready) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun playSong() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.number_song).apply {
                setOnCompletionListener {
                    isPlaying = false
                    isPaused = false
                }
            }
        }
        mediaPlayer?.start()
        isPlaying = true
        isPaused = false
    }

    fun togglePauseResume() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPaused = true
        } else {
            mediaPlayer?.start()
            isPaused = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background animation
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.abc_animation)
                .decoderFactory(
                    if (android.os.Build.VERSION.SDK_INT >= 28)
                        ImageDecoderDecoder.Factory()
                    else
                        GifDecoder.Factory()
                )
                .build(),
            contentDescription = "GIF Background",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop,
            alpha = 0.2f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Number ${numberList[currentNumberIndex]}",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            numberImages[numberList[currentNumberIndex]]?.let { imageRes ->
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Image for ${numberList[currentNumberIndex]}",
                    modifier = Modifier
                        .size(300.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (currentNumberIndex > 0) {
                            currentNumberIndex--
                            speakText(numberList[currentNumberIndex], ttsReady, tts)
                        }
                    },
                    enabled = currentNumberIndex > 0
                ) {
                    Text("Previous")
                }

                Button(
                    onClick = {
                        if (currentNumberIndex < numberList.size - 1) {
                            currentNumberIndex++
                            speakText(numberList[currentNumberIndex], ttsReady, tts)
                        }
                    },
                    enabled = currentNumberIndex != numberList.size - 1
                ) {
                    Text("Next")
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { playSong() },
                    modifier = Modifier
                        .width(250.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Play Number Song", color = Color.White)
                }

                if (isPlaying) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { togglePauseResume() },
                        modifier = Modifier
                            .width(250.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                    ) {
                        Text(
                            text = if (isPaused) "Resume Song" else "Pause Song",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
