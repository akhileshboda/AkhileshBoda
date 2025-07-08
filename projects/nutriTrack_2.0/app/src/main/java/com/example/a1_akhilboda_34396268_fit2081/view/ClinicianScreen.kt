package com.example.a1_akhilboda_34396268_fit2081.view

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.a1_akhilboda_34396268_fit2081.ui.components.TopBar
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.heading1
import com.example.a1_akhilboda_34396268_fit2081.view.ui.theme.A3Theme
import com.example.a1_akhilboda_34396268_fit2081.data.AppDatabase
import com.example.a1_akhilboda_34396268_fit2081.data.UserRepository
import com.example.a1_akhilboda_34396268_fit2081.viewmodel.ClinicianViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

class ClinicianScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val context = this
        val userDao = AppDatabase.getDatabase(context).userDao()
        val repo = UserRepository(userDao)
        val vm = ViewModelProvider(this, ClinicianViewModel.Factory(repo))[ClinicianViewModel::class.java]

        setContent {
            A3Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {TopBar("Clinician Panel", onBackClick = { context.finish()} )}

                ) {innerPadding ->
                    LazyColumn (
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(8.dp)
                    ) {
                        item { ShowAverageHEIFAScores(vm) }
                        item { ShowPatientDataPatterns() }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowAverageHEIFAScores(vm: ClinicianViewModel) {
    val averageHeifaMale    by vm.averageHeifaMale.collectAsState(initial = 0f)
    val formattedAvgHeifaMale = String.format("%.2f", averageHeifaMale)
    val averageHeifaFemale  by vm.averageHeifaFemale.collectAsState(initial = 0f)
    val formattedAvgHeifaFemale = String.format("%.2f", averageHeifaFemale)

    // Fire once to update average scores with state value via vm
    LaunchedEffect(Unit) {
        vm.calculateAverageHeifaScores()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "AVERAGE HEIFA SCORES",
                color = Color.DarkGray,
                fontSize = heading1,
                textDecoration = TextDecoration.Underline,
            )
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                Modifier.wrapContentSize(),
                colors = CardDefaults.cardColors(containerColor = Color.Blue),
            ) {
                Row(Modifier.padding(5.dp),) {
                    Icon(Icons.Filled.Male, contentDescription = "", tint = Color.White)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Male: $formattedAvgHeifaMale",
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                Modifier.wrapContentSize(),
                colors = CardDefaults.cardColors(containerColor = Color.Magenta),
            ) {
                Row(Modifier.padding(5.dp),) {
                    Icon(Icons.Filled.Female, contentDescription = "", tint = Color.White)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Female: $formattedAvgHeifaMale",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ShowPatientDataPatterns() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Text(
                text = "PATIENT DATA PATTERNS",
                modifier = Modifier.fillMaxWidth(),
                color = Color.DarkGray,
                fontSize = heading1,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center
            )
            Text("1. ")
            Text("2. ")
            Text("3. ")
        }
    }
}