package dev.atick.compose.ui.home.components

import ai.atick.material.MaterialColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Water
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.compose.utils.getFormattedDateTime
import dev.atick.compose.utils.round
import dev.atick.data.models.Dispenser

@Composable
fun DispenserItem(
    modifier: Modifier = Modifier,
    dispenser: Dispenser,
    onClick: (String) -> Unit
) {
    return Card(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .clickable { onClick.invoke(dispenser.deviceId) },
        ),
        elevation = if (isSystemInDarkTheme()) 0.dp else 2.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if (dispenser.alertMessage != null) {
            MaterialTheme.colors.error
        } else {
            MaterialTheme.colors.surface
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Last Updated: ${getFormattedDateTime(dispenser.timestamp)}",
                color = MaterialTheme.colors.onSurface,
                fontSize = 12.sp
            )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    Modifier.fillMaxSize()
                ) {
                    Column(
                        Modifier.weight(0.5F)
                    ) {


                        Text(
                            text = "Room No.",
                            color = MaterialTheme.colors.onSurface
                        )

                        Text(
                            text = dispenser.room.toString(),
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Column(
                        Modifier
                            .weight(0.5F)
                    ) {
                        SensorItem(
                            icon = Icons.Filled.InvertColors,
                            "${dispenser.dripRate?.round() ?: 0F} /min"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SensorItem(
                            icon = Icons.Filled.Air,
                            value = "${dispenser.flowRate.round()} mL/h"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                    SensorItem(
                        icon = Icons.Filled.Water,
                        value = "${dispenser.urineOut.round()} mL"
                    )
                }
            }
        }
    }
}