package com.etax.movies.details.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.etax.movies.details.presentation.MovieDetailsScreen
import com.etax.movies.details.presentation.MovieDetailsViewModel
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsRoute(val id: String)

fun NavController.navigateToMovieDetails(
    movieId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = MovieDetailsRoute(movieId)) {
        navOptions()
    }
}

fun NavGraphBuilder.movieDetailsScreen(
    onBackClick: () -> Unit,
) {
    composable<MovieDetailsRoute> { entry ->
        val id = entry.toRoute<MovieDetailsRoute>().id
        MovieDetailsScreen(
            viewModel = hiltViewModel<MovieDetailsViewModel, MovieDetailsViewModel.Factory>(
                key = id,
            ) { factory ->
                factory.create(id)
            }
        )
    }
}
