package com.capstone.kulinerkita.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("kuliner_kita_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "user_token"
        private const val KEY_ONBOARDING_SHOWN = "onboarding_shown"
    }

    fun saveToken(token: String) {
        preferences.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return preferences.getString(KEY_TOKEN, null)
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun clearSession() {
        preferences.edit().remove(KEY_TOKEN).apply()
    }

    fun setOnboardingShown(isShown: Boolean) {
        preferences.edit().putBoolean(KEY_ONBOARDING_SHOWN, isShown).apply()
    }

    fun isOnboardingShown(): Boolean {
        return preferences.getBoolean(KEY_ONBOARDING_SHOWN, false)
    }
}
