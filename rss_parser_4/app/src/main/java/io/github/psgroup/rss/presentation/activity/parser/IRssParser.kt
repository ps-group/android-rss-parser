package io.github.psgroup.rss.presentation.activity.parser

import io.github.psgroup.rss.presentation.activity.domain.Article
import io.github.psgroup.rss.presentation.activity.domain.Feed

interface IRssParser {
    fun parse(data: ByteArray): ParserResult

    sealed class ParserResult {
        class Success(
            val feed: Feed,
            val articles: List<Article>
        ) : ParserResult()
        object Error : ParserResult()
    }
}