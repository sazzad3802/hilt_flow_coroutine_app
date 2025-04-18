package com.shk.hiltfeed.data.models

import com.shk.hiltfeed.data.local.dto.PostDto

data class PostData(
    val posts: List<Post>,
    val total: Long,
    val skip: Long,
    val limit: Long
)

data class Post(
    val id: Long,
    val title: String,
    val body: String,
    val tags: List<String>,
    val reactions: Reactions,
    val views: Long,
    val userId: Long,
    val likes: Long,
    val dislikes: Long
) {
    companion object {
        fun toPostDtoList(posts: List<Post>?): List<PostDto> {
            val postDtoList = mutableListOf<PostDto>()
            posts?.forEach {
                postDtoList.add(toPostDto(it))
            }
            return postDtoList
        }

        private fun toPostDto(post: Post): PostDto {
            return PostDto(
                id = post.id,
                title = post.title,
                body = post.body,
                tags = post.tags.toString(),
                views = post.views,
                userId = post.userId,
                likes = post.reactions.likes,
                dislikes = post.reactions.dislikes
            )
        }
    }
}

data class Reactions(
    val likes: Long,
    val dislikes: Long
)
