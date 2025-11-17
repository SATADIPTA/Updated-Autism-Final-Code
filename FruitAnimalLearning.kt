package com.example.firebase.pages

import android.speech.tts.TextToSpeech
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

@Composable
fun FruitAnimalLearning(modifier: Modifier = Modifier, navController: NavController) {
    val pairs = listOf(
        FruitAnimalPair("Banana", R.drawable.banana, "Monkey", R.drawable.monkey),
        FruitAnimalPair("Carrot", R.drawable.carrot, "Rabbit", R.drawable.rabbit),
        FruitAnimalPair("Apple", R.drawable.apple, "Horse", R.drawable.horse),
        FruitAnimalPair("Grapes", R.drawable.grape, "Elephant", R.drawable.elephant),
        FruitAnimalPair("Orange", R.drawable.orange, "Woodpecker", R.drawable.woodpecker),
        FruitAnimalPair("Pineapple", R.drawable.pineapple, "Bear", R.drawable.bear),
        FruitAnimalPair("Watermelon", R.drawable.watermellon, "Zebra", R.drawable.zebra),
        FruitAnimalPair("Strawberry", R.drawable.strawberry, "Deer", R.drawable.deer),
        FruitAnimalPair("Mango", R.drawable.mango, "Giraffe", R.drawable.giraffe)
    )

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
        tts.setSpeechRate(0.75f)
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun speak(text: String) {
        if (ttsReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    var currentIndex by remember { mutableStateOf(0) }
    val currentPair = pairs[currentIndex]

    LaunchedEffect(currentIndex) {
        speak("${currentPair.animalName} eats ${currentPair.fruitName}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF9800)) // Orange background
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🐾 Learn who eats what!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
            // Removed clickable from here since you want separate speak button
        ) {
            Image(
                painter = painterResource(id = currentPair.animalRes),
                contentDescription = currentPair.animalName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.Cyan, RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(currentPair.animalName, fontSize = 18.sp, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = currentPair.fruitRes),
                contentDescription = currentPair.fruitName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Yellow, RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(currentPair.fruitName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        // New Speak Again button with background color
        Button(
            onClick = { speak("${currentPair.animalName} eats ${currentPair.fruitName}") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)), // green background
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(50.dp)
        ) {
            Text(text = "Speak Again", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (currentIndex < pairs.lastIndex) {
                        currentIndex++
                    } else {
                        currentIndex = 0
                    }
                }
            ) {
                Text("Next")
            }

            Button(onClick = {
                navController.popBackStack()
            }) {
                Text("Back to Home")
            }
        }
    }
}
