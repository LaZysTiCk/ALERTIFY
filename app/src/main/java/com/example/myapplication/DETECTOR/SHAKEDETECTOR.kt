package com.example.myapplication.DETECTOR



import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class ShakeDetector : SensorEventListener {

    // Sensitivity variables
    private val SHAKE_THRESHOLD = 15f // Adjust this value
    private val SHAKE_SLOP_TIME_MS = 500 // Minimum time between shakes
    private val SHAKE_COUNT_RESET_TIME_MS = 3000 // Time to reset counter

    private var mShakeTimestamp: Long = 0
    private var mShakeCount: Int = 0
    private var mListener: OnShakeListener? = null

    interface OnShakeListener {
        fun onShake(count: Int)
    }

    fun setOnShakeListener(listener: OnShakeListener) {
        mListener = listener
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Not used
    }

    override fun onSensorChanged(event: SensorEvent) {
        mListener?.let { listener ->
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            // Calculate gForce (will be ~1 when not moving)
            val gForce = Math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

            if (gForce > SHAKE_THRESHOLD) {
                val now = System.currentTimeMillis()

                // Ignore shakes too close together
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return
                }

                // Reset counter if last shake was >3 seconds ago
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0
                }

                mShakeTimestamp = now
                mShakeCount++

                listener.onShake(mShakeCount)
            }
        }
    }
}
