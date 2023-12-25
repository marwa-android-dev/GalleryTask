package com.atg.gallerytask.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.atg.gallerytask.data.model.Photo
import com.atg.gallerytask.data.PhotoRepository.Companion.NETWORK_PAGE_SIZE
import kotlinx.coroutines.delay
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class PhotoPagingSource(private val remoteDataSource: RemoteDataSource) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            // delay(3000)
            val response = remoteDataSource.getRecentPhotos(position)
            val photos = response.body()!!.photos.photoes

            val nextKey = if (photos.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = photos,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}