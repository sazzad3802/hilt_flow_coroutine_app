package com.shk.hiltfeed.features.blog_list.model

import com.shk.hiltfeed.features.blog_list.model.data.BlogResponse

interface ModelCallback {
    fun onSuccess(blogResponseList: List<BlogResponse>)
    fun onError(error: String)
}