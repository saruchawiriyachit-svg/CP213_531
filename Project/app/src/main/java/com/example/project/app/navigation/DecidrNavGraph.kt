package com.example.project.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project.ai.data.GeminiAiChatRepository
import com.example.project.ai.data.GeminiRecommendationRepository
import com.example.project.ai.data.remote.GeminiClient
import com.example.project.core.error.AppError
import com.example.project.core.error.Resource
import com.example.project.feature.chat.ui.ChatScreen
import com.example.project.feature.chat.ui.ChatViewModel
import com.example.project.feature.decision.domain.Decision
import com.example.project.feature.decision.domain.Recommendation
import com.example.project.feature.splash.SplashScreen
import com.example.project.ui.screens.HistoryScreen
import com.example.project.ui.screens.MainScreen
import com.example.project.ui.screens.ResultScreen

// ── Replace this with your Gemini API key ────────────────────────────────────
// Get a free key at: https://aistudio.google.com/app/apikey
private const val GEMINI_API_KEY = "AIzaSyBSPdCBoDq01D5jsG_apS0xPO2iaGDgL3I"

@Composable
fun DecidrNavGraph() {

    val navController  = rememberNavController()
    var currentDecision by remember { mutableStateOf<Decision?>(null) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val database = remember(context) {
        androidx.room.Room.databaseBuilder(
            context.applicationContext,
            com.example.project.feature.decision.data.local.DecidrDatabase::class.java,
            "decidr_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    val decisionRepo = remember(database) {
        com.example.project.feature.decision.data.OfflineFirstDecisionRepository(database.decidrDao)
    }

    // Shared Gemini service instance
    val geminiService  = remember { GeminiClient.geminiApiService }
    val recommendRepo  = remember { GeminiRecommendationRepository(geminiService, GEMINI_API_KEY) }
    val chatRepo       = remember { GeminiAiChatRepository(geminiService, GEMINI_API_KEY) }

    NavHost(
        navController = navController,
        startDestination = "splash",
        enterTransition  = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500)) + fadeIn(tween(500))
        },
        exitTransition   = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500)) + fadeOut(tween(500))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500)) + fadeIn(tween(500))
        },
        popExitTransition  = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500)) + fadeOut(tween(500))
        }
    ) {

        // ── Splash ────────────────────────────────────────────────────────────
        composable(
            route          = "splash",
            enterTransition = { fadeIn(tween(300)) },
            exitTransition  = { fadeOut(tween(600)) }
        ) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // ── Main ──────────────────────────────────────────────────────────────
        composable("main") {
            MainScreen(
                onNavigateToResult = { decision ->
                    currentDecision = decision
                    navController.navigate("result")
                },
                onNavigateToHistory = { navController.navigate("history") }
            )
        }

        // ── Result — calls Gemini to get real pros/cons ───────────────────────
        composable("result") {
            val decision = currentDecision

            if (decision != null && decision.options.isNotEmpty()) {

                // State: null = loading, non-null = done, error = show error screen
                var recommendation by remember { mutableStateOf<Recommendation?>(null) }
                var errorMsg       by remember { mutableStateOf<String?>(null) }
                // retryKey increments to re-trigger LaunchedEffect
                var retryKey       by remember { mutableStateOf(0) }

                // Fetch from Gemini — re-runs whenever retryKey changes
                LaunchedEffect(retryKey) {
                    recommendation = null
                    errorMsg       = null
                    when (val result = recommendRepo.getRecommendation(decision)) {
                        is Resource.Success -> {
                            recommendation = result.data
                            // Save decision and recommendation to local database
                            decisionRepo.saveDecisionWithRecommendation(decision, result.data)
                        }
                        is Resource.Error   -> {
                            errorMsg = when (result.exception) {
                                is AppError.RateLimitError ->
                                    "Jelly is getting too many requests right now 😅\nPlease wait a moment and try again!"
                                is AppError.NetworkError ->
                                    "Jelly can't connect to the internet 🌐\nPlease check your connection and try again."
                                is AppError.AiServiceUnavailable ->
                                    "Jelly's brain is temporarily unavailable 🐶\nPlease try again in a few seconds."
                                else ->
                                    result.exception.message ?: "Something went wrong. Please try again."
                            }
                        }
                    }
                }

                when {
                    // ── Loading ──────────────────────────────────────────────
                    recommendation == null && errorMsg == null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            androidx.compose.foundation.layout.Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Text(
                                    text     = "Jelly is thinking... 🐶",
                                    style    = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(top = 16.dp)
                                )
                            }
                        }
                    }
                    // ── Error ────────────────────────────────────────────────
                    errorMsg != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            androidx.compose.foundation.layout.Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(32.dp)
                            ) {
                                Text(text = "🐶", style = MaterialTheme.typography.displayMedium)
                                androidx.compose.foundation.layout.Spacer(
                                    modifier = Modifier.padding(top = 16.dp)
                                )
                                Text(
                                    text      = errorMsg!!,
                                    style     = MaterialTheme.typography.bodyLarge,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    color     = MaterialTheme.colorScheme.onBackground
                                )
                                androidx.compose.foundation.layout.Spacer(
                                    modifier = Modifier.padding(top = 24.dp)
                                )
                                androidx.compose.material3.Button(
                                    onClick = { retryKey++ },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Text("Try Again")
                                }
                                androidx.compose.foundation.layout.Spacer(
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                androidx.compose.material3.OutlinedButton(
                                    onClick  = { navController.popBackStack() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Text("Go Back")
                                }
                            }
                        }
                    }
                    // ── Success ──────────────────────────────────────────────
                    else -> {
                        ResultScreen(
                            options          = decision.options,
                            recommendation   = recommendation,
                            onBack           = { navController.popBackStack() },
                            onNavigateToChat = { navController.navigate("chat") }
                        )
                    }
                }
            } else {
                ResultScreen(
                    onBack           = { navController.popBackStack() },
                    onNavigateToChat = { navController.navigate("chat") }
                )
            }
        }

        // ── History ───────────────────────────────────────────────────────────
        composable("history") {
            HistoryScreen(
                onBack = { navController.popBackStack() },
                decisionRepo = decisionRepo
            )
        }

        // ── Chat — powered by Gemini ──────────────────────────────────────────
        composable("chat") {
            val decision = currentDecision
            val context  = decision?.let {
                "The user is deciding: '${it.query}'. " +
                "Options: ${it.options.joinToString(", ") { o -> o.title }}. " +
                "Factors: ${it.factors.joinToString(", ") { f -> "${f.name} (weight ${f.weight})" }}."
            } ?: ""

            val chatViewModel: ChatViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T =
                        ChatViewModel(
                            aiChatRepository = chatRepo,
                            initialContext   = context
                        ) as T
                }
            )

            val uiState by chatViewModel.uiState.collectAsState()

            ChatScreen(
                onNavigateBack = { navController.popBackStack() },
                uiState        = uiState,
                onSendMessage  = { chatViewModel.sendMessage(it) }
            )
        }
    }
}