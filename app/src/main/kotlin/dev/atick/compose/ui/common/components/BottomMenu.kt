package dev.atick.compose.ui.common.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomMenu(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    return Card(
        modifier = modifier.then(Modifier.fillMaxWidth()),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        ),
        elevation = if (isSystemInDarkTheme()) 0.dp else 8.dp
    ) {
        content.invoke()
    }
}