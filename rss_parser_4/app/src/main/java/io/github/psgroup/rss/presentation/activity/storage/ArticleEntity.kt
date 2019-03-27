package io.github.psgroup.rss.presentation.activity.storage

data class ArticleEntity(
    var id: Long = 0L,
    var title: String = "",
    var description: String = "",
    var dateInMillis: Long = 0,
    var imageUrl: String = ""
)