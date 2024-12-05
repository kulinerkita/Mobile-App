package com.capstone.kulinerkita.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout untuk Fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

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

        val logoutMenu = view.findViewById<LinearLayout>(R.id.logoutMenu)
        logoutMenu.setOnClickListener {
            // Hapus session
            sessionManager.clearSession()

            // Tampilkan pesan logout
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Navigasi ke ActivityOnboardingLast
            val intent = Intent(requireContext(), ActivityOnboardingLast::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val userName = view.findViewById<TextView>(R.id.userName)
        val userEmail = view.findViewById<TextView>(R.id.userEmail)

        // Set nama dan email pengguna
        userName.text = "Jhon Doe"
        userEmail.text = "example@gmail.com"

        return view
    }
}
