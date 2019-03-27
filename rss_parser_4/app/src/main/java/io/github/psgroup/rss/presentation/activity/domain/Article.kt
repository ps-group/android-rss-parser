package io.github.psgroup.rss.presentation.activity.domain

data class Article(
    val title: String,
    val description: String,
    val dateInMillis: Long,
    val imageUrl: String?
)
