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

data class ShapeItem(
    val name: String,
    val shapeImageRes: Int,
    val realLifeName: String,
    val realLifeImageRes: Int
)

@Composable
fun ShapesLearning(modifier: Modifier = Modifier, navController: NavController) {
    val shapes = listOf(
        ShapeItem("Circle", R.drawable.circle, "Clock", R.drawable.clock),
        ShapeItem("Square", R.drawable.square, "Chessboard", R.drawable.chessboard),
        ShapeItem("Rectangle", R.drawable.rectangle, "Door", R.drawable.door),
        ShapeItem("Octagon", R.drawable.octagon, "Stop sign", R.drawable.stop_sign),
        ShapeItem("Star", R.drawable.star, "Starfish", R.drawable.star_fish),
        ShapeItem("Triangle", R.drawable.triangle, "Cake piece", R.drawable.cake_piece)
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
        tts.setSpeechRate(0.9f)
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
    val currentShape = shapes[currentIndex]

    LaunchedEffect(currentIndex) {
        speak("This shape is ${currentShape.name}. ${currentShape.realLifeName} is also a ${currentShape.name}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDCE775)) // green
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "🧠 Let's Learn Shapes!",
            fontSize = 24.sp,
            color = Color.Blue,
            fontWeight = FontWeight.Bold
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("This shape is:", fontSize = 24.sp, color = Color.Black)
            Image(
                painter = painterResource(id = currentShape.shapeImageRes),
                contentDescription = currentShape.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
            )
            Text(
                text = currentShape.name,
                color = Color.Blue,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Real-life example:", fontSize = 22.sp, color = Color.Black)
            Image(
                painter = painterResource(id = currentShape.realLifeImageRes),
                contentDescription = currentShape.realLifeName,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(250.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(3.dp)
                    .clickable {
                        speak("This shape is ${currentShape.name}. ${currentShape.realLifeName} is also a ${currentShape.name}")
                    }
            )
            Text(
                text = currentShape.realLifeName,
                color = Color.Blue,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    speak("This shape is ${currentShape.name}. ${currentShape.realLifeName} is also a ${currentShape.name}")
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp)
            ) {
                Text("🔊 Speak Again")
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        currentIndex = (currentIndex + 1) % shapes.size
                    },
                    modifier = Modifier
                        .width(140.dp)
                        .height(48.dp)
                ) {
                    Text("Next")
                }

                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .width(140.dp)
                        .height(48.dp)
                ) {
                    Text("Back to Home")
                }
            }
        }
    }
}
