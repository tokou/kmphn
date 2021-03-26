package com.github.tokou.common.detail

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil

// https://news.ycombinator.com/formatdoc
fun String.parseText(): List<NewsDetail.Text> {
    val encoding = mapOf(
        "<p>" to "\n\n",
        "</p>" to "",
        "</pre>" to "</pre>",
        "&#x27;" to "'",
        "&gt;" to ">",
        "&lt;" to "<",
        "&quot;" to "'",
        "&amp;" to "&",
        "&#x2F;" to "/",
    )
    val decoded = encoding.entries.fold(this) { t, (k, v) -> t.replace(k, v) }
    val tags = "(<pre><code>((?:.|\n)*?)</code></pre>|<i>(.*?)</i>|<a href=\"(.*?)\" rel=\"nofollow\">(.*?)</a>)".toRegex()
    val matches = tags.findAll(decoded)
    val formatted = matches.map {
        val values = it.groupValues.drop(2)
        it.range to when {
            values[1].isNotBlank() -> NewsDetail.Text.Emphasis(values[1])
            values[0].isNotBlank() -> NewsDetail.Text.Code(values[0])
            else -> NewsDetail.Text.Link(values[3], values[2])
        }
    }
    var current = 0
    val all = formatted.flatMap { (range, element) ->
        val p = NewsDetail.Text.Plain(decoded.subSequence(current, range.first).toString())
        val prev = if (current < range.first) listOf(p) else emptyList()
        current = range.last + 1
        val isLast = formatted.last().second == element
        val n = NewsDetail.Text.Plain(decoded.subSequence(current, decoded.length).toString())
        val next = if (isLast && current < decoded.length) listOf(n) else emptyList()
        prev + listOf(element) + next
    }
    return all.toList().ifEmpty { listOf(NewsDetail.Text.Plain(decoded)) }
}
