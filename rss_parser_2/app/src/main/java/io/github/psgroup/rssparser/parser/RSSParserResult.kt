package io.github.psgroup.rssparser.parser

sealed class RSSParserResult
class Success(val channel: Channel, val items: List<Item>) : RSSParserResult()
object InvalidFormat : RSSParserResult()
