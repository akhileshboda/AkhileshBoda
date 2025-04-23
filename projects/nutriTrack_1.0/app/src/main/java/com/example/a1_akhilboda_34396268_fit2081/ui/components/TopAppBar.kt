package com.example.a1_akhilboda_34396268_fit2081.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.heading1

/**
 * Function to serve as a modular top app bar to achieve higher cohesion
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Function takes in parameters for title of screen and calling function context
fun TopBar(title: String, onBackClick: () -> Unit = {}) {
    CenterAlignedTopAppBar (
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = title,
                fontSize = heading1,
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            IconButton(
                // Return back to calling screen/Activity
                onClick = onBackClick
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }

    )
}
