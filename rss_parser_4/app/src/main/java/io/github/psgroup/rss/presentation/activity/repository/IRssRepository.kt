package io.github.psgroup.rss.presentation.activity.repository

import io.github.psgroup.rss.presentation.activity.domain.Feed

interface IRssRepository {
    fun get(): GetResult
    fun fetchRss(url: String): FetchResult
    fun clear()

    sealed class GetResult {
        object Empty : GetResult()
        class Success(val feed: Feed) : GetResult()
    }

    sealed class FetchResult {
        class Success(val feed: Feed) : FetchResult()
        class Error(val error: RepositoryError) : FetchResult()
    }
}
