package com.example.firebase.pages

import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.R
import java.util.*

data class SoundQuestion(
    val soundResId: Int,
    val correctImageResId: Int,
    val options: List<Int>
)

@Composable
fun SoundIdentification(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var currentIndex by remember { mutableStateOf(0) }
    var ttsReady by remember { mutableStateOf(false) }
    var gameCompleted by remember { mutableStateOf(false) }

    val questions = remember {
        listOf(
            SoundQuestion(
                R.raw.birdvoice,
                R.drawable.bird,
                listOf(R.drawable.bird, R.drawable.car, R.drawable.cat, R.drawable.ship)
            ),
            SoundQuestion(
                R.raw.carvoice,
                R.drawable.car,
                listOf(R.drawable.dog, R.drawable.car, R.drawable.train, R.drawable.bird)
            ),
            SoundQuestion(
                R.raw.catvoice,
                R.drawable.cat,
                listOf(R.drawable.train, R.drawable.cat, R.drawable.ship, R.drawable.dog)
            ),
            SoundQuestion(
                R.raw.trainvoice,
                R.drawable.train,
                listOf(R.drawable.bird, R.drawable.car, R.drawable.train, R.drawable.ship)
            ),
            SoundQuestion(
                R.raw.dogvoice,
                R.drawable.dog,
                listOf(R.drawable.cat, R.drawable.ship, R.drawable.dog, R.drawable.bird)
            ),
            SoundQuestion(
                R.raw.shipvoice,
                R.drawable.ship,
                listOf(R.drawable.ship, R.drawable.car, R.drawable.cat, R.drawable.dog)
            )
        ).shuffled()
    }

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

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
            tts.stop()
            tts.shutdown()
            mediaPlayer?.release()
        }
    }

    fun speak(text: String, id: String? = null) {
        if (ttsReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, id)
        }
    }

    fun playSoundAgain() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, questions[currentIndex].soundResId)
        mediaPlayer?.start()
    }

    if (gameCompleted) {

        LaunchedEffect(Unit) {
            speak("Congratulations! You have completed the game. Great job!")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF81C784))
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🎉 Great Job!", fontSize = 32.sp)
            Text("You completed the Sound Identification game.", fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A)),
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Back to Home")
            }
            Button(
                onClick = { navController.navigate("SoundIdentification") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A)),
                shape = RoundedCornerShape(50),
            ) {
                Text("play Again")
            }
        }
    } else {
        val currentQuestion = questions[currentIndex]

        LaunchedEffect(currentIndex) {
            playSoundAgain()
            speak("Guess the sound!")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFBA68C8))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("🎵 Guess the Sound!", fontSize = 24.sp)

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                currentQuestion.options.shuffled().forEach { imageResId ->
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clickable {
                                if (imageResId == currentQuestion.correctImageResId) {
                                    speak("It's correct", "correct")
                                } else {
                                    speak("Oops! It's incorrect")
                                }
                            }
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { playSoundAgain() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FC3F7)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("🔊 Play Sound Again")
                }

                Button(
                    onClick = { speak("Guess the sound!") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4DB6AC)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("🔈 Speak Again")
                }
            }

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A)),
                shape = RoundedCornerShape(50)
            ) {
                Text("Back to Home")
            }
        }
    }
}
