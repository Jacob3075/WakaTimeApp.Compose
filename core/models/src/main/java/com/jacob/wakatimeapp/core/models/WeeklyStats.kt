package com.jacob.wakatimeapp.core.models

import kotlinx.collections.immutable.ImmutableList

data class WeeklyStats(
    val totalTime: Time,
    val dailyStats: ImmutableList<DailyStats>,
    val range: Range,
    val todaysStats: DailyStats = dailyStats.last(),
)
