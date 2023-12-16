package dev.atick.compose.ui.registration.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.atick.compose.ui.common.components.CustomTextFiled
import dev.atick.compose.R
import dev.atick.data.models.Register
import kotlinx.serialization.json.Json

@ExperimentalAnimationApi
@Composable
fun RegistrationForm(
    modifier: Modifier = Modifier,
    onSubmitClick: (String) -> Unit
) {
    var roomNumber by remember { mutableStateOf("") }
    var wifiSsid by remember { mutableStateOf("") }
    var wifiPassword by remember { mutableStateOf("") }

    return Column(
        modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    ) {
        CustomTextFiled(
            textFieldValue = roomNumber,
            labelResourceId = R.string.label_room,
            hintResourceId = R.string.hint_room,
            onValueChange = { roomNumber = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextFiled(
            textFieldValue = wifiSsid,
            labelResourceId = R.string.label_wifi,
            hintResourceId = R.string.hint_wifi,
            onValueChange = { wifiSsid = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextFiled(
            textFieldValue = wifiPassword,
            labelResourceId = R.string.label_password,
            hintResourceId = R.string.hint_password,
            isPasswordField = true,
            onValueChange = { wifiPassword = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val data = Json.encodeToString(
                    Register.serializer(),
                    Register(
                        roomNumber,
                        wifiSsid,
                        wifiPassword
                    )
                )
                onSubmitClick(data)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = stringResource(R.string.register_device))
        }
    }
}