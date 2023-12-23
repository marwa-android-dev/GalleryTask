package com.atg.gallerytask.data.model

import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    val photos : Photos,
    val stat: String
)

data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    @SerializedName("photo")
    var photoes: List<Photo>,
)

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int,
    @SerializedName("url_s")
    val photoUrl : String,
    val height_s: Int,
    val width_s: Int
)