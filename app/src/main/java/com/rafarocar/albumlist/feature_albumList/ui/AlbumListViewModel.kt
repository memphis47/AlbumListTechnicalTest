package com.rafarocar.albumlist.feature_albumList.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import com.rafarocar.albumlist.feature_albumList.domain.useCase.GetAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel() {

    companion object {
        const val TAG = "AlbumListViewModel"
    }

    private val _albumList = MutableStateFlow<List<Album>>(emptyList())
    val albumList: StateFlow<List<Album>> = _albumList
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        retrieveAlbumList()
    }

    fun retrieveAlbumList() {
        viewModelScope.launch {
            Log.i(TAG, "fetching album list")
            _loading.value = true
            _error.value = null
            try {
                _albumList.value = getAlbumsUseCase()
                Log.i(TAG, "album list retrieved")
            } catch (e: Exception) {
                Log.i(TAG, "Error retrieving album list: ${e.message}")
                _error.value = e.message
            } finally {
                Log.i(TAG, "Dismissing loading")
                _loading.value = false
            }
        }
    }
}