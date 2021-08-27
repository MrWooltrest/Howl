package com.looker.ui_albums.components

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.itemSize
import com.looker.components.HowlImage
import com.looker.components.MaterialCard
import com.looker.components.WrappedText
import com.looker.components.rememberDominantColorState
import com.looker.data_albums.data.Album
import com.looker.ui_albums.components.AlbumsExtensions.artworkUri
import kotlinx.coroutines.launch

object AlbumsExtensions {

    val Long.artworkUri: Uri?
        get() = Uri.parse("content://media/external/audio/albumart/$this")
}

@Composable
fun AlbumsCard(
    modifier: Modifier = Modifier,
    album: Album,
    onClick: () -> Unit = {}
) {
    val cardWidth = LocalContext.current.itemSize(false, 2, 20.dp)
    AlbumsCard(
        modifier.padding(10.dp), album, cardWidth, onClick
    )
}

@Composable
private fun AlbumsCard(
    modifier: Modifier = Modifier,
    album: Album,
    cardWidth: Dp,
    onClick: () -> Unit
) {
    val backgroundColor = rememberDominantColorState()

    LaunchedEffect(album) {
        launch {
            backgroundColor.updateColorsFromImageUrl(album.albumId.artworkUri.toString())
        }
    }

    MaterialCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = 0.dp,
        backgroundColor = backgroundColor.color.copy(0.4f),
        onClick = onClick
    ) {
        AlbumsItem(
            album = album,
            imageSize = cardWidth
        )
    }
}

@Composable
fun AlbumsItem(
    modifier: Modifier = Modifier,
    album: Album,
    imageSize: Dp
) {
    Column(
        modifier = modifier
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HowlImage(
            data = album.albumId.artworkUri,
            modifier = Modifier.size(imageSize)
        )
        AlbumsItemText(
            modifier = Modifier.padding(horizontal = 8.dp),
            albumName = album.albumName,
            artistName = album.artistName
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun AlbumsItemText(
    modifier: Modifier = Modifier,
    albumName: String,
    artistName: String,
    textColor: Color = MaterialTheme.colors.onBackground
) {
    WrappedText(
        modifier = modifier,
        text = albumName,
        textAlign = TextAlign.Center,
        textColor = textColor
    )
    WrappedText(
        modifier = modifier,
        text = artistName,
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center,
        textColor = textColor
    )
}