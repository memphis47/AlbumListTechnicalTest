package com.rafarocar.albumlist.feature_albumList.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import java.net.URLEncoder
import androidx.paging.compose.collectAsLazyPagingItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumListScreen(
    albumListViewModel: AlbumListViewModel,
    navController: NavController
) {

    val lazyAlbums = albumListViewModel.albums.collectAsLazyPagingItems()

    Scaffold(
    ) { paddingValues ->
        when (lazyAlbums.loadState.refresh) {
            is LoadState.Loading -> {
                LoadingScreen()
            }

            is LoadState.Error -> {
                EmptyAlbums(albumListViewModel)
            }

            else -> {
                AlbumList(paddingValues, lazyAlbums, navController)
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
fun EmptyAlbums(
    albumListViewModel: AlbumListViewModel
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

@Composable
private fun AlbumList(
    paddingValues: PaddingValues,
    lazyAlbums: LazyPagingItems<Album>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = paddingValues,
    ) {
        items(lazyAlbums.itemCount) { index ->
            lazyAlbums[index]?.let { album ->
                AlbumItem(album, navController)
            }
        }

        if (lazyAlbums.loadState.append is LoadState.Loading) {
            item {
                LoadingItem()
            }
        }
    }

}

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

@Composable
fun AlbumItem(album: Album, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                AsyncImage(
                    model = album.thumbnailURL,
                    contentDescription = album.title,
                    modifier =
                        Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                )

                Spacer(
                    modifier = Modifier
                        .width(8.dp)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 40.dp)
                ) {
                    Text(
                        text = album.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Album info here",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.clickable {
                            val encodedUrl = URLEncoder.encode(album.url, "UTF-8")
                            navController.navigate("albumDetail/$encodedUrl")
                        }
                    )
                }
            }


            IconButton(
                onClick = {
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                )
            }
        }
    }
}