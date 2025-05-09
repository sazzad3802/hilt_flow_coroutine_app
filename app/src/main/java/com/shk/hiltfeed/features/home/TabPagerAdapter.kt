package com.shk.hiltfeed.features.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shk.hiltfeed.features.blog_list.view.BlogListFragment
import com.shk.hiltfeed.features.post.PostFragment

/*
class TabPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PostFragment()
            1 -> BlogListFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}*/
