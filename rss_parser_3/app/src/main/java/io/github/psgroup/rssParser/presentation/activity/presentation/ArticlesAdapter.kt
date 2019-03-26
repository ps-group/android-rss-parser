package io.github.psgroup.rssParser.presentation.activity.presentation

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.psgroup.rssParser.R

class ArticlesAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ArticleViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
            .let { view -> ArticleViewHolder(view) }

    override fun onBindViewHolder(viewHolder: ArticleViewHolder, position: Int) = Unit

    override fun getItemCount(): Int = 100

}
