package com.capstone.kulinerkita

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class Onboarding5Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_onboarding5, container, false)

        val createAccountButton = rootView.findViewById<Button>(R.id.createAccountButton)

        createAccountButton.setOnClickListener {
            try {
                Log.d("Onboarding5Fragment", "Membuka CreateAccountActivity")

                val intent = Intent(requireContext(), CreateAccountActivity::class.java)
                startActivity(intent)

            } catch (e: Exception) {
                Log.e("Onboarding5Fragment", "Error while starting CreateAccountActivity", e)
            }
        }

        val loginTextView = rootView.findViewById<TextView>(R.id.loginTextView)
        loginTextView.setOnClickListener {
            try {
                Log.d("Onboarding5Fragment", "Membuka SignInActivity")
                val intent = Intent(requireContext(), SignInActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("Onboarding5Fragment", "Error while starting SignInActivity", e)
            }
        }
        return rootView
    }
}
