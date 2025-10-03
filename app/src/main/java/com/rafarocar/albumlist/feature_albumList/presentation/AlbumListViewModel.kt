package com.rafarocar.albumlist.feature_albumList.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import com.rafarocar.albumlist.feature_albumList.domain.useCase.GetAlbumsUseCase
import com.rafarocar.albumlist.feature_albumList.domain.useCase.RefreshAlbumsUseCase
import com.rafarocar.albumlist.feature_albumList.presentation.intents.AlbumListEffect
import com.rafarocar.albumlist.feature_albumList.presentation.intents.AlbumListIntent
import com.rafarocar.albumlist.feature_albumList.presentation.intents.AlbumListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val refreshAlbumsUseCase: RefreshAlbumsUseCase
) : ViewModel() {

    companion object {
        const val TAG = "AlbumListViewModel"
    }

    private val _state = MutableStateFlow(AlbumListState())
    val state: StateFlow<AlbumListState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AlbumListEffect>(replay = 1)
    val effect: SharedFlow<AlbumListEffect> = _effect.asSharedFlow()


    init {
        viewModelScope.launch {
            Log.i(TAG, "Starting viewModel and retrieving albums")
            handleIntent(AlbumListIntent.LoadAlbums)
        }
    }

    /**
     * Handle intents to decide which action to perform based on the AlbumListIntent
     * @param intent The intent to handle
     */
    fun handleIntent(intent: AlbumListIntent) {
        Log.i(TAG, "Handling intent: $intent")
        when (intent) {
            is AlbumListIntent.LoadAlbums -> loadAlbums()
            is AlbumListIntent.RefreshAlbums -> refreshAlbums()
        }
    }

    /**
     * Retrieve the albums pagination
     */
    fun retrieveAlbums(): Flow<PagingData<Album>>? {
        Log.i(TAG, "Retrieving albums")
        return _state.value.albums
    }

    /**
     * Retrieve the albums from the UseCase
     */
    private fun loadAlbums() {
        Log.i(TAG, "Loading paged albums")
        val pagingFlow = getAlbumsUseCase().cachedIn(viewModelScope)

        _state.update { it.copy(albums = pagingFlow, isLoading = false) }
    }

    /**
     * Refresh the albums from the UseCase
     */

    private fun refreshAlbums() {
        viewModelScope.launch {
            try {
                Log.i(TAG, "Refreshing albums")
                refreshAlbumsUseCase()
            } catch (e: Exception) {
                Log.e(TAG, "Error refreshing albums", e)
                _effect.emit(AlbumListEffect.ShowError(e.message ?: "Unknown error"))
            }

        }
    }
}