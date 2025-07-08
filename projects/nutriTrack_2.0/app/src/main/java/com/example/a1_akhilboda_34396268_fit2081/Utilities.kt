package com.example.a1_akhilboda_34396268_fit2081

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.core.content.edit
import coil.compose.AsyncImage
import com.example.a1_akhilboda_34396268_fit2081.data.AppDatabase
import com.example.a1_akhilboda_34396268_fit2081.data.UserDao
import com.example.a1_akhilboda_34396268_fit2081.data.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import java.security.MessageDigest
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// simple SHA-256 hasher
fun hashSha256(input: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val bytes = md.digest(input.toByteArray(Charsets.UTF_8))
    return bytes.joinToString("") { "%02x".format(it) }
}

// Using object to enforce singleton use
object SessionManager {
    val PREFS_NAME: String
        get() = "prefs_nutritrack_session"
    val KEY_USER_ID: String
        get() = "userID"

    // Get preferences
    fun getPrefs(context: Context): SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Save session on login
    fun startSession(context: Context, userId: Int) = getPrefs(context).edit { putInt(KEY_USER_ID, userId) }

    // Clear session for logout
    fun endSession(context: Context) = getPrefs(context).edit() { remove(KEY_USER_ID) }
}

// Load image from web
@Composable
fun RandomPicsumImage(model: String, contentDescription: String, modifier: Modifier, contentScale: ContentScale ) {
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    )
}

fun Long.toAustralianDateTime(): String {
    // 1) Melbourne timezone
    val zone = ZoneId.of("Australia/Melbourne")

    // 2) Formatter: dd/MM/yyyy, hh:mm a (12-hour clock with AM/PM)
    val fmt = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy, hh:mm a",
        Locale("en", "AU")
    )

    // 3) Instant → ZonedDateTime → formatted string
    return Instant
        .ofEpochMilli(this)
        .atZone(zone)
        .format(fmt)
}




