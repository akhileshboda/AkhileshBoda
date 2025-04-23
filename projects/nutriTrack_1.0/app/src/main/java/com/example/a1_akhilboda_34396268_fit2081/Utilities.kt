package com.example.a1_akhilboda_34396268_fit2081

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Function to check if input is contained in user CS, returning any type of object if found
 */
fun accessCSV(context: Context, fileName: String, userID: String): MutableList<String> {
    val csvLine = mutableStateListOf<String>()

    try {
        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { lines ->
            lines.drop(1).forEach { line ->
                val fields = line.split(",")

                // Check if the second column matches the userID
                if (fields.size > 1 && fields[1].trim() == userID) {
                    csvLine.addAll(fields.map { it.trim() }) // Add the trimmed elements
                    return csvLine // Exit early after finding the matching row
                }
            }
        }
    } catch (e: Exception) {
        Log.e("CSV Access Error", "Error reading CSV: ${e.message}")
    }

    return csvLine
}


fun getUserIDs(context: Context): List<String> {
    val userIDs = mutableListOf<String>()

    try {
        val inputStream = context.assets.open("users.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { lines ->
            lines.drop(1).forEach { line ->
                val rawCsvLine = line.split(",")
                if (rawCsvLine.size > 1) {
                    userIDs.add(rawCsvLine[1].trim())
                }
            }
        }
    } catch (e: Exception) {
        Log.e("CSV Access Error", "Error reading CSV: ${e.message}")
    }

    return userIDs.sortedBy { it.toIntOrNull() ?: Int.MAX_VALUE } // Sort numerically
}

/**
 * Function to get food quality score depending on user specified sex
 */
@Composable
fun getFoodQualityScore(userID: String): String {
    // Create new context
    val context = LocalContext.current
    // Get CSV line for user
    val csv = accessCSV(context, "users.csv", userID)

    // Return food quality score based on user sex, formatted to 2dp
    return if (csv[4].equals("Male", ignoreCase = true)) String.format("%.2f", csv[5].toFloat()) else String.format("%.2f", csv[6].toFloat())
}