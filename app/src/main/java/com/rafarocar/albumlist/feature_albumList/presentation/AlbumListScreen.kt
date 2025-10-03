package com.rafarocar.albumlist.feature_albumList.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import com.rafarocar.albumlist.feature_albumList.presentation.intents.AlbumListIntent

/**
 * Screen to handle the states of pagination and decide which screen to show
 * Can show a loading screen, an empty screen or the list of albums
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumListScreen(
    albumListViewModel: AlbumListViewModel,
    onInfoClick: (String) -> Unit
) {

    albumListViewModel.retrieveAlbums()?.let { albums ->
        val state by albumListViewModel.state.collectAsState()
        val lazyAlbums = albums.collectAsLazyPagingItems()

        Scaffold(
        ) { paddingValues ->
            when (lazyAlbums.loadState.refresh) {
                is LoadState.Loading -> {
                    LoadingScreen()
                }

                is LoadState.Error -> {
                    if (lazyAlbums.itemCount == 0) {
                        EmptyAlbums(viewModel = albumListViewModel)
                    } else {
                        AlbumList(paddingValues, lazyAlbums, onInfoClick)
                    }
                }

                else -> {
                    AlbumList(paddingValues, lazyAlbums, onInfoClick)
                }
            }
        }
    } ?: EmptyAlbums(viewModel = albumListViewModel)
}

/**
 * Screen to show a loading indicator
 */
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

/**
 * Screen to show a message when there are no albums with a button to call the API again
 */
@Composable
fun EmptyAlbums(
    viewModel: AlbumListViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "No Albums has been found",
                tint = Color(0xFFFF8C00),
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No Albums has been Found",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please check your internet connection or try again",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.handleIntent(AlbumListIntent.LoadAlbums)
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                )
            ) {
                Text(text = "Try Again")
            }
        }
    }
}

/**
 * Screen to show the list of albums
 */
@Composable
private fun AlbumList(
    paddingValues: PaddingValues,
    lazyAlbums: LazyPagingItems<Album>,
    onInfoClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = paddingValues,
    ) {
        items(lazyAlbums.itemCount) { index ->
            lazyAlbums[index]?.let { album ->
                AlbumItem(
                    album = album,
                    onInfoClick = onInfoClick
                )
            }
        }

        if (lazyAlbums.loadState.append is LoadState.Loading) {
            item {
                LoadingItem()
            }
        }
    }

}

/**
 * Screen to show a loading indicator when retrieving other items
 */
@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(32.dp))
    }
}

/**
 * Item to show in the list of albums
 */
@Composable
fun AlbumItem(album: Album, onInfoClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp,
                top = 8.dp
            ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                val imageSize = 100.dp
                AsyncImage(
                    model = album.thumbnailURL,
                    contentDescription = album.title,
                    modifier =
                        Modifier
                            .size(imageSize)
                            .clip(RoundedCornerShape(12.dp))
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 40.dp)
                        .height(imageSize)
                ) {
                    Text(
                        text = album.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )

                    Text(
                        text = "Album info here",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.clickable {
                            onInfoClick(album.url)
                        }
                    )
                }
            }
        }
    }
}