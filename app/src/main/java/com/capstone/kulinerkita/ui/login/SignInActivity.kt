package com.capstone.kulinerkita.ui.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.capstone.kulinerkita.MainActivity
import com.capstone.kulinerkita.NotificationReceiver
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.KulinerKitaDatabase
import com.capstone.kulinerkita.databinding.ActivitySignInBinding
import com.capstone.kulinerkita.ui.onboarding.ActivityOnboardingLast
import com.capstone.kulinerkita.ui.register.CreateAccountActivity
import com.capstone.kulinerkita.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var database: KulinerKitaDatabase
    private lateinit var sessionManager: SessionManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            navigateToMainActivity()
        }

        // Meminta izin untuk menampilkan notifikasi jika perangkat menggunakan Android 13 atau lebih tinggi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        val passwordInput = findViewById<EditText>(R.id.passwordInputLogin)
        val passwordToggle = findViewById<ImageView>(R.id.passwordToggleLogin)

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

        database = KulinerKitaDatabase.getInstance(this)

        // Sign In Button
        binding.LoginButton.setOnClickListener {
            val email = binding.emaillogin.text.toString().trim()
            val password = binding.passwordInputLogin.text.toString().trim()

            if (email.isEmpty()) {
                binding.emaillogin.error = "Email wajib diisi!"
                binding.emaillogin.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emaillogin.error = "Format email tidak valid!"
                binding.emaillogin.requestFocus()
            } else if (password.isEmpty()) {
                binding.passwordInputLogin.error = "Password wajib diisi!"
                binding.passwordInputLogin.requestFocus()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = database.userDao().loginUser(email, password)
                    withContext(Dispatchers.Main) {
                        if (user != null) {
                            sessionManager.saveToken("fake_token_${user.id}")
                            Toast.makeText(this@SignInActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                            navigateToMainActivity()
                        } else {
                            Toast.makeText(this@SignInActivity, "Email atau password salah!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // Sign Up navigation
        binding.signupTextView.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

        // Back button action
        binding.IvBackLogin.setOnClickListener {
            startActivity(Intent(this, ActivityOnboardingLast::class.java))
            finish()
        }
    }

    // Menampilkan notifikasi dengan dua tombol (Setuju dan Tidak Setuju)
    private fun showNotification() {
        val channelId = "kuliner_kita_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Membuat channel notifikasi jika Android Oreo ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "KulinerKita Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val yesIntent = Intent(this, NotificationReceiver::class.java).apply {
            putExtra("response", "yes")
        }
        val noIntent = Intent(this, NotificationReceiver::class.java).apply {
            putExtra("response", "no")
        }

        val yesPendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, yesIntent, PendingIntent.FLAG_IMMUTABLE)
        val noPendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 1, noIntent, PendingIntent.FLAG_IMMUTABLE)

        // Membuat notifikasi dengan dua tombol
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Enable Maps")
            .setContentText("Do you agree to turn on Maps?")
            .setSmallIcon(R.drawable.notification_off)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(0, "Yes", yesPendingIntent)
            .addAction(0, "No", noPendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, Anda dapat menampilkan notifikasi
                showNotification()
            } else {
                // Izin ditolak, beri tahu pengguna atau sesuaikan fitur notifikasi
                Toast.makeText(this, "Permission denied to post notifications.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

