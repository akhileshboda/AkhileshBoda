package com.example.a1_akhilboda_34396268_fit2081

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.content.edit
import com.example.a1_akhilboda_34396268_fit2081.view.HomeScreen
import com.example.a1_akhilboda_34396268_fit2081.view.MainActivity

class Launcher : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = SessionManager.getPrefs(this)
        val userID = prefs.getInt(SessionManager.KEY_USER_ID, -1)

        val next = if (userID != -1) {
            Intent(this, HomeScreen::class.java)
                .putExtra("userID", userID.toString())
        } else {
            Intent(this, MainActivity::class.java)
        }

        startActivity(next)
        finish()
    }
}
