package com.shk.hiltfeed.features.blog_details

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.shk.hiltfeed.data.models.BlogItem
import com.shk.hiltfeed.databinding.ActivityBlogDetailsBinding

class BlogDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBlogDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val blogResponse = intent.getSerializableExtra("blog") as BlogItem

        Glide.with(binding.imageViewFeatured)
            .load(blogResponse.imageUrl)
            .into(binding.imageViewFeatured)

        binding.textViewTitle.text = blogResponse.title
        binding.textViewDate.text = blogResponse.date
        binding.textViewExcerpt.text = Html.fromHtml(blogResponse.excerpt, Html.FROM_HTML_MODE_COMPACT)
        binding.textViewContent.text = Html.fromHtml(blogResponse.content, Html.FROM_HTML_MODE_COMPACT)
    }
}