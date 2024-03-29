package dev.atick.bluetooth.utils

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.orhanobut.logger.Logger
import dev.atick.core.utils.extensions.hasPermission
import dev.atick.core.utils.extensions.permissionLauncher
import dev.atick.core.utils.extensions.resultLauncher
import dev.atick.core.utils.extensions.showAlertDialog
import javax.inject.Inject

class BtUtilsImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?
) : BtUtils {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var btLauncher: ActivityResultLauncher<Intent>

    override fun initialize(
        activity: ComponentActivity,
        onSuccess: () -> Unit
    ) {
        permissionLauncher = activity.permissionLauncher(
            onSuccess = {
                Logger.i("All Permissions Granted")
                enableBluetooth()
            },
            onFailure = { activity.finishAffinity() }
        )

        btLauncher = activity.resultLauncher(
            onSuccess = {
                Logger.i("Bluetooth is Enabled")
                onSuccess.invoke()
            },
            onFailure = { activity.finishAffinity() }
        )
    }

    override fun isAllPermissionsProvided(activity: ComponentActivity): Boolean {
        return isBluetoothPermissionGranted(activity) &&
                bluetoothAdapter?.isEnabled ?: false
    }

    override fun isBluetoothAvailable(activity: ComponentActivity): Boolean {
        return (isBluetoothPermissionGranted(activity) &&
                bluetoothAdapter?.isEnabled ?: false)
    }

    override fun setupBluetooth(activity: ComponentActivity) {
        if (bluetoothAdapter == null) activity.finishAffinity()
        showPermissionRationale(activity)
    }

    private fun enableBluetooth() {
        val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        btLauncher.launch(enableIntent)
    }

    private fun askForPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.addAll(
                listOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        permissionLauncher.launch(permissions.toTypedArray())
    }

    private fun showPermissionRationale(activity: ComponentActivity) {
        if (!isAllPermissionsProvided(activity)) {
            activity.showAlertDialog(
                title = "Permission Required",
                message = "This app requires Bluetooth connection " +
                        "to work properly. Please provide Bluetooth permission. " +
                        "Scanning for BLE devices also requires Location Access " +
                        "Permission. However, location information will NOT be" +
                        "used for tracking.",
                onApprove = {
                    Logger.i("Permission Rationale Approved")
                    askForPermissions()
                },
                onCancel = { activity.finishAffinity() }
            )
        }
    }

    private fun isBluetoothPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.hasPermission(Manifest.permission.BLUETOOTH_SCAN) &&
                    context.hasPermission(Manifest.permission.BLUETOOTH_CONNECT)
        } else true
    }
}