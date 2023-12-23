package com.atg.gallerytask.data.remote

import com.atg.gallerytask.data.model.PhotoResponse
import retrofit2.Response

class RemoteDataSource(private var photoApi: PhotoApi) {

    suspend fun getRecentPhotos(page: Int): Response<PhotoResponse> {
        return photoApi.getRecentPhotos(page)
    }
}