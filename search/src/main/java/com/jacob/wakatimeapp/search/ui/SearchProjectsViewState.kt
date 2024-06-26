package com.jacob.wakatimeapp.search.ui

import com.jacob.wakatimeapp.core.models.Error as CoreError
import androidx.compose.ui.text.input.TextFieldValue
import com.jacob.wakatimeapp.search.domain.models.ProjectDetails
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

sealed class SearchProjectsViewState {
    data object Loading : SearchProjectsViewState()
    data class Loaded(
        val projects: ImmutableList<ProjectDetails>,
        val searchQuery: TextFieldValue = TextFieldValue(""),
    ) : SearchProjectsViewState() {
        val filteredProjects = projects.filter {
            it.name.contains(
                searchQuery.text.trim(),
                ignoreCase = !searchQuery.text.any(Char::isUpperCase),
            )
        }.toImmutableList()
    }

    data class Error(val error: CoreError) : SearchProjectsViewState()
}
