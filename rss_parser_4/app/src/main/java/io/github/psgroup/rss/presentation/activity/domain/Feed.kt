package io.github.psgroup.rss.presentation.activity.domain

data class Feed(
    val title: String,
    val description: String,
    val articles: List<Article>
)
