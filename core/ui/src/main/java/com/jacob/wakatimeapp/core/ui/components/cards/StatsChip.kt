package com.jacob.wakatimeapp.core.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
fun StatsChip(
    gradient: Gradient,
    @DrawableRes iconId: Int,
    roundedCornerPercent: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    chipContent: @Composable ColumnScope.() -> Unit,
) {
    val gradientBrush = Brush.horizontalGradient(gradient.colorList)
    val shape = RoundedCornerShape(roundedCornerPercent)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick)
            .background(gradientBrush, shape),
    ) {
        Image(
            painter = painterResource(iconId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(gradient.onEndColor),
            modifier = Modifier
                .padding(
                    end = MaterialTheme.spacing.small,
                    bottom = MaterialTheme.spacing.extraSmall,
                )
                .size(size = 50.dp)
                .align(Alignment.BottomEnd),
        )
        Column(
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.small,
                ),
            content = chipContent,
        )
    }
}

@WtaPreviews
@Composable
private fun StatsChipPreview() = WakaTimeAppTheme {
    Surface {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            StatsChip(
                gradient = MaterialTheme.gradients.purpink,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 25,
                modifier = Modifier.weight(1f),
            ) {}
            StatsChip(
                gradient = MaterialTheme.gradients.amin,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 25,
                modifier = Modifier.weight(1f),
            ) {}
        }
    }
}
