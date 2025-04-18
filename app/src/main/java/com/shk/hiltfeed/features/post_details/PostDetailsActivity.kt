package com.shk.hiltfeed.features.post_details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.shk.hiltfeed.R
import com.shk.hiltfeed.data.local.dto.PostDto
import com.shk.hiltfeed.databinding.ActivityPostDetailsBinding
import com.shk.hiltfeed.utils.AppKeys
import com.shk.hiltfeed.features.user_details.UserDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostDetailsBinding
    private val viewModel: PostDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        val post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(AppKeys.POST, PostDto::class.java)
        } else {
            intent.getParcelableExtra(AppKeys.POST)
        }

        if (post != null) {
            binding.post = post
            viewModel.fetchUser(post.userId)
        }

        setupUserName()

        binding.userNameTv.setOnClickListener {
            startActivity(Intent(this@PostDetailsActivity, UserDetailsActivity::class.java).apply {
                putExtra(AppKeys.USER, viewModel.userDto)
            })
        }
    }

    private fun setupUserName() {
        lifecycleScope.launch {
            viewModel.user.collectLatest {
                binding.userNameTv.text = getString(R.string.posted_by, it.firstName)
            }
        }
    }
}