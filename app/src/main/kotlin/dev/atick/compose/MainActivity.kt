package dev.atick.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.bluetooth.utils.BtUtils
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var btUtils: BtUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_JetpackComposeStarter)
        setContentView(R.layout.activity_main)

        btUtils.initialize(this) {
            Logger.i("BLUETOOTH IS READY")
        }
    }
}