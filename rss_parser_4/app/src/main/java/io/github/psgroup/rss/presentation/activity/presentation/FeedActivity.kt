package io.github.psgroup.rss.presentation.activity.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.psgroup.rss.presentation.activity.di.ViewModelInjector
import io.github.psgroup.rss.presentation.activity.extension.exhaustive
import io.github.psgroup.rss.presentation.activity.viewmodel.FeedViewModel
import io.github.psgroup.rssParser.R
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.dialog_input_field.view.*

class FeedActivity : AppCompatActivity() {

    private var mUrlDialog: AlertDialog? = null
    private val mArticlesAdapter = ArticlesAdapter()
    private lateinit var mViewModel: FeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        initArticlesList()
        initAddRssButton()
        initRefresher()
        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mUrlDialog?.dismiss()
    }

    private fun initArticlesList() {
        newsList.layoutManager = LinearLayoutManager(this)
        newsList.adapter = mArticlesAdapter
    }

    private fun initAddRssButton() {
        addRssButton.setOnClickListener {
            openUrlDialog()
        }
    }

    private fun initRefresher() {
        listRefresher.setOnRefreshListener {
            mViewModel.onRefresh()
        }
    }

    private fun initViewModel() {
        mViewModel = ViewModelInjector.getFeedViewModel(this)

        mViewModel.addButtonViewState.observe(this, Observer { updateAddButtonView(it) })
        mViewModel.articlesViewState.observe(this, Observer { updateArticlesView(it) })
        mViewModel.refresherViewState.observe(this, Observer { updateRefresherView(it) })
    }

    private fun openUrlDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_input_field, null, false)
        mUrlDialog = AlertDialog.Builder(this)
                .setTitle("Введите ссылку на RSS")
                .setPositiveButton("Добавить") { dialog, buttonId ->
                    mViewModel.onAddRss(view.rssUrl.text)
                }
                .setNegativeButton("Отмена") { dialog, buttonId -> }
                .setView(R.layout.dialog_input_field)
                .show()
    }

    private fun updateAddButtonView(viewState: AddButtonViewState) {
        when (viewState) {
            AddButtonViewState.Hidden -> {
                addRssButtonBackground.visibility = View.GONE
            }
            AddButtonViewState.Locked -> {
                addRssButtonBackground.visibility = View.VISIBLE
                addRssButton.isEnabled = false
            }
            AddButtonViewState.ShowButton -> {
                addRssButtonBackground.visibility = View.VISIBLE
                addRssButton.isEnabled = true
            }
        }.exhaustive
    }

    private fun updateArticlesView(viewState: ArticlesViewState) {
        when (viewState) {
            ArticlesViewState.Hidden -> {
                newsList.visibility = View.GONE
                emptyListText.visibility = View.GONE
            }
            ArticlesViewState.Empty -> {
                newsList.visibility = View.GONE
                emptyListText.visibility = View.VISIBLE
            }
            is ArticlesViewState.ShowArticles -> {
                newsList.visibility = View.VISIBLE
                emptyListText.visibility = View.GONE
            }
        }.exhaustive
    }

    private fun updateRefresherView(viewState: Boolean) {
        listRefresher.isRefreshing = viewState
    }

}
