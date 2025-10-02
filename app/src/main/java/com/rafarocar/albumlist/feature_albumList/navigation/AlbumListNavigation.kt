package com.rafarocar.albumlist.feature_albumList.navigation

import android.net.Uri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.rafarocar.albumlist.feature_albumList.presentation.AlbumListScreen
import com.rafarocar.albumlist.feature_albumList.presentation.AlbumListViewModel
import com.rafarocar.albumlist.feature_albumList.presentation.AlbumWebViewScreen

// Graph routes
const val ALBUM_LIST_GRAPH_ROUTE = "album_list_graph"

const val ALBUM_WEB_VIEW_ROUTE = "album_web_view/{url}"

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
                onInfoClick = { url ->
                    navController.navigate("album_web_view/${Uri.encode(url)}")
                }
            )
        }

        // Route to Album WebView Screen
        composable(
            route = ALBUM_WEB_VIEW_ROUTE,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            AlbumWebViewScreen(url = url) {
                navController.popBackStack()
            }
        }
    }
}