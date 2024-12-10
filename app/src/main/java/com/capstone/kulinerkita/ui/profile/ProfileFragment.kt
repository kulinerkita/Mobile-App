package com.capstone.kulinerkita.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.ui.onboarding.ActivityOnboardingLast
import com.capstone.kulinerkita.utils.SessionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout untuk Fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Inisialisasi SessionManager
        val sessionManager = SessionManager(requireContext())

        // Inisialisasi komponen UI
        val backButton = view.findViewById<ImageView>(R.id.backButtonProfile)
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val settingsMenu = view.findViewById<LinearLayout>(R.id.settingsMenu)
        settingsMenu.setOnClickListener {
            Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show()
            // Navigasi ke SettingsFragment (Opsional)
        }

        val helpMenu = view.findViewById<LinearLayout>(R.id.helpMenu)
        helpMenu.setOnClickListener {
            Toast.makeText(requireContext(), "Help clicked", Toast.LENGTH_SHORT).show()
            // Navigasi ke HelpFragment (Opsional)
        }

        // Logout Menu
        val logoutMenu = view.findViewById<LinearLayout>(R.id.logoutMenu)
        logoutMenu.setOnClickListener {
            sessionManager.clearSession()
            mAuth.signOut()

            mGoogleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), ActivityOnboardingLast::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Logout failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Ambil Token dari SessionManager
        val token = sessionManager.getToken()
        Log.d("ProfileFragment", "Token yang ditemukan: $token")

        // Inisialisasi komponen teks untuk nama dan email
        val userName = view.findViewById<TextView>(R.id.userName)
        val userEmail = view.findViewById<TextView>(R.id.userEmail)

        if (token != null) {
            val currentUser = mAuth.currentUser
            Log.d("ProfileFragment", "FirebaseAuth currentUser: $currentUser")
            if (currentUser != null) {
                val name = currentUser.displayName
                val email = currentUser.email

                Log.d("ProfileFragment", "Firebase Auth - Nama: $name, Email: $email")

                if (name != null && email != null) {
                    Log.d("ProfileFragment", "Menyimpan data pengguna ke SharedPreferences: Nama: $name, Email: $email")
                    sessionManager.saveUser(name, email)
                    userName.text = name
                    userEmail.text = email
                } else {
                    Log.d("ProfileFragment", "Nama atau email dari FirebaseAuth null, tidak dapat menyimpan ke SharedPreferences")
                    userName.text = "Nama tidak tersedia"
                    userEmail.text = "Email tidak tersedia"
                }
            } else {
                Log.d("ProfileFragment", "Pengguna FirebaseAuth null. Ambil dari SharedPreferences.")
                val savedName = sessionManager.getUserName()
                val savedEmail = sessionManager.getUserEmail()

                Log.d("ProfileFragment", "SharedPreferences - Nama: $savedName, Email: $savedEmail")

                if (savedName != null && savedEmail != null) {
                    userName.text = savedName
                    userEmail.text = savedEmail
                } else {
                    userName.text = "Nama tidak ditemukan"
                    userEmail.text = "Email tidak ditemukan"
                }
            }
        } else {
            Log.d("ProfileFragment", "Token tidak ditemukan. User belum login.")
            userName.text = "User belum login"
            userEmail.text = "User belum login"
        }

        return view
    }
}