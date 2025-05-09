package com.example.myapplication

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var shakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handle window insets (for full screen apps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(+id/btn_yes)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize sensor manager and accelerometer
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Initialize shake detector
        shakeDetector = ShakeDetector()
        shakeDetector.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            override fun onShake(count: Int) {
                if (count >= 3) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Emergency Shake Detected!", Toast.LENGTH_SHORT).show()
                        // TODO: Add your emergency response here
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also {
            sensorManager.registerListener(shakeDetector, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeDetector)
    }
}
