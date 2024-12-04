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
import com.capstone.kulinerkita.databinding.FragmentProfileBinding
import com.capstone.kulinerkita.ui.login.SignInActivity
import com.capstone.kulinerkita.utils.SessionManager

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout untuk Fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        sessionManager = SessionManager(requireContext())

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

        binding.logoutMenu.setOnClickListener {
            sessionManager.clearSession()
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        val userName = view.findViewById<TextView>(R.id.userName)
        val userEmail = view.findViewById<TextView>(R.id.userEmail)

        // Set nama dan email pengguna
        userName.text = "Jhon Doe"
        userEmail.text = "example@gmail.com"

        return view
    }
}
