package io.github.psgroup.rssParser.presentation.activity.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.psgroup.rssParser.R
import io.github.psgroup.rssParser.presentation.activity.viewModel.FeedViewModel
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
        val provider = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
        mViewModel = provider[FeedViewModel::class.java]

        mViewModel.articles.observe(this, Observer { updateArticles(it) })
        mViewModel.isFeedExists.observe(this, Observer { updateFeed(it) })
        mViewModel.isRefreshing.observe(this, Observer { updateRefresher(it) })
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

    private fun updateFeed(isFeedExists: Boolean) {
        if (isFeedExists) {
            addRssButtonBackground.visibility = View.GONE
        } else {
            addRssButtonBackground.visibility = View.VISIBLE
        }
    }

    private fun updateArticles(articles: List<Any>) {
        val isArticlesExists = articles.isNotEmpty()

        if (isArticlesExists) {
            newsList.visibility = View.VISIBLE
            emptyListText.visibility = View.GONE
        } else {
            newsList.visibility = View.GONE
            emptyListText.visibility = View.VISIBLE
        }
    }

    private fun updateRefresher(isRefreshing: Boolean) {
        listRefresher.isRefreshing = isRefreshing
    }

}
