package com.example.firebase.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.firebase.R

@Composable
fun RhymesLearning(modifier: Modifier = Modifier, navController: NavController) {
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFE4B5), Color(0xFFFFC0CB), Color(0xFFADD8E6)) // light orange, pink, blue
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.baa_baa_black_sheep),
            contentDescription = "Baa Baa Black Sheep",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable {
                    navController.navigate("BaaBaaScreen")
                }
        )

        Image(
            painter = painterResource(id = R.drawable.twinkle_twinkle_little_star),
            contentDescription = "Twinkle Twinkle",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable {
                    navController.navigate("TwinkleScreen")
                }
        )
        Image(
            painter = painterResource(id = R.drawable.jack_and_jill),
            contentDescription = "Jack and Jill",
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .clickable {
                    navController.navigate("JackAndJillScreen")
                }
        )
        Image(
            painter = painterResource(id = R.drawable.humpty_dumpty),
            contentDescription = "Humpty Dumpty",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable {
                    navController.navigate("HumptyDumptyScreen")
                }
        )
    }
}
