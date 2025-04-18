package com.shk.hiltfeed.features.blog_list.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.shk.hiltfeed.features.blog_list.view_model.BlogListViewModel
import com.shk.hiltfeed.R
import com.shk.hiltfeed.databinding.ActivityBlogListBinding
import com.shk.hiltfeed.features.post.PostActivity
import com.shk.hiltfeed.listeners.ItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlogListActivity : AppCompatActivity(), ItemClickListener {

    private lateinit var binding: ActivityBlogListBinding
    private val viewModel: BlogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getBlogList(true)

        viewModel.showLoaderLiveData.observe(this) {
            binding.progressBar.isVisible = it ?: false
        }

        viewModel.showErrorLiveData.observe(this) { error ->
            if (error.isNotEmpty()) {
                binding.tvError.text = error
                binding.tvError.isVisible = true
            }
        }

        viewModel.blogListUiModelLiveData.observe(this) { blogList ->
            binding.swipeRefreshLayout.isRefreshing = false

            if (blogList.isNotEmpty()) {
                binding.tvError.isVisible = false
                val layoutManager = LinearLayoutManager(this)
                binding.recyclerView.layoutManager = layoutManager

                val adapter = BlogPostRecyclerViewAdapter(blogList)
                binding.recyclerView.adapter = adapter

                binding.progressBar.isVisible = false
            } else {
                binding.tvError.text = getString(R.string.empty_list_message)
                binding.tvError.isVisible = true
            }
        }

        viewModel.showErrorLiveData.observe(this) { error ->
            binding.swipeRefreshLayout.isRefreshing = false

            if (error.isNotEmpty()) {
                binding.tvError.text = error
                binding.tvError.isVisible = true
            }
        }

        binding.btnGoToDetail.setOnClickListener {
            onClick("GoToBlogDetail")
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getBlogList(false)
        }
    }

    override fun onClick(data: Any) {
        val intent = Intent(this, PostActivity::class.java)
        startActivity(intent)
    }
}