package com.example.firebase.pages

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.R
import java.util.*
import kotlin.math.roundToInt

data class FruitAnimalPair(
    val fruitName: String,
    val fruitRes: Int,
    val animalName: String,
    val animalRes: Int
)

@Composable
fun FruitAnimalMatchingGame(modifier: Modifier = Modifier, navController: NavController) {
    val stages = listOf(
        listOf(
            FruitAnimalPair("Banana", R.drawable.banana, "Monkey", R.drawable.monkey),
            FruitAnimalPair("Carrot", R.drawable.carrot, "Rabbit", R.drawable.rabbit),
            FruitAnimalPair("Apple", R.drawable.apple, "Horse", R.drawable.horse)
        ),
        listOf(
            FruitAnimalPair("Grapes", R.drawable.grape, "Elephant", R.drawable.elephant),
            FruitAnimalPair("Orange", R.drawable.orange, "Woodpecker", R.drawable.woodpecker),
            FruitAnimalPair("Pineapple", R.drawable.pineapple, "Bear", R.drawable.bear)
        ),
        listOf(
            FruitAnimalPair("Watermelon", R.drawable.watermellon, "Zebra", R.drawable.zebra),
            FruitAnimalPair("Strawberry", R.drawable.strawberry, "Deer", R.drawable.deer),
            FruitAnimalPair("Mango", R.drawable.mango, "Giraffe", R.drawable.giraffe)
        )
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

    fun speak(message: String) {
        if (ttsReady) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    var currentStage by remember { mutableStateOf(0) }
    var showEndScreen by remember { mutableStateOf(false) }

    val currentPairs = remember(currentStage) { stages[currentStage] }
    val shuffledPairs = remember(currentStage) { currentPairs.shuffled() }
    val animalBounds = remember(currentStage) { mutableStateMapOf<String, Rect>() }
    val matchedFruits = remember(currentStage) { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(currentStage) {
        speak("Match the fruit with the correct animal")
    }

    LaunchedEffect(matchedFruits.values.toList()) {
        if (matchedFruits.size == currentPairs.size && matchedFruits.values.all { it }) {
            kotlinx.coroutines.delay(500)
            if (currentStage < stages.lastIndex) {
                currentStage++
            } else {
                showEndScreen = true
                speak("Congratulations, all fruit-animal pairs matched successfully")
            }
        }
    }

    if (showEndScreen) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "🎉 Congratulations! 🎉",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                currentStage = 0
                showEndScreen = false
                speak("Match the fruit with the correct animal")
            }) {
                Text("Play Again")
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔊",
                fontSize = 28.sp,
                modifier = Modifier.clickable {
                    speak("Match the fruits with the correct animals")
                }
            )

            Text(
                "Match the fruits to the animals!",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Animal Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                currentPairs.forEach { pair ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(100.dp)
                    ) {
                        Image(
                            painter = painterResource(id = pair.animalRes),
                            contentDescription = pair.animalName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .background(Color.LightGray, RoundedCornerShape(12.dp))
                                .onGloballyPositioned { coords ->
                                    val pos = coords.positionInWindow()
                                    val size = coords.size
                                    animalBounds[pair.animalName] = Rect(
                                        pos.x,
                                        pos.y,
                                        pos.x + size.width,
                                        pos.y + size.height
                                    )
                                }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(pair.animalName, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Fruits Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                shuffledPairs.forEach { pair ->
                    var offset by remember(currentStage) { mutableStateOf(Offset.Zero) }
                    var fruitBounds by remember(currentStage) { mutableStateOf<Rect?>(null) }
                    val isMatched = matchedFruits[pair.fruitName] ?: false

                    if (!isMatched) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(100.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .offset {
                                        IntOffset(
                                            offset.x.roundToInt(),
                                            offset.y.roundToInt()
                                        )
                                    }
                                    .pointerInput(Unit) {
                                        detectDragGestures(
                                            onDragEnd = {
                                                var matched = false
                                                fruitBounds?.let { fruitRect ->
                                                    animalBounds[pair.animalName]?.let { targetRect ->
                                                        if (fruitRect.overlaps(targetRect)) {
                                                            matchedFruits[pair.fruitName] = true
                                                            matched = true
                                                            speak("Correct! ${pair.fruitName} matches ${pair.animalName}")
                                                        }
                                                    }
                                                }
                                                if (!matched) {
                                                    speak("Try again! ${pair.fruitName} does not match")
                                                    offset = Offset.Zero
                                                }
                                            },
                                            onDrag = { change, dragAmount ->
                                                change.consumeAllChanges()
                                                offset += dragAmount
                                            }
                                        )
                                    }
                                    .onGloballyPositioned { coords ->
                                        val pos = coords.positionInWindow()
                                        val size = coords.size
                                        fruitBounds = Rect(
                                            pos.x,
                                            pos.y,
                                            pos.x + size.width,
                                            pos.y + size.height
                                        )
                                    }
                            ) {
                                Image(
                                    painter = painterResource(id = pair.fruitRes),
                                    contentDescription = pair.fruitName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(90.dp)
                                        .background(Color.Yellow, RoundedCornerShape(12.dp))
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(pair.fruitName, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

// Helper Rect class for bounds
data class Rect(val left: Float, val top: Float, val right: Float, val bottom: Float) {
    fun overlaps(other: Rect): Boolean {
        return left < other.right &&
                right > other.left &&
                top < other.bottom &&
                bottom > other.top
    }
}
