package io.github.psgroup.rssParser.presentation.activity.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.psgroup.rssParser.R
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    private var mUrlDialog: AlertDialog? = null
    private val mArticlesAdapter = ArticlesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        addRssButton.setOnClickListener {
            openUrlDialog()
        }

        initArticlesList()
    }

    override fun onDestroy() {
        super.onDestroy()
        mUrlDialog?.dismiss()
    }

    private fun initArticlesList() {
        newsList.layoutManager = LinearLayoutManager(this)
        newsList.adapter = mArticlesAdapter
    }

    private fun openUrlDialog() {
        mUrlDialog = AlertDialog.Builder(this)
                .setTitle("Введите ссылку на RSS")
                .setPositiveButton("Добавить") { dialog, buttonId ->
                    startActivity(Intent(this, ArticleActivity::class.java))
                }
                .setNegativeButton("Отмена") { dialog, buttonId -> }
                .setView(R.layout.dialog_input_field)
                .show()
    }

}
