package com.shk.hiltfeed.features.blog_list.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shk.hiltfeed.data.local.NewsFeedDb
import com.shk.hiltfeed.data.models.BlogItem
import com.shk.hiltfeed.data.models.Post
import com.shk.hiltfeed.features.blog_list.model.BlogListRepository
import com.shk.hiltfeed.features.blog_list.model.ModelCallback
import com.shk.hiltfeed.features.blog_list.model.data.BlogResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BlogListViewModel @Inject constructor(
    private val model: BlogListRepository,
    private var newsFeedDb: NewsFeedDb
) : ViewModel() {

    val showLoaderLiveData = MutableLiveData<Boolean>()
    val showErrorLiveData = MutableLiveData<String>()
    val blogListUiModelLiveData = MutableLiveData<List<BlogItem>>()

    private var isDataLoaded = false

    fun getBlogList(showLoader: Boolean) {
        if (isDataLoaded) return

        showLoaderLiveData.postValue(showLoader)

        model.getBlogList(object : ModelCallback {
            override fun onSuccess(blogResponseList: List<BlogResponse>) {
                val blogListUiModel = getBlogUiModelList(blogResponseList)
                showLoaderLiveData.postValue(false)
                blogListUiModelLiveData.postValue(blogListUiModel)
                isDataLoaded = true
            }

            override fun onError(error: String) {
                showLoaderLiveData.postValue(false)
                showErrorLiveData.postValue(error)
            }
        })

    }

    private fun getBlogUiModelList(blogResponseList: List<BlogResponse>): List<BlogItem> {
        val blogUiModelList = mutableListOf<BlogItem>()

        blogResponseList.forEach {
            val blogUiModel = BlogItem(
                id = -1,
                title = it.title.rendered,
                imageUrl = it.jetpackFeaturedMediaUrl,
                date = getFormatterDate(it.date),
                content = it.content.rendered,
                excerpt = it.excerpt.rendered
            )
            blogUiModelList.add(blogUiModel)
        }

        val postsFromApi = BlogItem.toBlogDtoList(blogUiModelList)

        viewModelScope.launch(Dispatchers.IO) {
            newsFeedDb.blogDao().insertBlogs(postsFromApi)
        }

        return blogUiModelList
    }

    private fun getFormatterDate(dateInput: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

        val blogModifiedDate = inputFormat.parse(dateInput)

        return outputFormat.format(blogModifiedDate)
    }
}