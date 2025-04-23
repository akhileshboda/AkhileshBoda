package com.example.a1_akhilboda_34396268_fit2081

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.A1_AkhilBoda_34396268_FIT2081Theme
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.frameBoundaryPadding
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.individualElementPadding
import kotlin.math.exp
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            //context.getSharedPreferences("a1.sp", Context.MODE_PRIVATE).edit() { clear() }
            A1_AkhilBoda_34396268_FIT2081Theme {
                // Use Scaffold for proper framing of elements within device screen and mitigate overlap with system UI elements
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Use Box so that elements can be placed on top of each other
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Create a mutable state which screen recognises for showing Overlay
                        var showLoginOverlay = remember { mutableStateOf(false) }

                        // Column to contain main screen elements
                        Column(
                            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(start = 10.dp, end = 10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Logo, Disclaimer and Login button take up 3/4 of screen space
                            Column(
                                modifier = Modifier.fillMaxWidth().weight(3f),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Call function to display Logo
                                Logo()
                                // Call function to display Disclaimer
                                Disclaimer()

                                Spacer(modifier = Modifier.height(individualElementPadding))

                                // Call function to display login button
                                Login(showLoginOverlay)
                            }
                            Spacer(modifier = Modifier.weight(1f))

                            // Column contains student credit, allocated 1/4 screen space
                            Column(
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Designed by Akhil Boda (34396268)",
                                    fontSize = 12.sp,
                                    fontStyle = FontStyle.Italic
                                )
                            }
                        }
                        // Show overlay when login button is clicked
                        if (showLoginOverlay.value) {
                            ShowLoginOverlay(showLoginOverlay = showLoginOverlay)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Function which displays app logo
 */
@Composable
fun Logo() {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
        ) {
        // Draw logo using image stored in Resources directory
        Image (
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "NutriTrack Logo",
            modifier = Modifier.size(80.dp)
        )
        Text(
            text = "NutriTrack",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.offset(x = (-20).dp, y = 3.dp)
        )
    }
}

/**
 * Function to display app disclaimer text.
 */
@Composable
fun Disclaimer() {
    Text(
        text = "NutriTrack helps you understand your food quality score based on data collected by your healthcare professional. This app is not for tracking calories or diagnosing conditions—it’s designed to support informed, healthy food choices after a clinical dietary assessment.",
        style = MaterialTheme.typography.bodySmall.copy(
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            lineHeight = 18.sp
        ),
        modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp),
        textAlign = TextAlign.Center
    )
}

/**
 * Function to display Login button to users, activates the state of overlay upon click
 */
@Composable
fun Login(showLoginOverlay: MutableState<Boolean>) {
    Button (
        onClick = {
            showLoginOverlay.value = true
        }
    ) {
        Text(text = "Login")
    }
}

