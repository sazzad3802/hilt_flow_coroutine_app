package com.shk.hiltfeed.features.blog_list.model.remote

import com.shk.hiltfeed.features.blog_list.model.data.BlogResponse
import retrofit2.Call
import retrofit2.http.GET

interface BlogApiInterface {

    @GET("posts")
    fun getBlogList() : Call<List<BlogResponse>>
}