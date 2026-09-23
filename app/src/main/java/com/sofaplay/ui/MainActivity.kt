package com.sofaplay.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val label = TextView(this).apply {
            text = getString(com.sofaplay.R.string.foundation_ready)
            textSize = 18f
            setTextColor(android.graphics.Color.WHITE)
            setPadding(64, 64, 64, 64)
        }
        setContentView(label)
    }
}
