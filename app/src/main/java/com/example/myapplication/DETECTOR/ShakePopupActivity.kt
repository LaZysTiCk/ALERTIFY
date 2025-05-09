package com.example.myapplication.DETECTOR



import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ShakePopupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show the layout (popup)
        setContentView(R.layout.activity_shake_popup)

        // Find buttons from XML
        val yesBtn = findViewById<Button>(+id/btn_yes)
        val noBtn = findViewById<Button>(+id/btn_no)

        // When YES is clicked
        yesBtn.setOnClickListener {
            // Show message
            Toast.makeText(this, "🚑 Ambulance requested!", Toast.LENGTH_SHORT).show()
            // Close popup
            finish()
        }

        // When NO is clicked
        noBtn.setOnClickListener {
            // Just close popup
            finish()
        }
    }
}
