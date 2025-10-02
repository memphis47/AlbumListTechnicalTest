package com.rafarocar.albumlist.feature_albumList.common

import androidx.recyclerview.widget.ListUpdateCallback
import com.rafarocar.albumlist.feature_albumList.domain.model.Album


/**
 * Simple test-only diff util
 */
object AlbumDiffUtilForTests : androidx.recyclerview.widget.DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Album, newItem: Album) = oldItem == newItem
}

/**
 * No-op list callback for AsyncPagingDataDiffer
 */
class NoopListCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}