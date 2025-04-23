package com.example.a1_akhilboda_34396268_fit2081

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardElevation
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.a1_akhilboda_34396268_fit2081.ui.components.TopBar
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.A1_AkhilBoda_34396268_FIT2081Theme
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.cardPadding
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.frameBoundaryPadding
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.heading2
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.paragraph
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.paragraphBelowHeading
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.paragraphLineHeight
import androidx.core.content.edit


class FoodIntakeQuestionnaire : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Get current screen
            val context = LocalContext.current as Activity // for posterity, ignoring unsafe type cast as in this case it is to assume this context is always an Activity
            A1_AkhilBoda_34396268_FIT2081Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    // Import custom defined TopAppBar
                    topBar = {
                        TopBar("Food Intake Questionnaire", onBackClick = { context.finish() })
                    }
                ) {innerPadding ->
                    // Column to arrange child elements
                    Column (modifier = Modifier.fillMaxSize().padding(innerPadding).padding(start = 5.dp, end = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        // Retrieve userID from MainActivity
                        val userID = intent.getStringExtra("userID")

                        // Load shared preferences to populate mutable states
                        val sharedPreferences = getSharedPreferences("$userID.sp", Context.MODE_PRIVATE)
                        val sharedPreferencesFoods = sharedPreferences
                            .getString("foods", "")?.split(",")
                            ?.map { it.trim() }
                            ?.toMutableList() ?: mutableListOf()
                        val sharedPreferencesPersona = sharedPreferences.getString("persona", "") ?: ""
                        val sharedPreferencesTimings = sharedPreferences
                        .getString("timings", "")?.split(",")
                        ?.map { it.trim() } // remove white spaces
                        ?.toMutableList() ?: mutableListOf()

                        // Mutable list to store selected food items
                        val selectedFoods = remember { mutableStateListOf<String>().apply { addAll(sharedPreferencesFoods) } } // Always start with a fresh list of selected vegetables
                        // Mutable state String for selected persona
                        val selectedPersona = remember { mutableStateOf(sharedPreferencesPersona) }
                        // Mutable state to hold data for time pickers
                        val selectedTimings = remember { mutableStateListOf<String>().apply { addAll(sharedPreferencesTimings) } }
                        // Mutable state for showing confirmation dialog
                        val showConfirmDialog = remember { mutableStateOf(false) }

                        // Card to contain food selection checkboxes
                        Card (
                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, Color.Black),
                            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                        ) {
                            FoodCategories(selectedFoods)
                        }

                        // Card which contains Persona information and selection
                        Card (
                            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(vertical = cardPadding),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, Color.Black),
                            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                        ) {
                            // Select Persona
                            Personas(selectedPersona)
                        }

                        Card (
                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, Color.Black),
                            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                        ) {
                            Timings(selectedTimings)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 5.dp, end = 5.dp)) {
                            Column (modifier = Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.Start) {
                                // Draw clear button
                                Button(
                                    onClick = {
                                        selectedFoods.clear()
                                        selectedPersona.value = ""
                                        selectedTimings.clear()
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Clear",
                                        modifier = Modifier.offset(x = (-5).dp)
                                    )
                                    Text("Clear")
                                }
                            }
                            Column (modifier = Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.End) {
                                Button(
                                    onClick = {
                                        // Input validation
                                        if (selectedFoods.isEmpty() || selectedPersona.value.isEmpty() || selectedTimings.isEmpty()) {
                                            Toast.makeText(
                                                context,
                                                "Please ensure that fields from all sections have been completed",
                                                Toast.LENGTH_SHORT
                                            ).show() // Display login failure message
                                        } else if (selectedTimings.size < 3 || selectedTimings.contains("")) {
                                            Toast.makeText(
                                                context,
                                                "Please ensure that all timings have been selected.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            showConfirmDialog.value = true;
                                        }
                                    },
                                ) {
                                    Text("Save")
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Save",
                                        modifier = Modifier.offset(x = 5.dp)
                                    )
                                }
                            }
                        }

                        if (showConfirmDialog.value) {
                            AlertDialog(
                                onDismissRequest = { showConfirmDialog.value = false },
                                icon = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.confirm_icon),
                                            contentDescription = "Confirmation Icon",
                                            modifier = Modifier.size(48.dp) // Optional: adjust size as needed
                                        )
                                    }
                                },

                                title = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Confirm Details",
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                },

                                text = {
                                    Text(
                                        text = "Are you sure you want to confirm and save these responses?",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },

                                dismissButton = {
                                    Button(
                                        onClick = { showConfirmDialog.value = false }
                                    ) {
                                        Text("Cancel")
                                    }
                                },

                                // Get currently logged in user Id


                                confirmButton = {
                                    Button(
                                        onClick = {
                                            context.getSharedPreferences(
                                                "$userID.sp",
                                                Context.MODE_PRIVATE
                                            ).edit() {

                                                putString("foods", selectedFoods.joinToString())
                                                putString("persona", selectedPersona.value)
                                                putString("timings", selectedTimings.joinToString())
                                            }

                                            val intent = Intent(context, HomeScreen::class.java)
                                            intent.putExtra("userID", userID)
                                            context.startActivity(intent)
                                            finish()
                                            Toast.makeText(context, "Questionnaire Details Saved!",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Text("Confirm")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Function which displays and holds information about selected foods
 */
@Composable
fun FoodCategories(selectedFoods: SnapshotStateList<String>) {

    // List of food options
    val foodOptions = listOf<String>(
        "Vegetables", "Fruits", "Grains", "Meats", "Dairy", "Fats & Oils"
    )

    Column (
        modifier = Modifier.fillMaxWidth().padding(all = frameBoundaryPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = "Select all of the foods that you can eat:",
            fontWeight = FontWeight.Bold,
            fontSize = heading2
        )


        // Iterate through list of food options and display checkboxes, 3 items in a row
        foodOptions.chunked(3).forEach { row ->
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // For each row chunk
                row.forEach { food ->
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Checkbox for each food
                        Checkbox(
                            // Add checked items to selectedFoods list
                            checked = selectedFoods.contains(food),
                            onCheckedChange = {
                                if (it) {
                                    selectedFoods.add(food)
                                } else {
                                    selectedFoods.remove(food)
                                }
                            }
                        )
                        Text(food, fontSize = paragraph)
                    }
                }
            }
        }
    }

}

/**
 * Function to display Persona information
 */
@Composable
fun Personas(selectedPersona: MutableState<String>) {
    // Set button spacing
    val buttonPadding = 5.dp

    // Mutable state variables to store show status of Modals
    val showModal = remember { mutableStateOf<List<String>>(emptyList()) }

    // Create Map to store details for each button
    val buttons = mapOf<String, List<String>>(
        // Key: Button position Value: List of strings for - Title, Persona description, name of image in Resources
        "button1" to listOf(
            "Nutrition Navigator",
            "Always researching and fine-tuning their meals for optimal nutrition and performance.",
            "persona_nutrition_navigator"
        ),
        "button2" to listOf(
            "Intuitive Eater",
            "Listens to their body's signals and eats feels right in the moment.",
            "persona_intuitive_eater"
        ),
        "button3" to listOf(
            "Fitness Fueller",
            "Eats with purpose to support workouts, recovery and physical goals.",
            "persona_fitness_fueller"
        ),
        "button4" to listOf(
            "Balanced Nourisher",
            "Strives for a middle ground - healthy most of the time, indulgent when it counts.",
            "persona_balanced_nourisher"
        ),
        "button5" to listOf(
            "Wellness Wanderer",
            "Interested in being healthy but still figuring out the 'right' way to eat.",
            "persona_wellness_wanderer"
        ),
        "button6" to listOf(
            "Food Adventurer",
            "Eat for pleasure, exploration and experiences. No rules at all!",
            "persona_food_adventurer"
        )
    )

    // Convert map into list fo easier chunking
    val buttonList = buttons.entries.toList()

    Column(
        modifier = Modifier.fillMaxWidth().padding(all = frameBoundaryPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Persona",
            fontWeight = FontWeight.Bold,
            fontSize = heading2
        )

        Spacer(modifier = Modifier.height(paragraphBelowHeading))

        Text(
            text = "People can generally be categorised into six types based on their eating habits. Click on each persona below to explore the different types and choose the one that best describes you.",
            textAlign = TextAlign.Center,
            fontSize = paragraph,
            lineHeight = paragraphLineHeight
        )

        Spacer(modifier = Modifier.height(5.dp))

        buttonList.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = buttonPadding),
                horizontalArrangement = Arrangement.Center
            ) {
                rowItems.forEach { (key, value) ->
                    Button(
                        onClick = {
                            showModal.value = value // or pass value[0] if that’s what the modal uses
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = buttonPadding)
                            .height(40.dp)
                    ) {
                        Text(text = value[0])
                    }
                }
            }
        }
    }

    if (showModal.value.isNotEmpty()) {
        ShowModal(showModal)
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Extract persona names Map
    val personaNames = buttons.values.map { it[0] }

    // Get selected persona and assign to selected persona reference
    SelectPersona(personaNames, selectedPersona)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowModal(showModal: MutableState<List<String>>) {
    val context = LocalContext.current

    // Create drawable path name suitable for painterResource
    val personaImage = context.resources.getIdentifier(
        showModal.value[2], // Get image name from persona modal list
        "drawable",
        context.packageName
    )

    AlertDialog(
        onDismissRequest = { showModal.value = emptyList() },
        title = {

            Column(
                modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = personaImage),
                    contentDescription = "Persona Image",
                    modifier = Modifier.size(200.dp)
                )
                Text(text = showModal.value[0])
            }
        },
        text = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(showModal.value[1], textAlign = TextAlign.Center)
            }
        },
        confirmButton = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = {
                    showModal.value = emptyList()
                }) {
                    Text("Done")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPersona(personas: List<String>, selectedPersona: MutableState<String>) {
    val expanded = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(frameBoundaryPadding), horizontalAlignment = Alignment.CenterHorizontally) {
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth().padding(horizontal = frameBoundaryPadding).offset(y = -(20).dp),
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value }
        ) {
            OutlinedTextField(
                value = selectedPersona.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select option") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                personas.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedPersona.value = selectionOption
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Timings(selectedTimes: SnapshotStateList<String>) {
    // Get the current context for launching dialogs
    val context = LocalContext.current

    // Load values from shared preferences first

    val largestMealTime = remember { derivedStateOf { selectedTimes.getOrNull(0)?.trim().orEmpty().ifEmpty { "-- : --" } } }
    val wakeUpTime = remember { derivedStateOf { selectedTimes.getOrNull(1)?.trim().orEmpty().ifEmpty { "-- : --" } } }
    val bedtime = remember { derivedStateOf { selectedTimes.getOrNull(2)?.trim().orEmpty().ifEmpty { "-- : --" } } }

    // Place in list for accessing by dynamic row creation
    val times = listOf(largestMealTime, wakeUpTime, bedtime)

    // Mutable state to control show time picker
    var showTimePickerDialog = remember { mutableStateOf(false) }

    // Indicate which time picker is selected
    val selectedIndex = remember { mutableStateOf(-1) }

    // List containing time picker states
    val pickerStates = listOf(rememberTimePickerState(18, 0, true), rememberTimePickerState(8, 0, true), rememberTimePickerState(22, 0, true))

    Column(
        modifier = Modifier.fillMaxWidth().padding(frameBoundaryPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Timings",
            fontWeight = FontWeight.Bold,
            fontSize = heading2
        )
        // Row messages
        val messages = listOf(
            "Approximately when do you consume your largest meal?",
            "Approximately when do you wake up in the morning?",
            "Approximately when do you go to bed at night?"
        )

        // Draw all 3 rows
        messages.forEachIndexed { index, message ->
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message,
                    fontSize = paragraph,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(2f).padding(end = 5.dp),
                    lineHeight = paragraphLineHeight
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .size(width = 10.dp, height = 50.dp),
                    value = times[index].value,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            selectedIndex.value = index
                            showTimePickerDialog.value = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Pick Time"
                            )
                        }
                    },
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start)
                )
            }

            // Show TimePickerDialog
            if (showTimePickerDialog.value && selectedIndex.value in 0..2) {
                val currentPickerState = pickerStates[selectedIndex.value]

                AlertDialog(
                    onDismissRequest = {
                        showTimePickerDialog.value = false
                        selectedIndex.value = -1
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val hour = currentPickerState.hour
                            val minute = currentPickerState.minute
                            val timeFormatted = String.format("%02d:%02d", hour, minute)

                            val index = selectedIndex.value

                            // Ensure list has enough capacity before setting value
                            while (selectedTimes.size <= index) {
                                selectedTimes.add("")
                            }
                            selectedTimes[index] = timeFormatted

                            showTimePickerDialog.value = false
                            selectedIndex.value = -1
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showTimePickerDialog.value = false
                            selectedIndex.value = -1
                        }) {
                            Text("Cancel")
                        }
                    },
                    text = {
                        TimePicker(state = currentPickerState)
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}