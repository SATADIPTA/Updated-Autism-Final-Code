package com.example.firebase

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebase.pages.*

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("Home") {
            HomePage(modifier, navController, authViewModel)
        }
        composable("SignUp") {
            SignUp(modifier, navController, authViewModel)
        }
        composable("LanguageGame") {
            LanguageGamePage(modifier, navController)
        }

        composable("OutlinesGame") {
            OutlinesGamePage(modifier, navController)
        }
        composable("PatchesGame") {
            PatchGame(modifier, navController)
        }
        composable("DayGame") {
            Day(modifier, navController)
        }
        composable("MonthGame") {
            Month(modifier, navController)
        }
        composable("ColourGame") {
            Colour(modifier, navController)
        }
        composable("AlphabetGame") {
            Alphabet(modifier, navController)
        }
        composable("GeneralKnowledge") {
            GeneralKnowledge(modifier, navController)
        }
        composable("UpperCaseLetterLearningGame") {
            UpperCaseLetters(modifier, navController)
        }
        composable("LowerCaseLetterLearningGame") {
            LowerCaseLetters(modifier, navController)
        }
        composable("NumberLearningGame") {
            Numbers(modifier, navController)
        }
        composable("EmotionRecognitionGame") {
            EmotionRecognitionGame(modifier, navController)
        }
        composable("FruitAnimalMatchingGame") {
            FruitAnimalMatchingGame(modifier, navController)
        }
        composable("FruitAnimalLearning") {
            FruitAnimalLearning(modifier, navController)
        }
        composable("ShapesLearning") {
            ShapesLearning(modifier, navController)
        }
        composable("EmotionLearning") {
            EmotionLearning(modifier, navController)
        }
        composable("NameLearning") {
            NameLearning(modifier, navController)
        }
        composable("FruitsGame") {
            FruitsGame(modifier, navController)
        }
        composable("ColoursGame") {
            ColoursGame(modifier, navController)
        }
        composable("BirdsGame") {
            BirdsGame(modifier, navController)
        }

        composable("Animal") {
            Animal(modifier, navController)
        }
        composable("Fruits") {
            Fruits(modifier, navController)
        }
        composable("SoundIdentification") {
            SoundIdentification(modifier, navController)
        }
        composable("ObjectIdentification") {
            ObjectIdentification(modifier, navController)
        }
        composable("BodyPartsLearning") {
            BodyPartsLearning(modifier, navController)
        }
        composable("Habits") {
            Habits(modifier, navController)
        }
        composable("RhymesLearning") {
            RhymesLearning(modifier, navController)
        }
        composable("BaaBaaScreen") {
            PoemScreen(
                //poemText = "🐑Baa, baa, black sheep,\n\n🐑Have you any wool?\n\n🧒Yes sir, yes sir,\n\n🧒Three bags full...",
                poemText = "🐑 Baa, baa, black sheep,\n\nHave you any wool?\n\n🐑 Yes sir, yes sir,\n\nThree bags full.\n\nOne for the master,\n\nOne for the dame,\n\nAnd one for the little boy\n\nWho lives down the lane. 🧒",
                audioRes = R.raw.baa_baa_black_sheep
            )
        }
        composable("TwinkleScreen") {
            PoemScreen(
                //poemText = "Twinkle, twinkle, little star,\nHow I wonder what you are!\nUp above the world so high,\nLike a diamond in the sky...",
                poemText = "🌟 Twinkle, twinkle, little star,\n\nHow I wonder what you are!\n\n✨ Up above the world so high,\n\nLike a diamond in the sky.🌙 ",
                audioRes = R.raw.twinkle_twinkle_little_star
            )
        }
        composable("JackAndJillScreen") {
            PoemScreen(
                poemText = "👫 Jack and Jill went up the hill\n\nTo fetch a pail of water.\n\n🪣 Jack fell down and broke his crown \uD83D\uDC51,\n\nAnd Jill came tumbling after. 🤕",
                audioRes = R.raw.jack_and_jill  // Make sure you have jack_and_jill.mp4 in res/raw
            )
        }

        composable("HumptyDumptyScreen") {
            PoemScreen(
                poemText = "🥚 Humpty Dumpty sat on a wall,\n\nHumpty Dumpty had a great fall. 💥\n\nAll the king's horses and all the king's men\n\nCouldn't put Humpty together again. 🐎👑",
                audioRes = R.raw.humpty_dumpty  // Make sure you have humpty_dumpty.mp4 in res/raw
            )
        }


    }
}
