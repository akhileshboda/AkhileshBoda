package com.example.a1_akhilboda_34396268_fit2081.view

import BottomBar
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModelProvider
import com.example.a1_akhilboda_34396268_fit2081.RandomPicsumImage
import com.example.a1_akhilboda_34396268_fit2081.fruity.Fruit
import com.example.a1_akhilboda_34396268_fit2081.fruity.FruityRepository
import com.example.a1_akhilboda_34396268_fit2081.fruity.FruityService
import com.example.a1_akhilboda_34396268_fit2081.fruity.FruityViewModel
import com.example.a1_akhilboda_34396268_fit2081.gemini.GeminiViewModel
import com.example.a1_akhilboda_34396268_fit2081.model.AppDatabase
import com.example.a1_akhilboda_34396268_fit2081.model.daos.UserDao
import com.example.a1_akhilboda_34396268_fit2081.model.entities.NutriCoachTipEntity
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.NutriCoachRepository
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.UserRepository
import com.example.a1_akhilboda_34396268_fit2081.toAustralianDateTime
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.heading1
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.individualElementPadding
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.paragraph
import com.example.a1_akhilboda_34396268_fit2081.view.ui.theme.A3Theme
import com.example.a1_akhilboda_34396268_fit2081.viewmodel.NutriCoachViewModel
import com.example.a1_akhilboda_34396268_fit2081.viewmodel.UserViewModel


