package com.jacob.wakatimeapp.details.ui

import com.jacob.wakatimeapp.core.models.Time
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.datetime.LocalDate

sealed class DetailsPageViewState {
    data object Loading : DetailsPageViewState()

    data class Loaded(
        val projectName: String,
        val statsForProject: ImmutableMap<LocalDate, Time>,
    ) : DetailsPageViewState() {
        fun filterDayStatsToNonZeroDays(): List<Time> {
            return statsForProject.values.filter { it != Time.ZERO }
        }
    }

    data class Error(val error: com.jacob.wakatimeapp.core.models.Error) : DetailsPageViewState()
}
