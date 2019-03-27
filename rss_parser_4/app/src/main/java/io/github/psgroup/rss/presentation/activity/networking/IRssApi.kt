package io.github.psgroup.rss.presentation.activity.networking

interface IRssApi {
    fun get(url: String): GetResult

    sealed class GetResult {
        class Success(val bytes: ByteArray) : GetResult()
        object ConnectionError : GetResult()
        object PermissionsError : GetResult()
        object NotFoundError : GetResult()
        class UndefinedError(val code: Int) : GetResult()
    }
}