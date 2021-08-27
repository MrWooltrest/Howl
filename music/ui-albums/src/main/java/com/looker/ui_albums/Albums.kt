package com.looker.ui_albums

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.looker.components.backgroundGradient
import com.looker.components.rememberDominantColorState
import com.looker.data_albums.data.Album
import com.looker.ui_albums.components.AlbumsCard
import com.looker.ui_albums.components.AlbumsExtensions.artworkUri
import com.looker.ui_albums.components.AlbumsItem
import com.looker.ui_songs.SongsList
import kotlinx.coroutines.launch

@Composable
fun Albums() {
    Albums(modifier = Modifier.fillMaxSize())
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Albums(
    modifier: Modifier = Modifier,
    viewModel: AlbumsViewModel = viewModel()
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background
    ) {

        val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()

        val albumsList = viewModel.getAlbumsList()
        val currentAlbum = viewModel.currentAlbum.value

        AlbumsList(
            albumsList = albumsList,
            onAlbumClick = {
                viewModel.albumIndex = albumsList.indexOf(it)
                scope.launch { state.show() }
            }
        )

        val sheetColor = rememberDominantColorState()

        LaunchedEffect(viewModel.albumIndex) {
            launch {
                sheetColor.updateColorsFromImageUrl(currentAlbum.albumId.artworkUri.toString())
            }
        }

        BottomSheets(
            state = state,
            sheetContent = {
                Column(
                    modifier = Modifier.backgroundGradient(sheetColor.color.copy(0.4f))
                ) {
                    Spacer(Modifier.height(50.dp))
                    AlbumsItem(
                        modifier = Modifier.fillMaxWidth(),
                        album = currentAlbum,
                        imageSize = 250.dp
                    )
                    SongsList(songsList = viewModel.getSongsPerAlbum(currentAlbum.albumId))
                }
            }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsList(
    albumsList: List<Album>,
    onAlbumClick: (Album) -> Unit
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = true
        )
    ) {
        items(albumsList) { album ->
            AlbumsCard(album = album) {
                onAlbumClick(album)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun BottomSheets(
    modifier: Modifier = Modifier,
    state: ModalBottomSheetState,
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit = {}
) {
    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = state,
        sheetContent = sheetContent,
        content = content,
        sheetBackgroundColor = MaterialTheme.colors.background,
        scrimColor = MaterialTheme.colors.background.copy(0.1f),
        sheetShape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)
    )
}