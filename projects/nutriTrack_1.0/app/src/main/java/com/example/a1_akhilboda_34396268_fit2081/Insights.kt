package com.example.a1_akhilboda_34396268_fit2081

import BottomBar
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.A1_AkhilBoda_34396268_FIT2081Theme
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.individualElementPadding
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.paragraph
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.collections.filterKeys


class Insights : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A1_AkhilBoda_34396268_FIT2081Theme {
                // Get userID from intent
                val userID = intent.getStringExtra("userID").toString() // add toString to bypass null safety
                // Get food quality score
                val foodQualityScore = getFoodQualityScore(userID)
                // Mutable state to control information dialog
                val showInfo = remember { mutableStateOf(false) }

                // Map to store food labels and maximum values
                val foodLabels = mapOf<String, Int>(
                    "Discretionary" to 10,
                    "Vegetables" to 5,
                    "Fruit" to 10,
                    "Grains and Cereals" to 5,
                    "Wholegrains" to 5,
                    "Meat and Alternatives" to 10,
                    "Dairy and Alternatives" to 10,
                    "Sodium" to 10,
                    "Alcohol" to 5,
                    "Water" to 5,
                    "Added Sugars" to 10,
                    "Saturated Fat" to 5,
                    "Unsaturated Fat" to 5
                )
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {BottomBar("insights", userID)}
                ) { innerPadding ->
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(start = 5.dp, end = 13.dp)
                        .offset(x = 8.dp)
                    ) {
                        Row (modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Insights",
                                fontSize = 40.sp
                            )
                            IconButton(
                                onClick = {
                                    showInfo.value = true
                                }
                            ) {
                                Icon(Icons.Filled.Info, contentDescription = "Info")
                            }
                        }
                        if (showInfo.value) {ShowInfo(showInfo)}

                        Spacer(modifier = Modifier.padding(individualElementPadding))

                        Column(
                            modifier = Modifier.fillMaxWidth().padding(end = 10.dp).padding(start = 10.dp, end = 10.dp)
                        ) {
                            // Draw rows for each food metric
                            val extractedScores = extractScores(userID)

                            // Zip score values to label entries, dropping header row
                            val foodLabelsMapExtractedScores = foodLabels.entries.toList().zip(extractedScores.drop(0))

                            // Chunk the map into pairs of 2 for each row
                            foodLabelsMapExtractedScores.chunked(2).forEach { rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    rowItems.forEach { (entry, scoreStr) ->
                                        val label = entry.key
                                        val maxValue = entry.value.toFloat()
                                        val score = scoreStr.toFloatOrNull() ?: 0f
                                        val progress = (score / maxValue).coerceIn(0f, 1f)

                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(8.dp)
                                        ) {
                                            Text(text = label, fontSize = paragraph)
                                            LinearProgressIndicator(
                                                progress = { progress },
                                                modifier = Modifier.fillMaxWidth(),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = "$score / ${maxValue.toInt()}",
                                                fontSize = paragraph,
                                                modifier = Modifier.align(Alignment.End)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))


                            Row(
                                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total Score:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                                Card (modifier = Modifier.wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color.DarkGray)) {
                                    Text(foodQualityScore, color = Color.White, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), fontSize = 18.sp)
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))


                            Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                                Button(onClick = {

                                }){
                                    Icon(Icons.Filled.Sports, contentDescription = "Improve diet")
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text("Improve My Diet")
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                // Map extracted values to food labels
                                val foodLabelsKeysExtractedScores = foodLabels.keys.toList().zip(extractedScores.drop(1)).toMap()
                                val shareText = foodLabelsKeysExtractedScores.entries.joinToString() {(key, value) -> "$key = $value"}

                                Button(
                                    onClick = {
                                        // Set intent to send
                                        val intent = Intent(ACTION_SEND)
                                        // Define send/intent type as plain text
                                        intent.type = "text/plain"
                                        intent.putExtra(Intent.EXTRA_TEXT, "My NutriTrack Insights:\n\n$shareText\n\nTotal Score: $foodQualityScore")
                                        startActivity(Intent.createChooser(intent, "Share insights via"))
                                    },
                                ) {
                                    Icon(
                                        Icons.Filled.Share,
                                        contentDescription = "Share My Results"
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Share My Results")
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun extractScores(userID: String): List<String> {
    // Create local context
    val context = LocalContext.current

    var extractedScores = mapOf<String, String>()


    try {
        val inputStream = context.assets.open("users.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        // Capture header row
        val csvHeaderRow = reader.readLine().split(",")
        // Get CSV line for userID
        val csvUserRow = accessCSV(context, "users.csv", userID)
        // Get user sex
        val userSex = csvUserRow[4]
        //Map user row to header row
        val userMap = csvHeaderRow.zip(csvUserRow).toMap()

        // Filter through map to find values containing HEIFA and sex
        extractedScores = userMap.filterKeys { key ->
            key.endsWith("HEIFAscore$userSex", ignoreCase = true)
        }
        Log.d("HEIFA Scores", extractedScores.toString())

    } catch (e: Exception) {
        Log.e("CSV Access Error", "An error occurred when trying to retrieve CSV header row")
    }
    return extractedScores.values.toList()
}

/**
 * Function which show the info modal
 */
@Composable
fun ShowInfo(showInfo: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { showInfo.value = false },
        title = {
            Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("What are Insights?")
            }
        },
        icon = { Image(painter = painterResource(R.drawable.info), contentDescription = "Info Icon") },
        text = {
            Text(
                "Insights show you a breakdown of how your food choices stack up against national dietary guidelines.\n\n" +
                        "The bars give you an idea of where you’re doing well — and where there’s room to grow!"
            , textAlign = TextAlign.Center)
        },
        confirmButton = {
            Button(onClick = { showInfo.value = false }) {
                Text("Done")
            }
        }
    )

}