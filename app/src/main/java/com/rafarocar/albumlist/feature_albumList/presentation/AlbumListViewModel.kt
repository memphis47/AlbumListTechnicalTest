package com.rafarocar.albumlist.feature_albumList.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import com.rafarocar.albumlist.feature_albumList.domain.useCase.GetAlbumsUseCase
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel() {

    companion object {
        const val TAG = "AlbumListViewModel"
    }
    // Expose Flow<PagingData<Album>> directly
    val albums: Flow<PagingData<Album>> = getAlbumsUseCase()
        .cachedIn(viewModelScope)
}