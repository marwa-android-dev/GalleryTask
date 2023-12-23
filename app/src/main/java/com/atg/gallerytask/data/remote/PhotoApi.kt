package com.atg.gallerytask.data.remote

import com.atg.gallerytask.data.model.PhotoResponse
import com.atg.gallerytask.utils.Constant.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApi {

    @GET("services/rest/")
    suspend fun getRecentPhotos(
        @Query("page") page: Int = 1,
        @Query("method") method: String = "flickr.photos.getRecent",
        @Query("per_page") perPage: Int = 20,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1,
        @Query("extras") extras: String = "url_s"
    ): Response<PhotoResponse>
}




