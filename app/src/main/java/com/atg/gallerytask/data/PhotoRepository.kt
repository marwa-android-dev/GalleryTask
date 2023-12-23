package com.atg.gallerytask.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.atg.gallerytask.data.model.Photo
import com.atg.gallerytask.data.remote.PhotoPagingSource
import com.atg.gallerytask.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class PhotoRepository(private var remoteDataSource: RemoteDataSource) {

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    fun getPhotosResultStream(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PhotoPagingSource(remoteDataSource) }
        ).flow
    }
}