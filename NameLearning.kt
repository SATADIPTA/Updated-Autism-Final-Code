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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.R
import kotlinx.coroutines.*
import java.util.*

@Composable
fun NameLearning(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    val tts = remember { TextToSpeech(context, null) }
    var currentIndex by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    val qaList = listOf(
        "What is your name?" to "My name is Aarushi Chatterjee",
        "What is your father's name?" to "My father's name is Trinath Chatterjee",
        "What is your mother's name?" to "My mother's name is Moutusi Chatterjee",
        "What is your age?" to "My age is 3 years old",
        "What is your favourite colour?" to "My favourite colour is Pink"
    )

    fun speakQA(index: Int) {
        coroutineScope.launch {
            
            speakSlowly(tts, qaList[index].first)
            delay(2000)
            speakSlowly(tts, "Now say")
            delay(2000)
            speakSlowly(tts, qaList[index].second)
        }
    }

    // Speak automatically when screen loads or index changes
    LaunchedEffect(currentIndex) {
        if (currentIndex < qaList.size) {
            speakQA(currentIndex)
        } else {
            speakSlowly(tts, "Congrats, well done!")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Transparent background image
        Image(
            painter = painterResource(id = R.drawable.question),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.5f)
        )

        // Foreground content
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentIndex < qaList.size) {
                Text(
                    text = qaList[currentIndex].first,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = qaList[currentIndex].second,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(40.dp))

                // First Line: Speak Again
                Row(horizontalArrangement = Arrangement.Center) {
                    Button(onClick = { speakQA(currentIndex) }) {
                        Text("🔊 Speak Again")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Second Line: Previous & Next
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (currentIndex > 0) currentIndex--
                        },
                        enabled = currentIndex > 0
                    ) {
                        Text("⬅️ Previous")
                    }

                    Button(
                        onClick = {
                            if (currentIndex < qaList.size) currentIndex++
                        }
                    ) {
                        Text(if (currentIndex < qaList.size - 1) "Next ➡️" else "Finish ✅")
                    }
                }
            } else {
                Text(
                    text = "🎉 Congrats, well done!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
            }
        }
    }
}

fun speakSlowly(tts: TextToSpeech, text: String) {
    tts.setPitch(1.2f)
    tts.language = Locale.UK
    tts.setSpeechRate(0.6f) // Slower speech for kids
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
}
