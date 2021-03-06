package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.R
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.theme.Colors
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.Project
import java.time.LocalDate

@Composable
fun RecentProjects(parameters: RecentProjectsParameters) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Recent Projects", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "See All", color = Colors.AccentText, fontSize = 14.sp)
        }
        RecentProjectList(
            modifier = Modifier.padding(horizontal = 12.dp),
            projects = parameters.dailyStats?.projectsWorkedOn ?: emptyList()
        )
    }
}

@Composable
private fun RecentProjectList(modifier: Modifier = Modifier, projects: List<com.jacob.wakatimeapp.core.models.Project>) {
    Column(
        modifier = modifier
    ) {
        projects.take(3).map { ProjectCardItem(it) }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun ProjectCardItem(project: com.jacob.wakatimeapp.core.models.Project) {
    val cardShape = RoundedCornerShape(25)
    Box(
        modifier = Modifier
            .shadow(elevation = 8.dp, shape = cardShape)
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 2.dp)
            .background(color = Colors.CardBGPrimary, shape = cardShape)
            .clickable { }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Column(
                Modifier
                    .weight(1f, fill = true)
            ) {
                Text(text = project.name, fontSize = 22.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${project.time.hours} Hours, ${project.time.minutes} Mins",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            }
            Image(painter = painterResource(id = R.drawable.ic_arrow), contentDescription = "")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun RecentProjectPreview() = WakaTimeAppTheme(darkTheme = true) {
    Surface {
        RecentProjects(
            parameters = RecentProjectsParameters(
                com.jacob.wakatimeapp.core.models.DailyStats(
                    timeSpent = com.jacob.wakatimeapp.core.models.Time(0, 0, 0f),
                    projectsWorkedOn = listOf(
                        com.jacob.wakatimeapp.core.models.Project(com.jacob.wakatimeapp.core.models.Time(
                            10,
                            9,
                            0f), "Project 1", 75.0),
                        com.jacob.wakatimeapp.core.models.Project(com.jacob.wakatimeapp.core.models.Time(
                            100,
                            26,
                            0f), "Project 2", 20.0),
                        com.jacob.wakatimeapp.core.models.Project(com.jacob.wakatimeapp.core.models.Time(
                            5,
                            15,
                            0f), "Project 3", 10.0),
                    ),
                    mostUsedLanguage = "",
                    mostUsedEditor = "",
                    mostUsedOs = "",
                    date = LocalDate.now()
                )
            ),
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ProjectCardItemPreview() = WakaTimeAppTheme {
    Surface {
        ProjectCardItem(com.jacob.wakatimeapp.core.models.Project(com.jacob.wakatimeapp.core.models.Time(
            0,
            0,
            0f), "Project 1", 0.0))
    }
}
