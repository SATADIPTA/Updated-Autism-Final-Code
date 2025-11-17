package com.example.firebase.pages

import android.speech.tts.TextToSpeech
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.R
import java.util.*

@Composable
fun Alphabet(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var currentImage by remember { mutableStateOf<Int?>(null) }
    var currentImageName by remember { mutableStateOf<String?>(null) }

    // Initialize TextToSpeech
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.speak("Click on any alphabet to see its corresponding image", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.shutdown()
        }
    }

    val imageMap = mapOf(
        "A" to R.drawable.apple,
        "B" to R.drawable.ball,
        "C" to R.drawable.cat,
        "D" to R.drawable.dog,
        "E" to R.drawable.elephant,
        "F" to R.drawable.fish,
        "G" to R.drawable.grapes,
        "H" to R.drawable.hen,
        "I" to R.drawable.icecream,
        "J" to R.drawable.jug,
        "K" to R.drawable.kite,
        "L" to R.drawable.lion,
        "M" to R.drawable.monkey,
        "N" to R.drawable.nest,
        "O" to R.drawable.orange,
        "P" to R.drawable.parrot,
        "Q" to R.drawable.queen,
        "R" to R.drawable.rabbit,
        "S" to R.drawable.sun,
        "T" to R.drawable.tiger,
        "U" to R.drawable.umbrella,
        "V" to R.drawable.van,
        "W" to R.drawable.watch,
        "X" to R.drawable.xylophone,
        "Y" to R.drawable.yak,
        "Z" to R.drawable.zebra
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF8BBD0), Color(0xFFF48FB1), Color(0xFFF06292))
                )
            )
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Click on any alphabet to see its corresponding image",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        currentImage?.let { resId ->
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)
            )

            currentImageName?.let { name ->
                Text(
                    text = name.replaceFirstChar { it.uppercaseChar() },
                    fontSize = 24.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        val alphabets = ('A'..'Z').toList()
        for (chunk in alphabets.chunked(4)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                for (char in chunk) {
                    Button(onClick = {
                        val resource = imageMap[char.toString()]
                        currentImage = resource

                        // Example: "A for Apple"
                        val objectName = context.resources.getResourceEntryName(resource ?: 0)
                        val speakText = "${char.uppercaseChar()} for ${objectName.replaceFirstChar { it.uppercaseChar() }}"
                        currentImageName = objectName

                        tts?.speak(speakText, TextToSpeech.QUEUE_FLUSH, null, null)
                    }) {
                        Text(text = char.toString())
                    }
                }
            }
        }
    }
}
