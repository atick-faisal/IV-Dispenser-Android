package dev.atick.compose.ui.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SensorItem(
    icon: ImageVector,
    value: String,
    modifier: Modifier = Modifier
) {
    return Row(
        modifier = modifier.then(
            Modifier.fillMaxWidth()
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = MaterialTheme.colors.onSurface,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = value,
            color = MaterialTheme.colors.onSurface,
        )
    }
}