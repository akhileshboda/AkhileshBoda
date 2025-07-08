package com.example.a1_akhilboda_34396268_fit2081.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.a1_akhilboda_34396268_fit2081.SessionManager
import com.example.a1_akhilboda_34396268_fit2081.model.Persona
import com.example.a1_akhilboda_34396268_fit2081.model.AppDatabase
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.FoodIntakeRepository
import com.example.a1_akhilboda_34396268_fit2081.ui.components.TopBar
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.A1_AkhilBoda_34396268_FIT2081Theme
import com.example.a1_akhilboda_34396268_fit2081.viewmodel.FoodIntakeViewModel

class FoodIntakeQuestionnaire : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = intent.getStringExtra("userID")!!.toInt()
        val dao    = AppDatabase.getDatabase(this).foodIntakeDao()
        val repo   = FoodIntakeRepository(dao)
        val vm     = ViewModelProvider(
            this,
            FoodIntakeViewModel.Factory(userId, repo)
        )[FoodIntakeViewModel::class.java]

        setContent {
            // get the activity from LocalContext
            val activity = LocalContext.current as ComponentActivity

            // collect your saved event
            LaunchedEffect(vm.saved) {
                vm.saved.collect {
                    // once saved, navigate & finish
                    activity.startActivity(
                        Intent(activity, HomeScreen::class.java)
                            .apply { putExtra("userID", userId.toString()) }
                    )
                    activity.finish()
                    // Save session
                    SessionManager.startSession(this@FoodIntakeQuestionnaire, userId)
                }
            }

            A1_AkhilBoda_34396268_FIT2081Theme {
                val context = LocalContext.current as Activity
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {TopBar("Food Intake Questionnaire", onBackClick = { context.finish()} )}
                ) {innerPadding ->

                    // 1) The user’s saved times as strings:
                    val timings by vm.timings.collectAsState(initial = Triple("", "", ""))
                    val (largestMealTime, wakeUpTime, bedTime) = timings

                    // 2) Your initial picker positions:
                    val initialsMap by vm.timingsInitials.collectAsState(
                        initial = mapOf(
                            'l' to Triple(12, 0, true),
                            'w' to Triple(7,  0, true),
                            'b' to Triple(22, 0, true)
                        )
                    )
                    val initialLargest = initialsMap['l']!!
                    val initialWakeUp  = initialsMap['w']!!
                    val initialBedTime = initialsMap['b']!!

                    val foods         by vm.selectedFoods.collectAsState(emptyList())
                    val persona       by vm.persona.collectAsState("")
                    val canPickWakeUp by vm.canPickWakeUp.collectAsState(false)
                    val canPickBedTime by vm.canPickBedTime.collectAsState(false)
                    val bedTimeError  by vm.bedTimeError.collectAsState(initial = null)
                    val canSave       by vm.canSave.collectAsState(false)
                    val isSaving      by vm.isSaving.collectAsState(false)

                    // Call composables within LazyColumn
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                            .offset(y= -10.dp)
                    ) {
                        item {
                            FoodCategories(
                                selectedFoods = foods,
                                onSelectionChange = { vm.setFoods(it)}
                            )
                        }
                        item {
                            Personas(
                                selectedPersona = persona,
                                onPersonaSelected = { vm.setPersona(it) }
                            )
                        }
                        item {
                            Timings(
                                initialLargest = initialLargest,
                                initialWakeUp   = initialWakeUp,
                                initialBedTime  = initialBedTime,
                                largestMeal    = largestMealTime,
                                wakeUp         = wakeUpTime,
                                bedTime        = bedTime,
                                canPickWakeUp  = canPickWakeUp,
                                canPickBedTime = canPickBedTime,
                                bedTimeError   = bedTimeError,
                                onTimeChange   = { nl, nw, nb -> vm.setTimings(nl, nw, nb) }
                            )

                        }
                        item {
                            Row(modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Button(
                                    onClick = { vm.clearForm() },
                                    enabled = foods.isNotEmpty() || persona.isNotEmpty() || timings.first.isNotEmpty() || timings.second.isNotEmpty() || timings.third.isNotEmpty()
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = "", modifier = Modifier.offset(x = -8.dp))
                                    Text("Clear")
                                }
                                Button(
                                    onClick = {
                                        if (canSave) vm.save()
                                    },
                                    enabled = canSave
                                ) {
                                    Text("Continue")
                                    Icon(Icons.Filled.ArrowForward, contentDescription = "", modifier = Modifier.offset(x = 8.dp))
                                }
                            }
                        }
                    }
                    if (isSaving) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f))
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodCategories(
    selectedFoods: List<String>,
    onSelectionChange: (List<String>) -> Unit
) {
    val options = listOf(
        "Veg", "Fruits", "Grains",
        "Meats", "Dairy", "Fats & Oils",
        "Nuts & Seeds", "Eggs", "Poultry"
    )

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
        Column(Modifier.padding(16.dp)) {
            Text(
                "Select foods you can eat:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(8.dp))

            options.chunked(3).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { food ->
                        // Each cell gets equal weight
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.CenterStart  // or Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val checked = selectedFoods.contains(food)
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = { isChecked ->
                                        val updated = selectedFoods.toMutableList().apply {
                                            if (isChecked) add(food) else remove(food)
                                        }
                                        onSelectionChange(updated)
                                    }
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(food)
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Personas(
    selectedPersona: String,
    onPersonaSelected: (String) -> Unit
) {
    // Data: title, description, drawable name
    val personas = listOf(
        Persona(
            "Nutrition Navigator",
            "Always researching and fine-tuning their meals for optimal nutrition and performance.",
            "persona_nutrition_navigator"
        ),
        Persona(
            "Intuitive Eater",
            "Listens to their body's signals and eats what feels right in the moment.",
            "persona_intuitive_eater"
        ),
        Persona(
            "Fitness Fueller",
            "Eats with purpose to support workouts, recovery and physical goals.",
            "persona_fitness_fueller"
        ),
        Persona(
            "Balanced Nourisher",
            "Strives for a middle ground—healthy most of the time, indulgent when it counts.",
            "persona_balanced_nourisher"
        ),
        Persona(
            "Wellness Wanderer",
            "Interested in being healthy but still figuring out the 'right' way to eat.",
            "persona_wellness_wanderer"
        ),
        Persona(
            "Food Adventurer",
            "Eats for pleasure, exploration and experiences. No rules at all!",
            "persona_food_adventurer"
        )
    )

    // State for which persona’s info to show in dialog (or null ⇒ no dialog)
    var dialogPersona by remember { mutableStateOf<Persona?>(null) }
    val imageContext = LocalContext.current

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
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Your Persona",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(8.dp))

            // 2-column grid
            val chunked = personas.chunked(2)
            chunked.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    row.forEach { persona ->
                        Button(
                            onClick = { dialogPersona = persona },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 5.dp)
                        ) {
                            Text(persona.name, textAlign = TextAlign.Center, maxLines = 1)
                        }
                    }
                    if (row.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(4.dp))
            // Dropdown to actually pick the persona
            ExposedDropdownMenuBox(
                expanded = false,  // we use buttons + dialog, so dropdown is static
                onExpandedChange = { }
            ) {
                OutlinedTextField(
                    value = selectedPersona,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Selected Persona") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
            }
        }
    }

    // Show the dialog when a persona button is tapped
    dialogPersona?.let { p ->
        AlertDialog(
            onDismissRequest = { dialogPersona = null },
            title = {
                Text(text = p.name, style = MaterialTheme.typography.titleLarge)
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val resId = imageContext.resources.getIdentifier(
                        p.drawableName, "drawable", imageContext.packageName
                    )
                    if (resId != 0) {
                        Image(
                            painter = painterResource(resId),
                            contentDescription = p.name,
                            modifier = Modifier
                                .size(120.dp)
                                .padding(bottom = 8.dp)
                        )
                    }
                    Text(p.description, textAlign = TextAlign.Center)
                }
            },
            confirmButton = {
                Button(onClick = {
                    onPersonaSelected(p.name)
                    dialogPersona = null
                }) {
                    Text("Choose")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Timings(
    initialLargest: Triple<Int, Int, Boolean>,
    initialWakeUp: Triple<Int, Int, Boolean>,
    initialBedTime: Triple<Int, Int, Boolean>,
    largestMeal: String,
    wakeUp: String,
    bedTime: String,
    canPickWakeUp: Boolean,
    canPickBedTime: Boolean,
    bedTimeError: String?,
    onTimeChange: (String, String, String) -> Unit,
) {

    // state for which picker is open
    var pickerIndex by remember { mutableIntStateOf(-1) }
    val pickerStates = listOf(
        rememberTimePickerState(initialHour = initialLargest.first, initialMinute = initialLargest.second, is24Hour = initialLargest.third),
        rememberTimePickerState(initialHour = initialWakeUp.first, initialMinute = initialWakeUp.second, is24Hour = initialWakeUp.third),
        rememberTimePickerState(initialHour = initialBedTime.first, initialMinute = initialBedTime.second, is24Hour = initialWakeUp.third)
    )
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
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Select Your Typical Timings", style = MaterialTheme.typography.titleMedium)

            @Composable
            fun timeRow(label: String, value: String, index: Int, enabled: Boolean) {
                OutlinedTextField(
                    value = value,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(label) },
                    trailingIcon = {
                        IconButton(
                            onClick = { if (enabled) pickerIndex = index },
                            enabled   = enabled
                        ) {
                            Icon(Icons.Default.AccessTime, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            timeRow("Largest meal at", largestMeal, 0, true)
            timeRow("Wake up at",      wakeUp,       1, canPickWakeUp)
            timeRow("Go to bed at",    bedTime,      2, canPickBedTime)

            bedTimeError?.let {
                Spacer(Modifier.height(4.dp))
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
        }
    }

    if (pickerIndex in 0..2) {
        val state = pickerStates[pickerIndex]
        AlertDialog(
            onDismissRequest = { pickerIndex = -1 },
            confirmButton = {
                TextButton(onClick = {
                    val h = state.hour
                    val m = state.minute
                    val formatted = String.format("%02d:%02d", h, m)
                    val (l, w, b) = Triple(largestMeal, wakeUp, bedTime)
                    when (pickerIndex) {
                        0 -> onTimeChange(formatted, w, b)
                        1 -> onTimeChange(l, formatted, b)
                        2 -> onTimeChange(l, w, formatted)
                    }
                    pickerIndex = -1
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { pickerIndex = -1 }) { Text("Cancel") }
            },
            text = { TimePicker(state = state) }
        )
    }
}



