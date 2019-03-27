package io.github.psgroup.rss.presentation.activity.viewmodel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.psgroup.rss.presentation.activity.presentation.AddButtonViewState
import io.github.psgroup.rss.presentation.activity.presentation.ArticlesViewState
import io.github.psgroup.rss.presentation.activity.repository.IRssRepository

class FeedViewModel(
    private val repository: IRssRepository
) : ViewModel() {

    val addButtonViewState: LiveData<AddButtonViewState>
        get() = mAddButtonViewState
    val articlesViewState: LiveData<ArticlesViewState>
        get() = mArticlesViewState
    val refresherViewState: LiveData<Boolean>
        get() = mRefresherViewState

    private val mAddButtonViewState = MutableLiveData<AddButtonViewState>()
        .apply { value = AddButtonViewState.ShowButton }
    private val mArticlesViewState = MutableLiveData<ArticlesViewState>()
        .apply { value = ArticlesViewState.Hidden }
    private val mRefresherViewState = MutableLiveData<Boolean>()
        .apply { value = false }

    fun onAddRss(url: Editable) {
        mAddButtonViewState.value = AddButtonViewState.Hidden
        mArticlesViewState.value = ArticlesViewState.Empty
    }

    fun onRefresh() {
        mRefresherViewState.value = true
        mArticlesViewState.value = ArticlesViewState.ShowArticles(listOf(0, 1, 2))
        mRefresherViewState.value = false
    }

    fun onDeleteFeed() = Unit

    private fun getArticlesFirstTime() {
        mRefresherViewState.value = true
    }

}
