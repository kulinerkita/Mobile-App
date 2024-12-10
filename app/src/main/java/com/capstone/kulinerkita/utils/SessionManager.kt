package com.capstone.kulinerkita.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SessionManager(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("kuliner_kita_prefs", Context.MODE_PRIVATE)

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "user_token"
        private const val KEY_ONBOARDING_SHOWN = "onboarding_shown"
    }

    fun saveToken(token: String) {
        Log.d("SessionManager", "Menyimpan token: $token ke SharedPreferences")
        preferences.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        val token = preferences.getString(KEY_TOKEN, null)
        Log.d("SessionManager", "Mengambil token: $token")
        return token
    }

    fun isLoggedIn(): Boolean {
        val isLoggedIn = getToken() != null
        Log.d("SessionManager", "Apakah pengguna sudah login? $isLoggedIn")
        return isLoggedIn
    }

    fun clearSession() {
        Log.d("SessionManager", "Menghapus session, menghapus token, nama, dan email")
        preferences.edit().remove(KEY_TOKEN).apply()
        preferences.edit().remove("user_name").apply()
        preferences.edit().remove("user_email").apply()
    }

    fun setOnboardingShown(isShown: Boolean) {
        Log.d("SessionManager", "Menandai onboarding sudah ditampilkan: $isShown")
        preferences.edit().putBoolean(KEY_ONBOARDING_SHOWN, isShown).apply()
    }

    fun isOnboardingShown(): Boolean {
        val isShown = preferences.getBoolean(KEY_ONBOARDING_SHOWN, false)
        Log.d("SessionManager", "Apakah onboarding sudah ditampilkan? $isShown")
        return isShown
    }

    fun saveUser(name: String?, email: String?) {
        Log.d("SessionManager", "Menyimpan nama: $name, email: $email ke SharedPreferences")
        val editor = sharedPreferences.edit()
        editor.putString("user_name", name)
        editor.putString("user_email", email)
        editor.apply()
        Log.d("SessionManager", "Nama dan email berhasil disimpan.")
    }

    fun getUserName(): String? {
        val name = sharedPreferences.getString("user_name", null)
        Log.d("SessionManager", "Mengambil nama: $name")
        return name
    }

    fun getUserEmail(): String? {
        val email = sharedPreferences.getString("user_email", null)
        Log.d("SessionManager", "Mengambil email: $email")
        return email
    }
}