class NutriCoach : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get userID from Intent
        val userID = intent.getStringExtra("userID").toString()
        // Services
        val fruityService = FruityService.create()
        val userDao: UserDao = AppDatabase.getDatabase(this).userDao()
        val nutriCoachDao = AppDatabase.getDatabase(this).nutriCoachDao()

        // Repos
        val fruityRepository = FruityRepository(fruityService)
        val userRepo = UserRepository(userDao)
        val nutriCoachRepo = NutriCoachRepository(nutriCoachDao)

        // ViewModels
        val fruityVm     = ViewModelProvider(
            this,
            FruityViewModel.Factory(fruityRepository)
        )[FruityViewModel::class.java]

        val userVm      = ViewModelProvider(
            this,
            UserViewModel.Factory(userRepo)
        )[UserViewModel::class.java]

        val nutriCoachVm      = ViewModelProvider(
            this,
            NutriCoachViewModel.Factory(nutriCoachRepo)
        )[NutriCoachViewModel::class.java]

        val geminiVm      = ViewModelProvider(
            this,
            GeminiViewModel.Factory(userRepo)
        )[GeminiViewModel::class.java]

        // Get user
        userVm.getUser(userID.toInt())

        enableEdgeToEdge()
        setContent {
            // Get user fruit score
            val fruitScore by userVm.fruitScore.collectAsState()
            // Observe user
            val user by userVm.selectedUser.collectAsState()
            // convert User Entity toString
            val userToString = user.toString().split(", ").drop(3).joinToString(", ")
            // Update nutriCoachVm with user ID
            nutriCoachVm.setUserId(userID.toInt())
            // Get all tips for user
            val tips = nutriCoachVm.tips.collectAsState(initial = emptyList())
            // Observe for show previous messages dialog
            val showPreviousMessagesDialog by nutriCoachVm.showPreviousMessagesDialog.collectAsState()
            // Observe tips as a list


            A3Theme {
                Scaffold(
                    bottomBar = { BottomBar("nutricoach", userID) },
                ) { innerPadding ->
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                            .offset(y= -10.dp)
                    ) {
                        item {
                            Text(
                                text = "NutriCoach",
                                fontSize = 40.sp
                            )
                            Spacer(modifier = Modifier.padding(individualElementPadding))
                        }
                        item { FruityScreen(vm = fruityVm, fruitScore = fruitScore)}
                        item { Spacer(modifier = Modifier.padding(individualElementPadding)) }
                        item { MotivationalMessage(
                            geminiVm = geminiVm,
                            userToString = userToString,
                            nutriCoachVm = nutriCoachVm
                        ) }
                    }
                    if (showPreviousMessagesDialog) {
                        PreviousMessagesDialog(tips = tips, showPreviousMessagesDialog = showPreviousMessagesDialog, nutriCoachVm = nutriCoachVm)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruityScreen(vm: FruityViewModel, fruitScore: List<Float>) {
    // Collect States
    val query by vm.query.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()
    val fruit by vm.fruit.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Determine whether user fruit score is optimal and display Fruit details
            if (fruitScore[0] < 2 && fruitScore[1] < 2) {
                Text(
                    "FRUIT DETAILS",
                    fontSize = heading1,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Hey! It looks like you're struggling a bit there on your fruit intake, that's okay!",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    "Feel free check out some FRUIT DETAILS for your own information\n\n 🍑🍒🍌🙂",
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(individualElementPadding))

                OutlinedTextField(
                    value = query,
                    onValueChange = { vm.updateQuery(it) },
                    label = { Text("Search Fruit") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "") },
                    trailingIcon = {
                        if (query.isNotEmpty()) IconButton(onClick = { vm.clearQuery() }) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = ""
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
                Button(
                    modifier = Modifier.offset(y = 12.dp),
                    onClick = { vm.loadFruit() },
                    enabled = query.isNotEmpty()
                ) {
                    Text("Search")
                }


                Spacer(modifier = Modifier.padding(8.dp))

                // Display error message
                if (error?.isNotEmpty() == true) {
                    Text(
                        error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                if (isLoading)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.padding(individualElementPadding))
                    }

                if (fruit != null) {
                    DetailsRows(fruit = fruit)
                }
            }
            // Else display random image
            else {
                Text(
                    "LOVE SEEING YOU ABSOLUTELY SMASH OUT YOUR FRUIT INTAKE!!",
                    fontSize = heading1,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline
                )
                RandomPicsumImage(
                    model = "https://picsum.photos/seed/picsum/200/300",
                    contentDescription = "Static Random Image",
                    modifier = Modifier.size(300.dp),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun DetailsRows(fruit: Fruit?) {
    val rowTitles = listOf<String>(
        "Family",
        "Calories",
        "Fat",
        "Sugar",
        "Carbohydrates",
        "Protein"
    )

    rowTitles.forEach { row ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text("$row:", fontSize = paragraph, fontWeight = FontWeight.Bold)

            fruit?.let { f ->
                val fruitValue = when (row) {
                    "Family"        -> f.family
                    "Calories"      -> f.nutritions.calories.toString()
                    "Fat"           -> f.nutritions.fat.toString()
                    "Sugar"         -> f.nutritions.sugar.toString()
                    "Carbohydrates" -> f.nutritions.carbohydrates.toString()
                    "Protein"       -> f.nutritions.protein.toString()
                    else            -> ""
                }
                Text(fruitValue, fontSize = paragraph, textDecoration = TextDecoration.Underline)
            }
        }
    }
}

@Composable
fun MotivationalMessage(geminiVm: GeminiViewModel, userToString: String, nutriCoachVm: NutriCoachViewModel) {
    // Collect States
    val response by geminiVm.answer.collectAsState()
    val isLoading by geminiVm.isLoading.collectAsState()


    // Fire once to update gemini vm
    LaunchedEffect(Unit) {
        geminiVm.provideUserToString(userToString)
    }

    // Watch for update to response and insert tip
    LaunchedEffect(response) {
        if (response.isNotEmpty()) nutriCoachVm.insertNewTip(response)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column (modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "MOTIVATIONAL MESSAGE",
                fontSize = heading1,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )
            Spacer(modifier = Modifier.height(individualElementPadding))

            if(isLoading) { CircularProgressIndicator(); Spacer(modifier = Modifier.height(10.dp)) }

            if (response.isNotEmpty() && !isLoading) Text(response, fontSize = paragraph, textAlign = TextAlign.Center)

            Button(onClick = {
                geminiVm.askGemini()
            }) {
                Icon(Icons.AutoMirrored.Filled.Help, contentDescription = "", modifier = Modifier.offset(x = -8.dp))
                Text("Generate A Message")
            }
            Button(
                onClick = {nutriCoachVm.showPreviousMessagesDialog()}
            ) {
                Icon(Icons.Filled.AllInbox, contentDescription = "", modifier = Modifier.offset(x = -8.dp))
                Text("Previous Messages")
            }
        }

    }
}

@Composable
fun PreviousMessagesDialog(
    tips: State<List<NutriCoachTipEntity>>,
    showPreviousMessagesDialog: Boolean,
    nutriCoachVm: NutriCoachViewModel
) {
    if (!showPreviousMessagesDialog) return

    val config = LocalConfiguration.current
    val isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE
    val maxDialogHeight = if (isLandscape) 275.dp else 600.dp

    AlertDialog(
        onDismissRequest = { nutriCoachVm.hidePreviousMessagesDialog() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.fillMaxWidth(0.8f).wrapContentHeight().heightIn(max = maxDialogHeight),
        icon = {
            Icon(Icons.Filled.AllInbox, contentDescription = "History")
        },
        title = {
            Text("Previous Motivational Messages")
        },
        text = {
            LazyColumn (modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                //.verticalScroll(rememberScrollState())
            ) {
                if (tips.value.isEmpty()) {
                    item {
                        Text(
                            "(no tips yet)",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                } else {
                    tips.value.forEach { tip ->
                        item {
                            Text(
                                text = "${tip.createdAt.toAustralianDateTime()}",
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                            Text(
                                text = tip.tipText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
        },
        dismissButton = {
            Button(onClick = { nutriCoachVm.hidePreviousMessagesDialog() }) {
                Text("Close")
            }
        },
    )
}