package com.rafarocar.albumlist.feature_albumList.presentation

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView


/**
 * Screen to call a WebView to show details of the album
 * @param url The url to show in the WebView
 * @param onBack The callback to navigate back to the previous screen
 */
@Composable
fun AlbumWebViewScreen(
    url: String,
    onBack: () -> Boolean
) {
    val context = LocalContext.current
    AndroidView(
        factory = {
            WebView(context).apply {
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                webViewClient = WebViewClient()
                loadUrl(url, mapOf("User-Agent" to "AlbumListApp/1.0"))
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}