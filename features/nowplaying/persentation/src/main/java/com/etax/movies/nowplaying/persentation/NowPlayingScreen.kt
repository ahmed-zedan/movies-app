package com.etax.movies.nowplaying.persentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.etax.movies.nowplaying.domain.entities.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NowPlayingScreen(
    onMovieClick: (String) -> Unit,
    viewModel: NowPlayingViewModel
) {
    val lazyGridState = rememberLazyGridState()
    val movies = viewModel.movies.collectAsLazyPagingItems()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Now Playing") }) }
    ) { padding ->
        when {
            movies.loadState.refresh is LoadState.Loading -> {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            }

            movies.loadState.refresh is LoadState.Error -> {
                ErrorState(
                    message = (movies.loadState.refresh as LoadState.Error).error.message ?: "",
                    onRetry = { movies.retry() },
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                MovieGrid(
                    movies = movies,
                    lazyGridState = lazyGridState,
                    onMovieClick = onMovieClick,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
private fun MovieGrid(
    movies: LazyPagingItems<Movie>,
    lazyGridState: LazyGridState,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        state = lazyGridState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(count = movies.itemCount) { index ->
            movies[index]?.let { movie ->
                MovieItem(
                    movie = movie,
                    onClick = { onMovieClick(movie.id.toString()) }
                )
            }
        }

        // Append loading or error state
        movies.loadState.append.let { appendState ->
            when (appendState) {
                is LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LoadingIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }

                is LoadState.Error -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ErrorState(
                            message = appendState.error.message ?: "",
                            onRetry = { movies.retry() },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                else -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieItem(
    movie: Movie,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2 / 3f)
    ) {
        AsyncImage(
            model = movie.posterPath,
            contentDescription = "poster ${movie.title}",
//            placeholder = painterResource(R.drawable.placeholder),
//            error = painterResource(R.drawable.error),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
        )

    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black.copy(alpha = 0.79f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onRetry,
                colors = ButtonDefaults.outlinedButtonColors().copy(
                    contentColor = Color(0xBF000000)
                )
            ) {
                Text("Try again")
            }
        }
    }
}



