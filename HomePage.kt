package com.example.firebase.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebase.AuthState
import com.example.firebase.AuthViewModel
import com.example.firebase.R

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo("Home") { inclusive = true }
            }
        }
    }

    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        // Background Image with Transparency
        Image(
            painter = painterResource(id = R.drawable.alphabet_img),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.5f },
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 40.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // App Icon
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .padding(bottom = 16.dp)
            )

            // Welcome Text
            Text(
                text = "Welcome To Autism Helper",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Two columns inside a Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,

            ) {
                // Left Column - Learning Buttons
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Button(
                        onClick = { navController.navigate("AlphabetGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "ALPHABET LEARNING")
                    }
                    Button(
                        onClick = { navController.navigate("RhymesLearning") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "RHYMES LEARNING")
                    }
                    Button(
                        onClick = { navController.navigate("ShapesLearning") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "SHAPES LEARNING")
                    }

                    Button(
                        onClick = { navController.navigate("FruitAnimalLearning") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "FRUIT ANIMAL LEARNING")
                    }

                    Button(
                        onClick = { navController.navigate("DayGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "DAY NAME LEARNING")
                    }

                    Button(
                        onClick = { navController.navigate("MonthGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "MONTH NAME LEARNING")
                    }

                    Button(
                        onClick = { navController.navigate("BodyPartsLearning") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "BODY PARTS IMAGES")
                    }

                    Button(
                        onClick = { navController.navigate("Habits") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "HABITS LEARNING")
                    }

                    Button(
                        onClick = { navController.navigate("UpperCaseLetterLearningGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "UPPERCASE LETTER LEARNING")
                    }

                    Button(
                        onClick = { navController.navigate("LowerCaseLetterLearningGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "LOWERCASE LETTER LEARNING")
                    }

                    Button(
                        onClick = { navController.navigate("NumberLearningGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "NUMBER LEARNING")
                    }

                    Button(
                        onClick = { navController.navigate("EmotionLearning") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "EMOTION LEARNING")
                    }

                    Button(
                        onClick = { navController.navigate("NameLearning") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "NAME LEARNING")
                    }

                    Button(
                        onClick = { navController.navigate("BirdsGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "BIRDS GAME")
                    }


                }

                // Right Column - Game Buttons
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { navController.navigate("Animal") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "ANIMAL GAME")
                    }
                    Button(
                        onClick = { navController.navigate("SoundIdentification") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(80.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "SOUND RECOGNITION GAME")
                    }
                    Button(
                        onClick = { navController.navigate("OutlinesGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "OUTLINES GAME")
                    }
                    Button(
                        onClick = { navController.navigate("LanguageGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "LANGUAGE GAME")
                    }

                    Button(
                        onClick = { navController.navigate("ColourGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "COLOUR GAME")
                    }

                    Button(
                        onClick = { navController.navigate("GeneralKnowledge") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(80.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "GENERAL KNOWLEDGE GAME")
                    }
                    Button(
                        onClick = { navController.navigate("FruitAnimalMatchingGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(80.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "FRUIT ANIMAL MATCHING GAME")
                    }
                    Button(
                        onClick = { navController.navigate("EmotionRecognitionGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(80.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "EMOTION RECOGNITION GAME")
                    }

                    Button(
                        onClick = { navController.navigate("Fruits") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(42.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "FRUITS GAME")
                    }


                    Button(
                        onClick = { navController.navigate("ObjectIdentification") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(80.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "OBJECT IDENTIFICATION GAME")
                    }
                    Button(
                        onClick = { navController.navigate("PatchesGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "PATCHES GAME")
                    }
                    Button(
                        onClick = { navController.navigate("FruitsGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "FRUITS GAME")
                    }
                    Button(
                        onClick = { navController.navigate("ColoursGame") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(57.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "COLOURS GAME")
                    }


                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Out Button
            Button(
                onClick = { authViewModel.signout() },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Sign Out", color = Color.Red)
            }
        }
    }
}
