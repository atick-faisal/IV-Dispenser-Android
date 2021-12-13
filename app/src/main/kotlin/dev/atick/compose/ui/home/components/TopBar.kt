package dev.atick.compose.ui.home.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.compose.ui.common.components.TitleText

@Composable
fun TopBar(
    modifier: Modifier = Modifier
) {
    return Card(
        modifier = modifier
            .then(Modifier.fillMaxWidth()),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp
        ),
        elevation = if (isSystemInDarkTheme()) 0.dp else 4.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            TitleText(title = "I/V Dispensers")
        }
    }
}