/**
 * Function which shows the login overlay for users to enter their details
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowLoginOverlay(showLoginOverlay: MutableState<Boolean>) {
    // Assign local context
    val context = LocalContext.current

    // Set conditional overlay colour based on device theme
    var overlayColor: Float = if (isSystemInDarkTheme()) 0.2f else 0.5f

    // Set mutable state variables to remember user entered login and password
    var userID = remember { mutableStateOf("") }
    var phoneNumber = remember { mutableStateOf("") }

    // Get userIDs from CSV
    val userIDs = getUserIDs(context)

    // Mutable state to hold the expanded menu
    val expanded = remember { mutableStateOf(false) }

    // Ensure that fields are not left blank
    var userIDisEmpty = remember { mutableStateOf(false) }
    var phoneNumberIsEmpty = remember { mutableStateOf(false)}

    // Declare mutable state for retrieving password from CSV
    var csvUserID = remember { mutableStateOf("") }
    var csvPhoneNumber = remember { mutableStateOf("") }

    // Hide keyboard after Login form is submitted
    val keyboardController = LocalSoftwareKeyboardController.current

    // When overlay disappears to clear userID and phone number values
    DisposableEffect(showLoginOverlay.value) {
        onDispose {
            if (!showLoginOverlay.value) {
                // Set userID value to empty string
                userID.value = ""

                // Set phone number value to empty string
                phoneNumber.value = ""
                // Restore phoneNumber isEmpty flag
                phoneNumberIsEmpty.value = true
            }
        }
    }

    // Use Box to stack elements
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = overlayColor)).clickable(onClick = {showLoginOverlay.value = false}), // Exit the overlay but tapping outside the card
    ) {
        // Define card style using Material theming and st
        Card (
            modifier = Modifier.size(width = 300.dp, height = 600.dp).align(Alignment.TopCenter).offset(y = 80.dp).clickable(enabled = false) {}, // Ensure that overlay cannot be exited from clicking within the Login card
            elevation = CardDefaults.elevatedCardElevation(30.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            // Arrange card elements
            Column(modifier = Modifier.fillMaxSize().padding(top = frameBoundaryPadding, start = 20.dp, end = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                // Allocate Login title and disclaimer text 1/2.5 of Card space
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "LOGIN",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(individualElementPadding))

                Text(
                    text = "This app is for pre-registered users only. Please ensure you have created an account through your healthcare provider and have received your MyID.",
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp,
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic
                )

                // Allocated TextFields 0.5/2.5 of Card space
                Spacer(modifier = Modifier.weight(0.5f))

                // Dropdown menu for displaying userIDs from CSV
                ExposedDropdownMenuBox(
                    expanded = expanded.value,
                    onExpandedChange = { expanded.value = !expanded.value}
                ) {
                    TextField(
                        value = userID.value,
                        onValueChange = {},
                        readOnly = true,
                        label = {Text("My ID")},
                        leadingIcon = {Icon(Icons.Filled.Person, contentDescription = "User ID", modifier = Modifier.size(20.dp))},
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    // Dropdown menu for userIDs
                    val scrollState = rememberScrollState()
                    ExposedDropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = {expanded.value = false},
                        modifier = Modifier.heightIn(max = 200.dp).verticalScroll(scrollState)
                    ) {
                        userIDs.forEach { id ->
                        DropdownMenuItem(
                            text = { Text(id) },
                            onClick = {
                                userID.value = id.toString()
                                expanded.value = false
                                userIDisEmpty.value = false
                            }
                        )}
                    }
                }
                // Display error message if phone number field is left empty
                if (userIDisEmpty.value) {
                    Text(
                        text = "Please enter the phone number that is associated with your MyID.",
                        modifier = Modifier.align(Alignment.Start)
                            .padding(start = 15.dp, top = 5.dp),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 8.sp,
                        lineHeight = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(individualElementPadding))

                // Take user phone number
                TextField(
                    value = phoneNumber.value,
                    onValueChange = {
                        phoneNumber.value = it
                        phoneNumberIsEmpty.value = it.isEmpty() // Flag error if field is attempted to leave empty
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "Phone Number",
                            fontSize = 10.sp,
                            modifier = Modifier.offset(x = (-5).dp, y = 2.dp),
                            maxLines = 1
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword), // Password priority precedes .clickable(), ensures UI elements do not prevent keyboard from appearing
                    leadingIcon = {
                        Icon(Icons.Filled.Phone, contentDescription = "Phone Number")
                    },
                    trailingIcon = {
                        // Clear phone number
                        if (!phoneNumberIsEmpty.value) {
                            IconButton(onClick = {phoneNumber.value = ""}) { Icon(Icons.Filled.Clear, contentDescription = "Clear Phone Number") }
                        }
                    },
                    placeholder = {Text("e.g. 614123456789")}
                )
                // Display error message if phone number field is left empty
                if (phoneNumberIsEmpty.value) {
                    Text(
                        text = "Please enter the phone number that is associated with your MyID.",
                        modifier = Modifier.align(Alignment.Start). padding(start = 15.dp, top = 5.dp),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 8.sp,
                        lineHeight = 12.sp
                    )
                } else if (phoneNumber.value.count() > 11) {
                    Text(
                        text = "Please enter the Australian Phone Number correctly.",
                        modifier = Modifier.align(Alignment.Start). padding(start = 15.dp, top = 5.dp),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 8.sp,
                        lineHeight = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))


                // Draw button for authentication and proceed to the next screen
                Button(
                    onClick = {
                        keyboardController?.hide() // Hide keyboard

                        // Set empty field flags
                        userIDisEmpty.value = userID.value.isEmpty()
                        phoneNumberIsEmpty.value = phoneNumber.value.isEmpty()

                        // Early return if any field is empty
                        if (userIDisEmpty.value || phoneNumberIsEmpty.value) {
                            Toast.makeText(
                                context,
                                "Please complete all fields before continuing.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button //Exit this click without running remainder input validation logic
                        }

                        // Check phone number length
                        if (phoneNumber.value.length != 11) {
                            Toast.makeText(
                                context,
                                "Please enter a valid 11-digit Australian phone number.",
                                Toast.LENGTH_LONG
                            ).show()
                            return@Button //Exit this click without running remainder input validation logic
                        }

                        // Access CSV and validate user
                        val csvLine = accessCSV(context, "users.csv", userID.value)

                        if (csvLine.size >= 2) {
                            csvUserID.value = csvLine[1]
                            csvPhoneNumber.value = csvLine[0]

                            // Match user input to CSV contents
                            if (userID.value == csvUserID.value && phoneNumber.value == csvPhoneNumber.value) {
                                // Get sharedPreferences for user
                                val sharedPreferences = context.getSharedPreferences("${userID.value}.sp", Context.MODE_PRIVATE)
                                // Create intent for passing to next screen, if user has logged in on device with questionnaire already completed, redirect to homescreen
                                val intent = if (sharedPreferences.all.size < 3) { // if questionnaire data has been added, size of sp is greater than 3
                                    Intent(context, FoodIntakeQuestionnaire::class.java)
                                } else {
                                    Intent(context, HomeScreen::class.java)
                                }

                                showLoginOverlay.value = false
                                intent.putExtra("userID", userID.value)
                                // Store first and last name in shared preferences for particular userid
                                context.getSharedPreferences("${userID.value}.sp", Context.MODE_PRIVATE).edit() {
                                    putString("LastName", csvLine[2])
                                    putString("FirstName", csvLine[3])
                                    apply()
                                }
                                context.startActivity(intent)
                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(
                                    context,
                                    "USER NOT FOUND.\nPlease check your details and try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Invalid MyID entered. Please select a valid ID from the dropdown.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                ) {
                    Text("Continue")
                }
                // Continue button is given 1/2.5 Card space
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}