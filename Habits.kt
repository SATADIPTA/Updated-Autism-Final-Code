package com.example.firebase.pages

import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.R
import java.util.*

@Composable
fun Habits(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Initialize TTS and MediaPlayer safely
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
        mediaPlayer = MediaPlayer.create(context, R.raw.applause)
    }

    // Release resources when leaving Composable
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
            mediaPlayer?.release()
        }
    }

    // Render screen when TTS is ready
    tts?.let { safeTts ->
        HabitViewer(tts = safeTts, applausePlayer = mediaPlayer)
    }
}

data class HabitPair(
    val goodImage: Int,
    val goodLabel: String,
    val badImage: Int,
    val badLabel: String
)

@Composable
fun HabitViewer(tts: TextToSpeech, applausePlayer: MediaPlayer?) {
    val habits = listOf(
        HabitPair(R.drawable.brushing, "Brushing your teeth", R.drawable.no_brush, "Not brushing your teeth"),
        HabitPair(R.drawable.wash_hands, "Washing hands before eating", R.drawable.no_wash, "Not washing hands before eating"),
        HabitPair(R.drawable.throw_trash, "Throwing trash in the bin", R.drawable.littering, "Throwing trash on the ground"),
        HabitPair(R.drawable.say_thanks, "Saying please and thank you", R.drawable.shouting, "Shouting or grabbing"),
        HabitPair(R.drawable.eat_veg, "Eating vegetables", R.drawable.eat_junk, "Eating too much junk food"),
        HabitPair(R.drawable.use_tissue, "Using tissue when sneezing", R.drawable.no_tissue, "Sneezing without covering mouth"),
        HabitPair(R.drawable.share_toys, "Sharing toys", R.drawable.snatch_toys, "Snatching toys"),
        HabitPair(R.drawable.homework, "Doing homework on time", R.drawable.watch_tv, "Watching too much TV"),
        HabitPair(R.drawable.sleep_early, "Sleeping early", R.drawable.sleep_late, "Staying up late")
    )

    var currentIndex by remember { mutableStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }

    LaunchedEffect(currentIndex, isFinished) {
        if (isFinished) {
            applausePlayer?.start()
            tts.speak("You have finished learning habits.", TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            val habit = habits[currentIndex]
            val speech = "Good habit: ${habit.goodLabel}. Bad habit: ${habit.badLabel}"
            tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)) // Light blue background
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isFinished) {
            Text("You have finished learning habits!", fontSize = 24.sp, fontWeight = FontWeight.Bold,color=Color.Black)
            Button(onClick = {
                currentIndex = 0
                isFinished = false
            }) {
                Text("Restart")
            }
        } else {
            Text("Learn Good and Bad Habits", fontSize = 22.sp, fontWeight = FontWeight.Bold,color=Color.Black)

            val habit = habits[currentIndex]

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Good Habit
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFC8E6C9))
                        .padding(16.dp)
                    ) {
                    Image(
                        painter = painterResource(id = habit.goodImage),
                        contentDescription = "Good Habit",
                        modifier = Modifier.size(220.dp)
                    )
                    Text("✅ Good", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,color=Color.Green)
                    Text(habit.goodLabel, fontSize = 18.sp,color=Color.Black, fontWeight = FontWeight.Medium)
                }

                // Bad Habit
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFCDD2))
                        .padding(16.dp)
                    ) {
                    Image(
                        painter = painterResource(id = habit.badImage),
                        contentDescription = "Bad Habit",
                        modifier = Modifier.size(220.dp)
                    )
                    Text("❌ Bad", fontSize = 20.sp, fontWeight = FontWeight.Bold, color=Color.Red)
                    Text(habit.badLabel, fontSize = 18.sp,color=Color.Black, fontWeight = FontWeight.Medium)
                }
            }

            Button(onClick = {
                if (currentIndex < habits.lastIndex) {
                    currentIndex++
                } else {
                    isFinished = true
                }
            }) {
                Text("Next")
            }
        }
    }
}
