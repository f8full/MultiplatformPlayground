package com.halcyonmobile.multiplatformplayground.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.halcyonmobile.multiplatformplayground.R
import com.halcyonmobile.multiplatformplayground.model.ui.ApplicationUiModel
import com.halcyonmobile.multiplatformplayground.ui.theme.AppTheme
import com.halcyonmobile.multiplatformplayground.viewmodel.FavouritesViewModel
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@Composable
fun FavouriteScreen(onApplicationClicked: (ApplicationUiModel.App) -> Unit) {
    val viewModel = remember {
        FavouritesViewModel()
    }
    val favourites by viewModel.favourites.collectAsState()
    val state by viewModel.state.collectAsState()

    Column {
        Text(
            text = stringResource(id = R.string.favourites),
            style = AppTheme.typography.h4,
            modifier = Modifier.statusBarsPadding().padding(horizontal = 16.dp, vertical = 8.dp)
        )
        when (state) {
            FavouritesViewModel.State.LOADING -> Box(
                Modifier.align(Alignment.CenterHorizontally).weight(1f)
            ) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            FavouritesViewModel.State.NORMAL ->
                Applications(
                    items = favourites,
                    onApplicationClicked = onApplicationClicked
                )
            // TODO extract error to separate composable
            FavouritesViewModel.State.ERROR -> Column(
                Modifier.align(Alignment.CenterHorizontally).weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.general_error),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = AppTheme.typography.body1
                )
                Button(onClick = { viewModel.loadFavourites() }) {
                    Text(text = stringResource(id = R.string.retry))
                }
            }
        }
    }
}