package dev.atick.compose.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
        elevation = 2.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                Modifier
                    .weight(0.4F)
            ) {
                Text(
                    text = dispenser.timestamp.toString(),
                    color = Color.LightGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Room No.")

                Text(
                    text = dispenser.room.toString(),
                    color = Color.DarkGray,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Thin
                )
            }

            Column(
                Modifier
                    .weight(0.6F)
            ) {
                SensorItem(
                    icon = Icons.Filled.InvertColors,
                    "${dispenser.dripRate} drips/min"
                )

                Spacer(modifier = Modifier.height(8.dp))

                SensorItem(
                    icon = Icons.Filled.Air,
                    value = "${dispenser.flowRate} mL/h"
                )

                Spacer(modifier = Modifier.height(8.dp))

                SensorItem(
                    icon = Icons.Filled.Water,
                    value = "${dispenser.urineOut} mL"
                )
            }
        }
    }
}