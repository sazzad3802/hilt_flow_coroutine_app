package com.shk.hiltfeed.data.models

import com.shk.hiltfeed.data.local.dto.BlogDto
import java.io.Serializable

data class BlogItem(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val date: String,
    val content: String,
    val excerpt: String
): Serializable {
    companion object {
        fun toBlogDtoList(items: List<BlogItem>?): List<BlogDto> {
            val mDtoList = mutableListOf<BlogDto>()
            items?.forEach {
                mDtoList.add(toBlogDto(it))
            }
            return mDtoList
        }

        private fun toBlogDto(blogItem: BlogItem): BlogDto {
            return BlogDto(
                id = blogItem.id,
                title = blogItem.title,
                imageUrl = blogItem.imageUrl,
                date = blogItem.date,
                content = blogItem.content,
                excerpt = blogItem.excerpt,
            )
        }
    }
}
