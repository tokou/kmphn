package com.github.tokou.common.detail

import com.github.tokou.common.detail.NewsDetail.Text.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ParseCommentTests {

    private val code = """    # 1. Apache conf
        |    Header set Referrer-Policy "no-referrer-when-downgrade"
        |    <!-- 2. meta HTML head with same effect [2] -->
        |    <meta name="referrer" content="no-referrer-when-downgrade">
        |    <!-- 3.  HTML anchor attribute -->
        |    <a href="https://…" referrerpolicy="no-referrer-when-downgrade">""".trimMargin()

    private val cases = listOf(
        listOf(Emphasis("Emphasis")) to "<i>Emphasis</i>".parseText(),
        listOf(Link("Text", "Link")) to "<a href=\"Link\" rel=\"nofollow\">Text</a>".parseText(),
        listOf(Code("Hello world")) to "<pre><code>Hello world</code></pre>".parseText(),
        listOf(Code(code)) to "<pre><code>$code</code></pre>".parseText(),
        listOf(Emphasis("Emphasis"), Emphasis("Emphasis2")) to "<i>Emphasis</i><i>Emphasis2</i>".parseText(),
        listOf(Plain("Hello "), Emphasis("Emphasis"), Plain(" "), Emphasis("Emphasis2"), Plain(" gotcha")) to "Hello <i>Emphasis</i> <i>Emphasis2</i> gotcha".parseText(),
        listOf(Plain("Hello "), Emphasis("Emphasis"), Plain(" yeah right "), Link("Text", "Link"), Plain(" No shit "), Code("Hello world"), Plain(" Cool")) to "Hello <i>Emphasis</i> yeah right <a href=\"Link\" rel=\"nofollow\">Text</a> No shit <pre><code>Hello world</code></pre> Cool".parseText()
    )

    @Test
    fun testParse() {
        cases.forEach { (expected, actual) ->
            assertEquals(expected, actual)
        }
    }
}
