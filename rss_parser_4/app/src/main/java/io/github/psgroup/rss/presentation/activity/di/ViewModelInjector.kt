package io.github.psgroup.rss.presentation.activity.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.psgroup.rss.presentation.activity.networking.RssApi
import io.github.psgroup.rss.presentation.activity.parser.RssParser
import io.github.psgroup.rss.presentation.activity.presentation.FeedActivity
import io.github.psgroup.rss.presentation.activity.repository.IRssRepository
import io.github.psgroup.rss.presentation.activity.repository.RssRepository
import io.github.psgroup.rss.presentation.activity.storage.RssStorage
import io.github.psgroup.rss.presentation.activity.viewmodel.FeedViewModel
import java.lang.IllegalArgumentException

object ViewModelInjector {

    fun getFeedViewModel(activity: FeedActivity): FeedViewModel {
        val provider = ViewModelProvider(activity, Factory)
        return provider[FeedViewModel::class.java]
    }

    private fun newRepository(): IRssRepository = RssRepository(
        api = RssApi(),
        parser = RssParser(),
        storage = RssStorage()
    )

    private object Factory: ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass == FeedViewModel::class.java) {
                return FeedViewModel(newRepository()) as T
            }

            throw IllegalArgumentException(
                "ViewModel ${modelClass.canonicalName} not supported")
        }

    }

}