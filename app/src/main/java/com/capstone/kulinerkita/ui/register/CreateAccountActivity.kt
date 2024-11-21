package com.capstone.kulinerkita.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.ui.login.SignInActivity
import com.capstone.kulinerkita.ui.onboarding.ActivityOnboardingLast

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val passwordToggle = findViewById<ImageView>(R.id.passwordToggle)
        val buttonBack = findViewById<ImageView>(R.id.Iv_back)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val footerText = findViewById<TextView>(R.id.loginTextView)

        var isPasswordVisible = false

        // Toggle password visibility
        passwordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordInput.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordToggle.setImageResource(R.drawable.ic_visibility)
            } else {
                passwordInput.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordToggle.setImageResource(R.drawable.ic_visibility_off)
            }
            passwordInput.setSelection(passwordInput.text.length)
        }

        // Sign-up button action
        signUpButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (name.isEmpty()) {
                nameInput.error = "Nama tidak boleh kosong"
                nameInput.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Email tidak valid"
                emailInput.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                passwordInput.error = "Password harus minimal 6 karakter"
                passwordInput.requestFocus()
                return@setOnClickListener
            }

            Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ActivityOnboardingLast::class.java)
            startActivity(intent)
            finish()
        }

        // Footer text action
        footerText.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        // back to onboarding last
        buttonBack.setOnClickListener {
            val intent = Intent(this, ActivityOnboardingLast::class.java)
            startActivity(intent)
            finish()
        }
    }
}
