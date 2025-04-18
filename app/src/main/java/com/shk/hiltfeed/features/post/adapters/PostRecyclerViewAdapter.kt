package com.shk.hiltfeed.features.post.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.shk.hiltfeed.R
import com.shk.hiltfeed.data.local.dto.PostDto
import com.shk.hiltfeed.databinding.PostRvItemBinding
import com.shk.hiltfeed.listeners.ItemClickListener

class PostRecyclerViewAdapter : RecyclerView.Adapter<PostRecyclerViewAdapter.PostViewHolder>() {
    private val postList = mutableListOf<PostDto>()
    private lateinit var itemClickListener: ItemClickListener

    private val imageResources = listOf(R.drawable.jc1, R.drawable.jc2, R.drawable.jc3)


    fun setData(postList: List<PostDto>,itemClickListener: ItemClickListener ) {
        this.postList.addAll(postList)
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(PostRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), itemClickListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        val imageRes = imageResources[position % imageResources.size]
        holder.bind(post, imageRes)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class PostViewHolder(
        private var postRvItemBinding: PostRvItemBinding,
        private var itemClickListener: ItemClickListener
    ) :
        ViewHolder(postRvItemBinding.root) {
        fun bind(postDto: PostDto, imageRes: Int) {
            postRvItemBinding.post = postDto
            postRvItemBinding.imageViewFeatured.setImageResource(imageRes)
            postRvItemBinding.root.setOnClickListener {
                itemClickListener.onClick(postDto)
            }
        }
    }
}