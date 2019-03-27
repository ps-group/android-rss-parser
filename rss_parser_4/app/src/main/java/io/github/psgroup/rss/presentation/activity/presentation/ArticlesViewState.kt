package io.github.psgroup.rss.presentation.activity.presentation

sealed class ArticlesViewState {
    object Hidden : ArticlesViewState()
    object Empty : ArticlesViewState()
    class ShowArticles(val articles: List<Any>) : ArticlesViewState()
}