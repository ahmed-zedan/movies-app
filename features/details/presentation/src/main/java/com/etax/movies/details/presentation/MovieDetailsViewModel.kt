package com.etax.movies.details.presentation

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = MovieDetailsViewModel.Factory::class)
class MovieDetailsViewModel @AssistedInject constructor(
    @Assisted val movieId: String,
): ViewModel() {


    @AssistedFactory
    interface Factory {
        fun create(movieId: String): MovieDetailsViewModel
    }
}
