package com.github.tokou.common.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil

fun Instant.format(): String {
    val period = periodUntil(Clock.System.now(), TimeZone.currentSystemDefault())
    fun plural(n: Int) = if (n == 1) "" else "s"
    return when {
        period.years > 0 -> period.years.let { "$it year${plural(it)}" }
        period.months > 0 -> period.months.let { "$it month${plural(it)}" }
        period.days > 0 -> period.days.let { "$it day${plural(it)}" }
        period.hours > 0 -> period.hours.let { "$it hour${plural(it)}" }
        else -> period.minutes.let { "$it minute${plural(it)}" }
    }
}
