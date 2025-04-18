package com.shk.hiltfeed.features.blog_list.model.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class BlogResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("date_gmt")
    val dateGmt: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("modified_gmt")
    val modifiedGmt: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("title")
    val title: Title,
    @SerializedName("content")
    val content: Content,
    @SerializedName("excerpt")
    val excerpt: Excerpt,
    @SerializedName("jetpack_featured_media_url")
    val jetpackFeaturedMediaUrl: String
) : Serializable

/*{
    @Keep
    data class Title(
        @SerializedName("rendered")
        val rendered: String
    ) : Serializable

    @Keep
    data class Content(
        @SerializedName("rendered")
        val rendered: String
    ) : Serializable

    @Keep
    data class Excerpt(
        @SerializedName("rendered")
        val rendered: String
    ) : Serializable
}*/


@Keep
data class Title(
    @SerializedName("rendered")
    val rendered: String
) : Serializable

@Keep
data class Content(
    @SerializedName("rendered")
    val rendered: String
) : Serializable

@Keep
data class Excerpt(
    @SerializedName("rendered")
    val rendered: String
) : Serializable