package com.example.firebase.pages

import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun LowerCaseLetters(modifier: Modifier = Modifier,navController: NavController) {
    val letters = ('a'..'z').toList()
    val context = navController.context
    var ttsReady by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }

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
            mediaPlayer = null
        }
    }

    fun speakText(text: String) {
        if (ttsReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun playSong() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.abc_song).apply {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tap a letter to hear it", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(15.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(letters.size) { index ->
                    val letter = letters[index]
                    var tapped by remember { mutableStateOf(false) }

                    val scale by animateFloatAsState(
                        targetValue = if (tapped) 1.3f else 1f,
                        animationSpec = tween(durationMillis = 200),
                        label = "letterBounce"
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale
                            )
                            .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                            .clickable {
                                tapped = true
                                speakText(letter.toString())
                            }
                    ) {
                        Text(
                            text = letter.toString(),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    LaunchedEffect(tapped) {
                        if (tapped) {
                            delay(200)
                            tapped = false
                        }
                    }
                }
            }

            Button(
                onClick = { playSong() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Play Alphabet Song", color = Color.White)
            }

            if (isPlaying) {
                Button(
                    onClick = { togglePauseResume() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
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


