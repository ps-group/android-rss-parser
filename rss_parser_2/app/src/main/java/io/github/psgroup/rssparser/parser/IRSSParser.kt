package io.github.psgroup.rssparser.parser

interface IRSSParser {
    fun parse(bytes: ByteArray): RSSParserResult
}