package com.example.firebase.pages

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.firebase.R
import java.util.*
import kotlin.math.roundToInt
import androidx.navigation.NavController

@Composable
fun PatchGame(modifier: Modifier = Modifier, navController: NavController) {
    var patch1Offset by remember { mutableStateOf(Offset(50f, 50f)) }
    var patch2Offset by remember { mutableStateOf(Offset(200f, 50f)) }

    val dropZone1 = Offset(260f, 350f) // Target area 1
    val dropZone2 = Offset(300f, 479f) // Target area 2
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
        tts.setSpeechRate(0.5f)
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

    LaunchedEffect(patch1Offset, patch2Offset) {
        val threshold = 50f

        val isPatch1Correct = (patch1Offset - dropZone1).getDistance() < threshold
        val isPatch2Correct = (patch2Offset - dropZone2).getDistance() < threshold

        if (isPatch1Correct) {
            Toast.makeText(context, "Patch 1: Correct!", Toast.LENGTH_SHORT).show()
            speak("Great job!")
        } else if (patch1Offset.y > 200) {
            speak("Try placing patch 1 in the correct spot.")
        }

        if (isPatch2Correct) {
            Toast.makeText(context, "Patch 2: Correct!", Toast.LENGTH_SHORT).show()
            speak("Well done!")
        } else if (patch2Offset.y > 200) {
            speak("Try placing patch 2 in the correct spot.")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.mainimg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        // Drop zones (use alpha during testing, remove later)
        Box(
            modifier = Modifier
                .offset(x = dropZone1.x.dp, y = dropZone1.y.dp)
                .size(100.dp)
                .background(Color.Red.copy(alpha = 0.3f))
        )

        Box(
            modifier = Modifier
                .offset(x = dropZone2.x.dp, y = dropZone2.y.dp)
                .size(100.dp)
                .background(Color.Green.copy(alpha = 0.3f))
        )

        // Patch 1
        Image(
            painter = painterResource(id = R.drawable.patch1),
            contentDescription = "Patch 1",
            modifier = Modifier
                .offset { IntOffset(patch1Offset.x.roundToInt(), patch1Offset.y.roundToInt()) }
                .size(95.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        patch1Offset += dragAmount
                        change.consume()
                    }
                }
        )

        // Patch 2
        Image(
            painter = painterResource(id = R.drawable.patch2),
            contentDescription = "Patch 2",
            modifier = Modifier
                .offset { IntOffset(patch2Offset.x.roundToInt(), patch2Offset.y.roundToInt()) }
                .size(100.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        patch2Offset += dragAmount
                        change.consume()
                    }
                }
        )

        // Instruction Button
        IconButton(
            onClick = { speak("Place the patches in the image") },
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color.Transparent, shape = CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.thinking_emoji),
                contentDescription = "Instruction"
            )
        }
    }
}
