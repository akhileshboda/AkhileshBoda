package com.example.a1_akhilboda_34396268_fit2081.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a1_akhilboda_34396268_fit2081.R
import com.example.a1_akhilboda_34396268_fit2081.SessionManager
import com.example.a1_akhilboda_34396268_fit2081.model.AppDatabase
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.FoodIntakeRepository
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.UserRepository
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.A1_AkhilBoda_34396268_FIT2081Theme
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.individualElementPadding
import com.example.a1_akhilboda_34396268_fit2081.viewmodel.LoginViewModel
import com.example.a1_akhilboda_34396268_fit2081.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    // Use by viewModels() delegate for proper ViewModel lifecycle management
    private val loginVm: LoginViewModel by viewModels {
        val userDao = AppDatabase.getDatabase(this).userDao()
        val foodIntakeDao = AppDatabase.getDatabase(this).foodIntakeDao()
        val userRepo = UserRepository(userDao)
        val foodIntakeRepo = FoodIntakeRepository(foodIntakeDao)
        LoginViewModel.Factory(userRepo, foodIntakeRepo)
    }

    private val userVm: UserViewModel by viewModels {
        val userDao = AppDatabase.getDatabase(this).userDao()
        val userRepo = UserRepository(userDao)
        UserViewModel.Factory(userRepo) // You'll need to create this factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Gert
        enableEdgeToEdge()
        setContent {
            A1_AkhilBoda_34396268_FIT2081Theme {
                Scaffold(
                    bottomBar =  {
                        Column(modifier = Modifier.fillMaxWidth().offset(y = -(20).dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Nametag()
                        }
                    }
                ) { innerPadding ->
                    // Pass vms to main screen
                    MainScreen(padding = innerPadding, context = this, userVm = userVm, loginVm = loginVm)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    padding: PaddingValues,
    context: ComponentActivity,
    userVm: UserViewModel,
    loginVm: LoginViewModel,
) {
    // Collect states
    // Login ViewModel
    val showLoginOverlay by loginVm.showLoginOverlay.collectAsState()
    val userPhoneInput by loginVm.userPhoneInput.collectAsState()
    val phoneErr by loginVm.phoneErr.collectAsState()
    val newPassword by loginVm.newPassword.collectAsState()
    val newPasswordError by loginVm.newPasswordError.collectAsState()
    val confirmPassword by loginVm.confirmPassword.collectAsState()
    val confirmPasswordError by loginVm.confirmPasswordError.collectAsState()
    val existingPassword by loginVm.existingPassword.collectAsState()
    val existingPasswordError by loginVm.existingPasswordError.collectAsState()
    val isLoading by loginVm.isLoading.collectAsState()
    val enableContinueButton by loginVm.enableContinueButton.collectAsState()

    // User ViewModel
    val userIDs by userVm.userIds.collectAsState(emptyList())
    val selectedUser by userVm.selectedUser.collectAsState()
    val expanded by userVm.expanded.collectAsState()
    val isFirstTime by userVm.isFirstTime.collectAsState()

    val selectedUserId =  selectedUser?.id?.toString() ?: ""
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()
        Disclaimer()
        Spacer(modifier = Modifier.height(individualElementPadding))
        Button(
            onClick = {loginVm.showLoginOverlay()}
        ) {
            Text("Login")
        }
    }

    if (showLoginOverlay) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(Modifier.matchParentSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .pointerInput(Unit) {
                    detectTapGestures {
                        loginVm.hideLoginOverlay()
                        userVm.clearSelectedUser()
                    }
                }) {}
            Card(
                Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.TopCenter)
                    .padding(top = 50.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("LOGIN", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                    ExposedDropdownMenuBox(
                        expanded       = expanded,
                        onExpandedChange = { userVm.toggleDropdown() }
                    ) {
                        OutlinedTextField(
                            value       = selectedUserId,
                            onValueChange = { /*read only */ },
                            leadingIcon = {Icon(Icons.Filled.Person, null)},
                            readOnly    = true,
                            label       = { Text("My ID") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier    = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded       = expanded,
                            onDismissRequest = { userVm.toggleDropdown() }
                        ) {
                            userIDs.forEach { id ->
                                DropdownMenuItem(
                                    text    = { Text(id.toString()) },
                                    onClick = {
                                        userVm.selectUserId(id)   // ← load the user
                                        loginVm.resetAll()
                                    }
                                )
                            }
                        }
                    }
                    // Once user is selected show either a password field or phone number field
                    if (selectedUser != null && selectedUser!!.password == null) {
                        // Enable is firstTime flag
                        // Show phone number field
                        Spacer(Modifier.height(12.dp))
                        Text("It seems like you are registering for the first time, enter your phone number below.", fontSize = 12.sp, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = userPhoneInput,
                            onValueChange = { loginVm.updatePhoneInput(it, registeredPhone = selectedUser!!.phoneNumber) },
                            label = { Text("Phone Number") },
                            isError = !phoneErr.isNullOrEmpty(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            leadingIcon = { Icon(Icons.Filled.Phone, null) },
                            trailingIcon = {
                                if (userPhoneInput.isNotEmpty()) IconButton({ loginVm.clearPhone() }) {
                                    Icon(Icons.Filled.Clear, null)
                                }
                            },
                            placeholder = { Text("e.g. 614123456789") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        phoneErr?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                        // Show new password field
                        if (userPhoneInput.length == 11 && phoneErr!!.isEmpty()) {
                            Text(
                                "Please enter a new password below.",
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )

                            OutlinedTextField(
                                value = newPassword,
                                onValueChange = { loginVm.updateNewPassword(it) },
                                label = { Text("New Password") },
                                isError = !newPasswordError.isNullOrEmpty(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                leadingIcon = {Icon(Icons.Filled.Password, null)},
                                trailingIcon = {
                                    if (newPassword.isNotEmpty()) IconButton({ loginVm.clearNewPassword() }) {Icon(
                                        Icons.Filled.Clear, null)}
                                },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth()
                            )
                            newPasswordError?.let {
                                Text(
                                    it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            // Show confirm password field
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { loginVm.updateConfirmPassword(it) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                label = { Text("Confirm Password") },
                                leadingIcon = {Icon(Icons.Filled.Password, null)},
                                trailingIcon = {
                                    if (confirmPassword.isNotEmpty()) IconButton({ loginVm.clearConfirmPassword() }) {Icon(
                                        Icons.Filled.Clear, null)}
                                },
                                isError = !confirmPasswordError.isNullOrEmpty(),
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth()
                            )
                            confirmPasswordError?.let {
                                Text(
                                    it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    } else {
                        // Show existing password field
                        OutlinedTextField(
                            value = existingPassword,
                            onValueChange = { loginVm.updateExistingPassword(it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            label = { Text("Enter Password") },
                            leadingIcon = {Icon(Icons.Filled.Password, null)},
                            trailingIcon = {
                                if (existingPassword.isNotEmpty()) IconButton({ loginVm.clearExistingPassword();  }) {Icon(
                                    Icons.Filled.Clear, null)}
                            },
                            isError = !existingPasswordError.isNullOrEmpty(),
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = selectedUser != null
                        )

                        if (existingPasswordError != null) {
                            Text(
                                existingPasswordError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    if (isLoading) CircularProgressIndicator()
                    else {
                        Button(
                            onClick = {
                                scope.launch {
                                    // Login flows - all input validation already completed
                                    // Check if user has completed food intake questionnaire
                                    val hasForm = loginVm.hasForm(selectedUserId.toInt())
                                    // If first time create new user and store in db navigate to Food Intake Questionnaire
                                    if (isFirstTime) {
                                        loginVm.continueNewUser(selectedUserId.toInt())
                                        // Navigate onwards to FoodIntakeQuestionnaire
                                        val intent =
                                            Intent(context, FoodIntakeQuestionnaire::class.java)
                                        intent.putExtra("userID", selectedUserId)
                                        context.startActivity(intent)
                                        // Clear selected details for fresh login flows
                                        loginVm.hideLoginOverlay(); userVm.clearSelectedUser()

                                    }
                                    // else navigate existing user to home screen (check input validation completed prior
                                    else {
                                        // Check password
                                        val verifiedUser = loginVm.verifyExistingUser(selectedUserId.toInt())
                                        if (verifiedUser) {
                                            if (hasForm) {
                                                // Navigate onwards to HomeScreen
                                                val intent = Intent(context, HomeScreen::class.java)
                                                intent.putExtra("userID", selectedUserId)
                                                context.startActivity(intent)
                                                // Finish Activity to prevent
                                                context.finish()
                                            } else {
                                                // Navigate onwards to Food Intake Questionnaire
                                                val intent = Intent(
                                                    context,
                                                    FoodIntakeQuestionnaire::class.java
                                                )
                                                intent.putExtra("userID", selectedUserId)
                                                context.startActivity(intent)
                                            }
                                            // Clear selected details for fresh login flows
                                            loginVm.hideLoginOverlay(); userVm.clearSelectedUser()

                                            // Make toast
                                            Toast.makeText(context, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show()

                                            // Include start session
                                            SessionManager.startSession(context, selectedUserId.toInt())
                                        }

                                    }
                                }

                            },
                            enabled = enableContinueButton
                        ) {
                            Text("Continue")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Logo() {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "NutriTrack Logo",
            modifier = Modifier.size(80.dp)
        )
        Text(
            "NutriTrack",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.offset(x = (-20).dp, y = 3.dp)
        )
    }
}

@Composable
fun Disclaimer() {
    Text(
        "NutriTrack helps you understand your food quality score based on data collected by your healthcare professional. This app is not for tracking calories or diagnosing conditions—it’s designed to support informed, healthy food choices after a clinical dietary assessment.",
        style = MaterialTheme.typography.bodySmall.copy(
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            lineHeight = 18.sp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun Nametag() {
    Text(
        text = "Designed by Akhil Boda (34396268)",
        fontSize = 12.sp,
        fontStyle = FontStyle.Italic
    )
}
