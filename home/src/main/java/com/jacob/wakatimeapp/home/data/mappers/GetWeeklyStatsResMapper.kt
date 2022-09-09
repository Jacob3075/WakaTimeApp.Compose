package com.jacob.wakatimeapp.home.data.mappers

import com.jacob.wakatimeapp.core.models.*
import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO.Data
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO.Data.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

fun GetLast7DaysStatsResDTO.toModel() = WeeklyStats(
    totalTime = Time.createFrom(cumulativeTotal.digital, cumulativeTotal.decimal),
    dailyStats = getDailyStatsFromDto(data),
    range = StatsRange(
        startDate = parseDate(start),
        endDate = parseDate(end)
    )
)

private fun getDailyStatsFromDto(data: List<Data>) = data.map {
    DailyStats(
        timeSpent = Time.createFrom(it.grandTotal.digital, it.grandTotal.decimal),
        mostUsedEditor = it.editors.maxByOrNull(Editor::percent)?.name ?: "NA",
        mostUsedLanguage = it.languages.maxByOrNull(Language::percent)?.name ?: "NA",
        mostUsedOs = it.operatingSystems.maxByOrNull(OperatingSystem::percent)?.name ?: "NA",
        date = LocalDate.parse(it.range.date),
        projectsWorkedOn = it.projects.filterNot { project -> project.name == "Unknown Project" }
            .map { project ->
                Project(
                    Time(
                        project.hours,
                        project.minutes,
                        project.decimal.toFloat()
                    ),
                    project.name,
                    project.percent
                )
            }
    )
}

private fun parseDate(dateTimeString: String): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    return sdf.parse(dateTimeString)!!
}