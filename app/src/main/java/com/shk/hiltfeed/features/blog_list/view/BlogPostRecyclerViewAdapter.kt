package com.shk.hiltfeed.features.blog_list.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.shk.hiltfeed.R
import com.shk.hiltfeed.features.blog_details.BlogDetailsActivity
import com.shk.hiltfeed.data.models.BlogItem
import com.shk.hiltfeed.databinding.ItemBlogPostBinding

class BlogPostRecyclerViewAdapter(private val blogList: List<BlogItem>) :
    RecyclerView.Adapter<BlogPostRecyclerViewAdapter.BlogPostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogPostViewHolder {
        val binding = ItemBlogPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlogPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogPostViewHolder, position: Int) {
        val blogPost = blogList[position]
        holder.bind(blogPost)
    }

    override fun getItemCount(): Int {
        return blogList.size
    }

    inner class BlogPostViewHolder(private val binding: ItemBlogPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(blogPost: BlogItem) {
//            Glide.with(itemView).load(blogPost.imageUrl).into(binding.imageViewFeatured)

            binding.imageViewFeatured.load(blogPost.imageUrl) {
                placeholder(R.drawable.ic_launcher_foreground)
                error(R.drawable.ic_launcher_foreground)
                crossfade(true)
//                transformations(CircleCropTransformation())
            }

            binding.textViewTitle.text = blogPost.title
            binding.textViewModifiedDate.text = blogPost.date

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, BlogDetailsActivity::class.java)
                intent.putExtra("blog", blogPost)
                binding.root.context.startActivity(intent)
            }
        }
    }
}