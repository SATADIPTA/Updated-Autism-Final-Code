package com.example.firebase.pages

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.firebase.R
import java.util.*

data class BodyPart(val name: String, val imageRes: Int)

@Composable
fun BodyPartsLearning(modifier: Modifier = Modifier, navController: NavController) {
    val bodyParts = listOf(
        BodyPart("Head", R.drawable.head),
        BodyPart("Eye", R.drawable.eye),
        BodyPart("Ear", R.drawable.ear),
        BodyPart("Nose", R.drawable.nose),
        BodyPart("Lips", R.drawable.lips),
        BodyPart("Mouth", R.drawable.mouth),
        BodyPart("Hand", R.drawable.hand),
        BodyPart("Leg", R.drawable.leg),
        BodyPart("Foot", R.drawable.foot),
        BodyPart("Overall Human Body", R.drawable.full_body)
    )

    var index by remember { mutableStateOf(0) }
    val currentPart = bodyParts[index]

    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }

    val tts = remember {
        TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
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

    LaunchedEffect(index, ttsReady) {
        if (ttsReady) {
            if (index == bodyParts.lastIndex) {
                tts.speak("This is the overall human body", TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                tts.speak("This is the ${currentPart.name}", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🧠 Body Parts Learning",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        Image(
            painter = painterResource(id = currentPart.imageRes),
            contentDescription = currentPart.name,
            modifier = Modifier
                .size(500.dp)
                .padding(10.dp)
        )

        Text(
            text = currentPart.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Blue,
            modifier = Modifier.padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { if (index > 0) index-- },
                enabled = index > 0,
                modifier = Modifier.width(120.dp)
            ) {
                Text("Previous")
            }

            Button(
                onClick = { if (index < bodyParts.size - 1) index++ },
                enabled = index < bodyParts.size - 1,
                modifier = Modifier.width(120.dp)
            ) {
                Text("Next")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .width(180.dp)
                .height(50.dp)
        ) {
            Text("Back to Home")
        }
    }
}


