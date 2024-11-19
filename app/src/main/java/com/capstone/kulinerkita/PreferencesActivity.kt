package com.capstone.kulinerkita

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val btnHalal = findViewById<MaterialButton>(R.id.btnHalal)
        val btnVegan = findViewById<MaterialButton>(R.id.btnVegan)
        val btnSpicy = findViewById<MaterialButton>(R.id.btnSpicy)
        val btnLocalFood = findViewById<MaterialButton>(R.id.btnLocalFood)

        val buttons = listOf(btnHalal, btnVegan, btnSpicy, btnLocalFood)

        buttons.forEach { button ->
            button.setOnClickListener {
                buttons.forEach { it.isChecked = false }
                button.isChecked = true
            }
        }
    }
}
