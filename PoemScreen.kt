package com.example.firebase.pages

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firebase.R  // Make sure this import exists

@Composable
fun PoemScreen(poemText: String, audioRes: Int) {
    val context = LocalContext.current
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }

    // 🎵 Play the audio when screen appears
    LaunchedEffect(audioRes) {
        mediaPlayer = MediaPlayer.create(context, audioRes)
        mediaPlayer?.start()
    }

    // 🛑 Release the player when screen is removed
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    // 🎨 Background based on which poem is playing
    val backgroundBrush = when (audioRes) {
        R.raw.baa_baa_black_sheep -> Brush.verticalGradient(
            colors = listOf(

                Color(0xFFFFE082), // light yellow
                Color(0xFFFFA726), // orange
                Color(0xFFEF5350)  // sunset red
            )
        )

        R.raw.twinkle_twinkle_little_star -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFB2EBF2), // light sky blue
                Color(0xFF81D4FA),
                Color(0xFF4FC3F7)  // deeper sky blue
            )
        )
        R.raw.jack_and_jill -> Brush.verticalGradient(
            colors = listOf(
                //Color(0xFFFFCDD2), // soft pink
                //Color(0xFFF8BBD0), // light pink
                //Color(0xFFFFAB91)  // peach
                Color(0xFFCE93D8), // Medium Orchid
                Color(0xFFBA68C8), // Orchid (strong violet tone)
                Color(0xFFAB47BC), // Deep Violet

            )

        )
        R.raw.humpty_dumpty -> Brush.verticalGradient(
            colors = listOf(

                Color(0xFFDCEDC8), // pale green
                Color(0xFFA5D6A7), // gentle green
                Color(0xFF66BB6A)  // vibrant leafy green
            )

        )

        else -> Brush.verticalGradient(
            colors = listOf(Color.White, Color.LightGray)
        )
    }

    // 🖼️ UI Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = poemText,
            fontSize = 30
                .sp,
            fontWeight = FontWeight.Medium,
            color=Color.Black
        )
    }
}
