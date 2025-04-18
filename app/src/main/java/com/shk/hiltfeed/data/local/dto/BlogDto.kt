package com.shk.hiltfeed.data.local.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity("blogs")
@Parcelize
data class BlogDto (
    @PrimaryKey
    val id: Long,
    val title: String,
    val imageUrl: String,
    val date: String,
    val content: String,
    val excerpt: String
): Parcelable