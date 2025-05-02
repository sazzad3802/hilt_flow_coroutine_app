package com.shk.hiltfeed.features.post

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.shk.hiltfeed.databinding.ActivityPostBinding
import com.shk.hiltfeed.services.UserDataFetchService
import com.shk.hiltfeed.utils.AppKeys
import com.shk.hiltfeed.utils.ConstData
import com.shk.hiltfeed.features.post_details.PostDetailsActivity
import com.shk.hiltfeed.services.UserFetchWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostBinding
    private val viewModel: PostViewModel by viewModels()
    var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        binding.viewmodel = viewModel

        binding.lifecycleOwner = this
        setContentView(binding.root)


        handleSelectedPost()

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && !viewModel.isLoading.value) {
                    currentPage++
                    viewModel.fetchPosts(ConstData.limit, currentPage)
                }
            }
        })
//        startService(Intent(this, UserDataFetchService::class.java))
        UserFetchWorker.enqueue(this)

    }

    private fun handleSelectedPost() {
        lifecycleScope.launch {
            viewModel.selectedPost.collect{
                startActivity(Intent(this@PostActivity, PostDetailsActivity::class.java).apply {
                    putExtra(AppKeys.POST, it)
                })
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, UserDataFetchService::class.java))
    }
}