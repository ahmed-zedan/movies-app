package com.etax.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.etax.movieapp.ui.theme.MovieAppTheme
import com.etax.movies.details.presentation.navigation.movieDetailsScreen
import com.etax.movies.details.presentation.navigation.navigateToMovieDetails
import com.etax.movies.nowplaying.persentation.navigation.NowPlayingRoute
import com.etax.movies.nowplaying.persentation.navigation.nowPlayingScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MovieAppNavHost(modifier = Modifier.fillMaxSize().padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MovieAppNavHost(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NowPlayingRoute, modifier = modifier) {
        nowPlayingScreen(
            onMovieClick = { id ->
                navController.navigateToMovieDetails(id)
            }
        )

        movieDetailsScreen(
            onBackClick = { navController.popBackStack() }
        )
    }
}
