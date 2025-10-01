package com.rafarocar.albumlist.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rafarocar.albumlist.feature_albumList.navigation.ALBUM_LIST_GRAPH_ROUTE
import com.rafarocar.albumlist.feature_albumList.navigation.searchNavGraph

/**
 * Navigation graph for the app
 */
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = ALBUM_LIST_GRAPH_ROUTE) {
        searchNavGraph(navController = navController)
    }
}

