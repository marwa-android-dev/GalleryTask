package com.atg.gallerytask.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atg.gallerytask.data.PhotoRepository
import com.atg.gallerytask.data.model.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel@Inject constructor(private val photoRepository: PhotoRepository) : ViewModel() {

    private val _searchPhotosDataFlow: MutableStateFlow<List<Photo>> = MutableStateFlow(emptyList())
    val searchPhotosDataFlow: Flow<List<Photo>> = _searchPhotosDataFlow

    private val _networkErrorEvent = MutableSharedFlow<Unit>()
    val networkErrorEvent: SharedFlow<Unit> = _networkErrorEvent

    private val _query = MutableSharedFlow<String>()

    init {
        _query
            .debounce(500)
            .onEach { query -> searchForPhotos(query) }
            .launchIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        viewModelScope.launch {
            _query.emit(query)
        }
    }

    private suspend fun searchForPhotos(query: String) {
        Log.d(TAG, "Searching for photos with query: $query")
        try {
            if (query.isNotEmpty()) {
                Log.d(TAG, "Search successful")
                val results = photoRepository.searchPhotos(query).body()?.photos?.photoes
                if (results != null) {
                    _searchPhotosDataFlow.value = results
                }
            } else {
                _searchPhotosDataFlow.value = emptyList()
            }
        }catch (e: IOException) {
            _networkErrorEvent.emit(Unit)
            Log.e(TAG, "Network failure: ${e.message}")
        }catch (e: Exception) {
            Log.e(TAG, "Unexpected error: ${e.message}")
        }
    }
}
