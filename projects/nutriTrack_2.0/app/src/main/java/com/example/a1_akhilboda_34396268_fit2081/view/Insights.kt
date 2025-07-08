package com.example.a1_akhilboda_34396268_fit2081.view

import BottomBar
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.a1_akhilboda_34396268_fit2081.model.AppDatabase
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.UserRepository
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.A1_AkhilBoda_34396268_FIT2081Theme
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.individualElementPadding
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.paragraph
import com.example.a1_akhilboda_34396268_fit2081.viewmodel.InsightsViewModel

class Insights : ComponentActivity() {
    private lateinit var vm: InsightsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get dao
        val dao = AppDatabase.getDatabase(this).userDao()
        // Get repo
        val userRepo = UserRepository(dao)
        // Get vm
        vm = ViewModelProvider(this, InsightsViewModel.Factory(userRepo))[InsightsViewModel::class.java]

        enableEdgeToEdge()
        val userId = intent.getStringExtra("userID")!!
        vm.getUser(userId.toInt())
        setContent {
            A1_AkhilBoda_34396268_FIT2081Theme {
                InsightsScreen(vm = vm, userId = userId)
            }
        }
    }
}

@Composable
fun InsightsScreen(vm: InsightsViewModel, userId: String) {
    val totalScore   by vm.totalScore.collectAsState()
    val metrics      by vm.metricScores.collectAsState()
    val showInfo     = remember { mutableStateOf(false) }
    val context      = LocalContext.current

    Scaffold(
        modifier  = Modifier.fillMaxSize(),
        bottomBar = { BottomBar("insights", userId) }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment   = Alignment.CenterVertically
            ) {
                Text("Insights", fontSize = 40.sp)
                IconButton(onClick = { showInfo.value = true }) {
                    Icon(Icons.Default.Info, contentDescription = "Info")
                }
            }
            if (showInfo.value) {
                AlertDialog(
                    onDismissRequest = { showInfo.value = false },
                    title = { Text("What are Insights?") },
                    text = {Text("Insights show you how your eating habits score across key categories.")
                    },
                    confirmButton = {
                        Button(onClick = { showInfo.value = false }) { Text("Close") }
                    }
                )
            }
            Spacer(Modifier.height(individualElementPadding))

            // Render each metric
            metrics.entries.toList().chunked(2).forEach { pair ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    pair.forEach { (label, scoreStr) ->
                        val score = scoreStr.toFloat()
                        val maxVal = when(label) {
                            "Discretionary"          -> 10f
                            "Vegetables"             -> 10f
                            "Fruit"                  -> 10f
                            "Grains and Cereals"     -> 5f
                            "Wholegrains"            -> 5f
                            "Meat and Alternatives"  -> 10f
                            "Dairy and Alternatives" -> 10f
                            "Sodium"                 -> 10f
                            "Alcohol"                -> 5f
                            "Water"                  -> 5f
                            "Added Sugars"           -> 10f
                            "Saturated Fat"          -> 5f
                            "Unsaturated Fat"        -> 5f
                            else -> 1f
                        }
                        Column(
                            Modifier
                                .weight(1f)
                                .padding(8.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(label, fontSize = paragraph, modifier = Modifier.offset(x = 5.dp))
                            LinearProgressIndicator(
                            progress = { (score / maxVal).coerceIn(0f,1f) },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                "$score / $maxVal",
                                fontSize = paragraph,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(105.dp))
            // Total
            Row(
                Modifier.fillMaxWidth().offset(x = -12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment   = Alignment.CenterVertically
            ) {
                Text("Total Score:", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Card(
                    colors   = CardDefaults.cardColors(containerColor = Color.DarkGray),
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(
                        totalScore,
                        color    = Color.White,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 18.sp
                    )
                }
            }
            Spacer(Modifier.height(individualElementPadding))

            // Actions
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    val intent = Intent(context, NutriCoach::class.java)
                    intent.putExtra("userID", userId)
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Default.Sports, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Improve My Diet")
                }
                Button(onClick = {
                    Intent(Intent.ACTION_SEND).also { intent ->
                        intent.type = "text/plain"
                        intent.putExtra(
                            Intent.EXTRA_TEXT,
                            "My NutriTrack Insights:\n" +
                                    metrics.entries.joinToString("\n") { "${it.key} = ${it.value}" } +
                                    "\n\nTotal Score: $totalScore"
                        )
                        context.startActivity(Intent.createChooser(intent, "Share via"))
                    }
                }) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Share My Results")
                }
            }
            Spacer(Modifier.height(individualElementPadding))
        }
    }
}