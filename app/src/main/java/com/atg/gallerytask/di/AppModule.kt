package com.atg.gallerytask.di

import com.atg.gallerytask.data.PhotoRepository
import com.atg.gallerytask.data.remote.PhotoApi
import com.atg.gallerytask.data.remote.RemoteDataSource
import com.atg.gallerytask.utils.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providePhotoService(retrofit: Retrofit): PhotoApi {
        return retrofit.create(PhotoApi::class.java)
    }

    @Singleton
    @Provides
    fun providePhotoRepository(remoteDataSource: RemoteDataSource): PhotoRepository {
        return PhotoRepository(remoteDataSource)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(photoApi: PhotoApi): RemoteDataSource {
        return RemoteDataSource(photoApi)
    }
}