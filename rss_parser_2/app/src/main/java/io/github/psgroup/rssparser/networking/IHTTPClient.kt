package io.github.psgroup.rssparser.networking

interface IHTTPClient {
    fun request(url: String): HTTPRequestResult
}