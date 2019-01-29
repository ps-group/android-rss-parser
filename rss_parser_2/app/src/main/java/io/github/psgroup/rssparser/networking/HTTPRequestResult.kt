package io.github.psgroup.rssparser.networking

sealed class HTTPRequestResult
class Success(val bytes: ByteArray) : HTTPRequestResult()
object InvalidUrl : HTTPRequestResult()
object BadConnection : HTTPRequestResult()
