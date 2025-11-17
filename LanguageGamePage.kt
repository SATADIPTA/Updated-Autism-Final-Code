package com.example.firebase.pages

import android.speech.tts.TextToSpeech
import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*
import kotlinx.coroutines.delay

@Composable
fun LanguageGamePage(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    val textToSpeech = rememberTextToSpeech(context)

    GameScreen(modifier, textToSpeech)
}

@Composable
fun rememberTextToSpeech(context: Context): TextToSpeech {
    val textToSpeech = remember {
        TextToSpeech(context) {}
    }

    DisposableEffect(Unit) {
        textToSpeech.language = Locale.US

        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    return textToSpeech
}

@Composable
fun GameScreen(modifier: Modifier = Modifier, textToSpeech: TextToSpeech) {
    val questions = listOf(
        Pair("Q", "q"),
        Pair("B", "b"),
        Pair("R", "r"),
        Pair("G", "g"),
        Pair("E", "e")
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedUppercase by remember { mutableStateOf<String?>(null) }
    var selectedLowercase by remember { mutableStateOf<String?>(null) }
    var feedback by remember { mutableStateOf("") }
    var gameCompleted by remember { mutableStateOf(false) }
    var initialSpoken by remember { mutableStateOf(false) }

    val (correctUppercase, correctLowercase) = questions[currentQuestionIndex]
    val options = listOf(correctUppercase, correctLowercase, "X", "x", "Y", "y", "Z", "z").shuffled()

    LaunchedEffect(currentQuestionIndex) {
        if (!initialSpoken) {
            textToSpeech.speak(
                "Press the uppercase $correctUppercase and its lowercase",
                TextToSpeech.QUEUE_FLUSH,
                null,
                null
            )
            initialSpoken = true
        }
    }

    LaunchedEffect(feedback) {
        if (feedback.contains("Hurray")) {
            delay(2000)
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                selectedUppercase = null
                selectedLowercase = null
                feedback = ""
                initialSpoken = false
            } else {
                gameCompleted = true
            }
        }
    }

    if (gameCompleted) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Cyan, Color.Blue))),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "\uD83D\uDE0D  \uD83C\uDFC6 Congratulations! You have completed the game!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            textToSpeech.speak(
                "Congratulations! You have completed the game!",
                TextToSpeech.QUEUE_FLUSH,
                null,
                null
            )

        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.Magenta, Color.Cyan)))
            .padding(70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Press the Uppercase and Lowercase: '$correctUppercase'",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        IconButton(onClick = {
            textToSpeech.speak(
                "Press the uppercase $correctUppercase and its lowercase",
                TextToSpeech.QUEUE_FLUSH,
                null,
                null
            )
        }) {
            Icon(
                Icons.Default.VolumeUp,
                contentDescription = "Play Question Voice",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        options.chunked(2).forEach { rowOptions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowOptions.forEach { letter ->
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.Green, shape = RoundedCornerShape(20.dp))
                            .clickable {
                                if (letter == correctUppercase) {
                                    selectedUppercase = letter
                                } else if (letter == correctLowercase) {
                                    selectedLowercase = letter
                                } else {
                                    feedback = "\uD83D\uDE41Wrong! Try again."
                                    textToSpeech.speak(
                                        "Oops! Wrong! Try again.",
                                        TextToSpeech.QUEUE_FLUSH,
                                        null,
                                        null
                                    )
                                    selectedUppercase = null
                                    selectedLowercase = null
                                    return@clickable
                                }

                                if (selectedUppercase != null && selectedLowercase != null) {
                                    if (selectedUppercase == correctUppercase && selectedLowercase == correctLowercase) {
                                        feedback = "\uD83D\uDE0AHurray! It's correct!"
                                        textToSpeech.speak(
                                            "Hurray! It's correct!",
                                            TextToSpeech.QUEUE_FLUSH,
                                            null,
                                            null
                                        )
                                    } else {
                                        feedback = "\uD83D\uDE41Wrong! Try again."
                                        textToSpeech.speak(
                                            "Oops! Wrong! Try again.",
                                            TextToSpeech.QUEUE_FLUSH,
                                            null,
                                            null
                                        )
                                        selectedUppercase = null
                                        selectedLowercase = null
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letter,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            feedback,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (feedback.contains("Hurray")) Color.Blue else Color.Red
        )
    }
}
