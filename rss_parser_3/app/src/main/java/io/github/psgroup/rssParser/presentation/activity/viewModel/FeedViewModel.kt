package io.github.psgroup.rssParser.presentation.activity.viewModel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedViewModel : ViewModel() {

    val isFeedExists: LiveData<Boolean>
        get() = mIsFeedExists
    val isRefreshing: LiveData<Boolean>
        get() = mIsRefreshing
    val articles: LiveData<List<Any>>
        get() = mArticles

    private val mIsFeedExists = MutableLiveData<Boolean>()
    private val mArticles = MutableLiveData<List<Any>>()
    private val mIsRefreshing = MutableLiveData<Boolean>()

    init {
        mIsFeedExists.value = false
        mArticles.value = listOf()
        mIsRefreshing.value = false
    }

    fun onAddRss(url: Editable) {
        mIsFeedExists.value = true
    }

    fun onRefresh() {
        mIsRefreshing.value = true
        mArticles.value = listOf(0, 1, 2)
        mIsRefreshing.value = false
    }

    fun onDeleteFeed() = Unit

}
