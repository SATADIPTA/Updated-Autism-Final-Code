package com.example.firebase.pages

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.R
import java.util.*

data class Bird(val name: String, val resId: Int)

@Composable
fun BirdsGame(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(0) }
    var gameFinished by remember { mutableStateOf(false) }

    val fullBirdList = remember {
        listOf(
            Bird("Parrot", R.drawable.parrot),
            Bird("Pigeon", R.drawable.pigeon),
            Bird("Peacock", R.drawable.peacock),
            Bird("Owl", R.drawable.owl),
            Bird("Crow", R.drawable.crow),
            Bird("Sparrow", R.drawable.sparrow),
            Bird("Duck", R.drawable.duck),
            Bird("Swan", R.drawable.swan),
            Bird("Eagle", R.drawable.eagle),
            Bird("Kingfisher", R.drawable.kingfisher),
            Bird("Vulture", R.drawable.vulture),
            Bird("Hen", R.drawable.hen),
            Bird("Woodpecker", R.drawable.woodpecker),
            Bird("Flamingo", R.drawable.flamingo)
        ).shuffled()
    }

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
                    if (currentIndex < fullBirdList.size - 1) {
                        currentIndex += 1
                    } else {
                        gameFinished = true
                    }
                }
            }

            override fun onError(utteranceId: String?) {}
        })

        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun speak(text: String, id: String? = null) {
        if (ttsReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, id)
        }
    }

    if (gameFinished) {
        LaunchedEffect(Unit) {
            speak("Congratulations! You have completed the birds game!")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE1F5FE)) // Light blue
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎉 Great Job! 🎉",
                color = Color(0xFF01579B),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "🦜 You finished the birds game!",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A)),
                shape = RoundedCornerShape(50),
            ) {
                Text("Back to Home")
            }
            Button(
                onClick = { navController.navigate("BirdsGame") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A)),
                shape = RoundedCornerShape(50),
            ) {
                Text("Play Again")
            }
        }
    } else {
        val correctBird = fullBirdList[currentIndex]

        val imageOptions = remember(currentIndex) {
            val others = fullBirdList.filter { it.name != correctBird.name }.shuffled().take(3)
            (others + correctBird).shuffled()
        }

        LaunchedEffect(currentIndex) {
            speak("Which bird is ${correctBird.name}?")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9C4)) // Light yellow
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Which bird is ${correctBird.name}?",
                color = Color(0xFF1B5E20),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                imageOptions.chunked(2).forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        row.forEach { bird ->
                            Image(
                                painter = painterResource(id = bird.resId),
                                contentDescription = bird.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(180.dp)
                                    .background(Color.White, RoundedCornerShape(16.dp))
                                    .clickable {
                                        if (bird.name == correctBird.name) {
                                            speak("It's correct", "correct")
                                        } else {
                                            speak("Oops! It's incorrect")
                                        }
                                    }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { speak("Which bird is ${correctBird.name}?") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4DB6AC)),
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("🔊 Speak Again")
            }

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A)),
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Back to Home")
            }
        }
    }
}


