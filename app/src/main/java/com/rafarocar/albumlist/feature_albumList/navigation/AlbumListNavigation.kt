package com.rafarocar.albumlist.feature_albumList.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.rafarocar.albumlist.feature_albumList.ui.AlbumListScreen
import com.rafarocar.albumlist.feature_albumList.ui.AlbumListViewModel

// Graph routes
const val ALBUM_LIST_GRAPH_ROUTE = "album_list_graph"

const val ALBUM_LIST_ROUTE = "album_list"

/**
 * Navigation graph for the search screen
 * @param navController The navigation controller
 */
fun NavGraphBuilder.searchNavGraph(
    navController: NavController
) {
    navigation(
        route = ALBUM_LIST_GRAPH_ROUTE,
        startDestination = ALBUM_LIST_ROUTE
    ) {

        // Route to Search Screen
        composable(ALBUM_LIST_ROUTE) { backStackEntry ->
            val viewModel: AlbumListViewModel = hiltViewModel()
            AlbumListScreen(
                albumListViewModel = viewModel,
                navController = navController
            )
        }
    }
}