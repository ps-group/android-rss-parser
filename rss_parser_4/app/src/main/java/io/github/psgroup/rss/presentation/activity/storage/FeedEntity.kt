package io.github.psgroup.rss.presentation.activity.storage

data class FeedEntity(
    var id: Long = 0L,
    var title: String = "",
    var description: String = "",
    var articles: List<ArticleEntity> = listOf()
)
