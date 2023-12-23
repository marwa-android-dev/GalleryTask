package com.atg.gallerytask.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.atg.gallerytask.data.PhotoRepository
import com.atg.gallerytask.data.model.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(private val photoRepository: PhotoRepository) : ViewModel() {

    private val _photosDataFlow: MutableStateFlow<PagingData<Photo>> = MutableStateFlow(PagingData.empty())
    val photosDataFlow: Flow<PagingData<Photo>> = _photosDataFlow

    init {
        setRecentPhotos()
    }

    private fun setRecentPhotos(){
        viewModelScope.launch {
            val response = photoRepository.getPhotosResultStream().cachedIn(viewModelScope)
            response.collect { pagingData ->
                _photosDataFlow.value = pagingData
            }
        }
    }
}