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

data class Animal(val name: String, val resId: Int)

@Composable
fun Animal(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(0) }
    var gameFinished by remember { mutableStateOf(false) }

    // Shuffle once using remember
    val fullAnimalList = remember {
        listOf(
            Animal("Bear", R.drawable.bear),
            Animal("Cat", R.drawable.cat),
            Animal("Deer", R.drawable.deer),
            Animal("Dog", R.drawable.dog),
            Animal("Elephant", R.drawable.elephant),
            Animal("Giraffe", R.drawable.giraffe),
            Animal("Horse", R.drawable.horse),
            Animal("Lion", R.drawable.lion),
            Animal("Monkey", R.drawable.monkey),
            Animal("Panda", R.drawable.panda_img),
            Animal("Rabbit", R.drawable.rabbit),
            Animal("Tiger", R.drawable.tiger),
            Animal("Yak", R.drawable.yak),
            Animal("Zebra", R.drawable.zebra),
            Animal("Donkey", R.drawable.donkey),
            Animal("Goat", R.drawable.goat),
            Animal("Cow", R.drawable.cow),
            Animal("Butterfly", R.drawable.butterfly),
            Animal("Lizard", R.drawable.lizard),
            Animal("Cockroach", R.drawable.cockroach),
            Animal("Rhinoceros", R.drawable.rhinoceros),
            Animal("Mosquito", R.drawable.mosquito),
            Animal("Crocodile", R.drawable.crocodile),
            Animal("Mouse", R.drawable.mouse),
            Animal("Squirrel", R.drawable.squirrel),
            Animal("Snake", R.drawable.snake)
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
                    if (currentIndex < fullAnimalList.size - 1) {
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
            speak("Congratulations! You have completed the game. Great job!")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFC8E6C9))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎉 Congratulations! 🎉",
                color = Color(0xFF388E3C),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "🏆 You have completed the game! 😊",
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
                onClick = { navController.navigate("Animal") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A)),
                shape = RoundedCornerShape(50),
            ) {
                Text("Play Again")
            }
        }
    } else {
        val correctAnimal = fullAnimalList[currentIndex]

        // ✅ Corrected logic here
        val imageOptions = remember(currentIndex) {
            val otherAnimals = fullAnimalList.filter { it.name != correctAnimal.name }.shuffled().take(3)
            (otherAnimals + correctAnimal).shuffled()
        }

        LaunchedEffect(currentIndex) {
            speak("Which animal is ${correctAnimal.name}?")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF59D))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Which animal is ${correctAnimal.name}?",
                color = Color.Red,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                imageOptions.chunked(2).forEach { rowAnimals ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowAnimals.forEach { animal ->
                            Image(
                                painter = painterResource(id = animal.resId),
                                contentDescription = animal.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(140.dp)
                                    .background(Color.White, RoundedCornerShape(16.dp))
                                    .clickable {
                                        if (animal.name == correctAnimal.name) {
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
                onClick = { speak("Which animal is ${correctAnimal.name}?") },
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
