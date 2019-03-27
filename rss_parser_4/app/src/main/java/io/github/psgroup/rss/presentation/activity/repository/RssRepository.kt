package io.github.psgroup.rss.presentation.activity.repository

import io.github.psgroup.rss.presentation.activity.domain.Article
import io.github.psgroup.rss.presentation.activity.domain.Feed
import io.github.psgroup.rss.presentation.activity.networking.IRssApi
import io.github.psgroup.rss.presentation.activity.parser.IRssParser
import io.github.psgroup.rss.presentation.activity.storage.ArticleEntity
import io.github.psgroup.rss.presentation.activity.storage.FeedEntity
import io.github.psgroup.rss.presentation.activity.storage.IRssStorage
import java.lang.IllegalArgumentException

class RssRepository(
    private val api: IRssApi,
    private val parser: IRssParser,
    private val storage: IRssStorage
) : IRssRepository {

    override fun get(): IRssRepository.GetResult {
        val feed = storage.get() ?: return IRssRepository.GetResult.Empty

        return IRssRepository.GetResult.Success(
            feed = feed.asDomainModel()
        )
    }

    override fun fetchRss(url: String): IRssRepository.FetchResult {
        val apiResult = api.get(url)

        return when (apiResult) {
            is IRssApi.GetResult.Success -> apiResult.asRepositoryResult()
            else -> apiResult.asRepositoryResult()
        }
    }

    override fun clear() = storage.clear()

    private fun IRssApi.GetResult.asRepositoryResult(): IRssRepository.FetchResult = when (this) {
        is IRssApi.GetResult.Success ->
            throw IllegalArgumentException()
        IRssApi.GetResult.ConnectionError ->
            IRssRepository.FetchResult.Error(RepositoryError.Connection)
        IRssApi.GetResult.PermissionsError ->
            IRssRepository.FetchResult.Error(RepositoryError.NetworkPermissions)
        IRssApi.GetResult.NotFoundError ->
            IRssRepository.FetchResult.Error(RepositoryError.RssNotFound)
        is IRssApi.GetResult.UndefinedError ->
            IRssRepository.FetchResult.Error(RepositoryError.Undefined)
    }

    private fun IRssApi.GetResult.Success.asRepositoryResult(): IRssRepository.FetchResult {
        val parserResult = parser.parse(bytes)

        return when (parserResult) {
            is IRssParser.ParserResult.Success -> {
                val entityToSave = parserResult.asFeedEntity()
                val savedEntity = storage.save(entityToSave)
                IRssRepository.FetchResult.Success(
                    feed = savedEntity.asDomainModel()
                )
            }
            IRssParser.ParserResult.Error -> IRssRepository.FetchResult.Error(
                error = RepositoryError.RssFormat
            )
        }
    }

    private fun IRssParser.ParserResult.Success.asFeedEntity(): FeedEntity {
        return FeedEntity()
    }

    private fun FeedEntity.asDomainModel(): Feed {
        return Feed(
            title = title,
            description = description,
            articles = articles.map { it.asDomainModel() }
        )
    }

    private fun ArticleEntity.asDomainModel(): Article {
        return Article(
            title = title,
            description = description,
            dateInMillis = dateInMillis,
            imageUrl = imageUrl
        )
    }

}