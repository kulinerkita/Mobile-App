@file:Suppress("DEPRECATION")

package com.capstone.kulinerkita.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
                            // Simpan token atau data session jika perlu
                            sessionManager.saveToken("fake_token_${user.id}")
                            Toast.makeText(this@SignInActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@SignInActivity, MainActivity::class.java).apply {
                                // Jika perlu, kirimkan data seperti nama pengguna
                                putExtra("username", user.name)
                            }

                            // Mulai MainActivity
                            startActivity(intent)
                            finish()
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

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = auth.currentUser
                val username = user?.displayName ?: "User"

                // Mengarahkan ke MainActivity dengan membawa username
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("username", username)
                }
                startActivity(intent)
                finish()
            } else {
                showToast(this, "Tidak bisa login. Coba beberapa saat lagi")
            }
        }
    }


    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

