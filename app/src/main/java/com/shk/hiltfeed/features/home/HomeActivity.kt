package com.shk.hiltfeed.features.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.shk.hiltfeed.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    val singleThreadContext = newSingleThreadContext("CounterThread") // ensure single thread execution to avoid race problem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupTabLayout()
        setupUserCount()
    }

    private fun setupUserCount() {
        // for livedata approach
        /*homeViewModel.userCount.observe(this) { count ->
            binding.textUserCount.text = "$count Users"
            Snackbar.make(binding.root, count.toString(), Snackbar.LENGTH_SHORT).show()
        }*/

        // for flow approach
        lifecycleScope.launch(singleThreadContext) {
            // below "repeatOnLifecycle" helps surviving from configuration changes + otherwise flow keeps running even when ui is in bg, which causes memory leak and app crash
            // always need to use when collecting flow in activity / fragment
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.userCount
                    .combine(homeViewModel.lastCount) { count, lastCount ->
                        Pair(count, lastCount)
                    }
                    .collect { (count, lastCount) ->
                        withContext(Dispatchers.Main) {
                            binding.textUserCount.text = "$count Users"
                            if(count > lastCount){
                                Snackbar.make(binding.root, binding.textUserCount.text, Snackbar.LENGTH_SHORT).show()
                                homeViewModel.updateLastCount(count)
                            }
                    }
                }
            }
        }

    }

    private fun setupViewPager() {
        binding.mViewPager.adapter = TabPagerAdapter(this)
        binding.mViewPager.isUserInputEnabled = true
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.mTabLayout, binding.mViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Posts"
                1 -> "Blogs"
                else -> null
            }
        }.attach()
    }





    // to ensure thread safety and avoid race condition, introduce @volatile and synchronized
    companion object {
        @Volatile var count = 4
        fun increment() {
            synchronized(this) {
                count++
            }
        }
    }

}