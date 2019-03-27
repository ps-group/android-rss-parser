package io.github.psgroup.rss.presentation.activity.storage

interface IRssStorage {
    fun get(): FeedEntity?
    fun save(entity: FeedEntity): FeedEntity
    fun clear()
}
