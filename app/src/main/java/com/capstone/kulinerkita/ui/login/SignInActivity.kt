@file:Suppress("DEPRECATION")

package com.capstone.kulinerkita.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.capstone.kulinerkita.MainActivity
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.KulinerKitaDatabase
import com.capstone.kulinerkita.databinding.ActivitySignInBinding
import com.capstone.kulinerkita.ui.onboarding.ActivityOnboardingLast
import com.capstone.kulinerkita.ui.register.CreateAccountActivity
import com.capstone.kulinerkita.utils.SessionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var database: KulinerKitaDatabase
    private lateinit var sessionManager: SessionManager
    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val gson = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gson)

        sessionManager = SessionManager(this)

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
                            Toast.makeText(
                                this@SignInActivity,
                                "Login Berhasil",
                                Toast.LENGTH_SHORT
                            ).show()
                            showNotificationForMaps()
                        } else {
                            Toast.makeText(
                                this@SignInActivity,
                                "Email atau password salah!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding?.googleButton?.setOnClickListener{ signInWithGoogle() }

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

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account!=null){
                updateUI(account)
        }
    } else {
        showToast(this, "Berhasil login")
    }
}

    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
        if (it.isSuccessful){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
            else {
                showToast(this,"Tidak bisa login. Coba beberapa saat lagi")
        }
        }
    }

    private fun showNotificationForMaps() {
        val channelId = "kuliner_kita_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "KulinerKita Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val yesIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("response", "yes")
        }
        val noIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("response", "no")
        }

        val yesPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, yesIntent, PendingIntent.FLAG_IMMUTABLE)
        val noPendingIntent: PendingIntent = PendingIntent.getActivity(this, 1, noIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Enable Maps")
            .setContentText("Do you agree to turn on Maps?")
            .setSmallIcon(R.drawable.notification_off)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(0, "Yes", yesPendingIntent)
            .addAction(0, "No", noPendingIntent)
            .build()

        notificationManager.notify(1001, notification)
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

