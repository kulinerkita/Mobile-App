package com.capstone.kulinerkita.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.kulinerkita.MainActivity
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.KulinerKitaDatabase
import com.capstone.kulinerkita.ui.home.HomeFragment
import com.capstone.kulinerkita.ui.onboarding.ActivityOnboardingLast
import com.capstone.kulinerkita.ui.register.CreateAccountActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInActivity : AppCompatActivity() {
    private lateinit var database: KulinerKitaDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val emailInput = findViewById<EditText>(R.id.emaillogin)
        val passwordInput = findViewById<EditText>(R.id.passwordInputLogin)
        val passwordToggle = findViewById<ImageView>(R.id.passwordToggleLogin)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)
        val buttonBack = findViewById<ImageView>(R.id.Iv_backLogin)
        val footerText = findViewById<TextView>(R.id.signupTextView)
        val signInButton = findViewById<Button>(R.id.LoginButton)

        database = KulinerKitaDatabase.getInstance(this)

        var isPasswordVisible = false

        // Password toggle functionality
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

        // Forgot password
        forgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show()
        }

        // Sign Up navigation
        footerText.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        // Back to onboarding last
        buttonBack.setOnClickListener {
            val intent = Intent(this, ActivityOnboardingLast::class.java)
            startActivity(intent)
            finish()
        }

        // Sign In Button
        signInButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty()) {
                emailInput.error = "Email wajib diisi!"
                emailInput.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Format email tidak valid!"
                emailInput.requestFocus()
            } else if (password.isEmpty()) {
                passwordInput.error = "Password wajib diisi!"
                passwordInput.requestFocus()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = database.userDao().loginUser(email, password)
                    withContext(Dispatchers.Main) {
                        if (user != null) {
                            Toast.makeText(this@SignInActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                            // Arahkan ke MainActivity setelah login berhasil
                            val intent = Intent(this@SignInActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(this@SignInActivity, "Email atau password salah!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }
}
