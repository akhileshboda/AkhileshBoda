package com.example.a1_akhilboda_34396268_fit2081.view

import BottomBar
import android.content.Context
import androidx.compose.material3.AlertDialog
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.a1_akhilboda_34396268_fit2081.SessionManager
import com.example.a1_akhilboda_34396268_fit2081.model.AppDatabase
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.UserRepository
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.heading1
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.individualElementPadding
import com.example.a1_akhilboda_34396268_fit2081.view.ui.theme.A3Theme
import com.example.a1_akhilboda_34396268_fit2081.viewmodel.UserViewModel
import com.google.ai.client.generativeai.type.content


class Settings : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Get context
        val context = this
        // Get user id from intent
        val userID = intent.getStringExtra("userID")!!
        // Instantiate user vm via dao and repo
        val dao = AppDatabase.getDatabase(this).userDao()
        val repo = UserRepository(dao)
        val vm = ViewModelProvider(this, UserViewModel.Factory(repo))[UserViewModel::class.java]

        // Set user in viewmodel
        vm.getUser(userID.toInt())

        setContent {
            // Collect from vm
            val user = vm.selectedUser.collectAsState()
            val confirmLogout by vm.confirmLogout.collectAsState()

            A3Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomBar("settings", userID) }

                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(8.dp)
                            .verticalScroll(rememberScrollState(0))
                    ) {
                        Text(
                            text = "Settings",
                            fontSize = 40.sp
                        )
                        Spacer(modifier = Modifier.padding(individualElementPadding))
                        ShowAccount(
                            firstName = user.value!!.firstName,
                            lastName = user.value!!.lastName,
                            phoneNumber = user.value!!.phoneNumber,
                            userID = userID
                        )
                        ShowOtherSettings(context = context, vm = vm)
                    }
                }
                if (confirmLogout) {
                    AlertDialog(
                        icon = {Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "")},
                        onDismissRequest = { vm.hideLogoutDialog() },
                        title           = { Text("Logout") },
                        text            = { Text("Are you sure you want to logout?", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                        confirmButton   = {
                            Button(onClick = {
                                // Run intent
                                // Intent back to MainActivity and finish activity to prevent return
                                val intent = Intent(context, MainActivity::class.java)
                                intent.removeExtra("userID")
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent)
                                SessionManager.endSession(context)
                            }) {
                                Text("Confirm")
                            }
                        },
                        dismissButton   = {
                            Button(
                                onClick = { vm.hideLogoutDialog() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ShowAccount(firstName: String, lastName: String, phoneNumber: String, userID: String,) {
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
                .padding(12.dp)
        ) {
            Text(
                text = "ACCOUNT",
                modifier = Modifier.fillMaxWidth(),
                color = Color.DarkGray,
                fontSize = heading1,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Icon(Icons.Filled.Person, contentDescription = "")
                Spacer(modifier = Modifier.width(10.dp))
                Text("$firstName $lastName")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Icon(Icons.Filled.Phone, contentDescription = "")
                Spacer(modifier = Modifier.width(10.dp))
                Text(phoneNumber)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Icon(Icons.Filled.Badge, contentDescription = "")
                Spacer(modifier = Modifier.width(10.dp))
                Text(userID)
            }
        }
    }
}

@Composable
fun ShowOtherSettings(context: Context, vm: UserViewModel) {
    val showClinicianLoginDialog by vm.showClinicianLoginDialog.collectAsState()
    val passkey by vm.passkey.collectAsState()
    val validPasskey by vm.validPasskey.collectAsState()
    val passkeyErrorMsg by vm.passkeyErrorMsg.collectAsState()

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
            modifier = Modifier.fillMaxWidth().padding(12.dp),
        ) {
            Text(
                text = "OTHER SETTINGS",
                modifier = Modifier.fillMaxWidth(),
                color = Color.DarkGray,
                fontSize = heading1,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                colors = ButtonColors(
                    containerColor = Color.Red, contentColor = Color.White,
                    disabledContainerColor = Color.Red,
                    disabledContentColor = Color.White
                ),
                onClick = {
                    // Show confirmation dialog
                    vm.showLogoutDialog()
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "", modifier = Modifier.offset(x = -8.dp))
                Text("Logout")
            }

            Button(
                colors = ButtonColors(
                    containerColor = Color.Green, contentColor = Color.Black,
                    disabledContainerColor = Color.Green,
                    disabledContentColor = Color.White
                ),
                onClick = {
                    vm.showClinicianLoginDialog()
                }
            ) {
                Icon(Icons.Filled.MedicalServices, contentDescription = "", modifier = Modifier.offset(x = -8.dp))
                Text("Clinician Login")
            }
        }
        if (showClinicianLoginDialog) {
            val focusRequester = remember { FocusRequester() }

            AlertDialog(
                icon = {Icon(Icons.Filled.MedicalServices, contentDescription = "")},
                onDismissRequest = { vm.hideClinicianLoginDialog() },
                title           = { Text("Clinician Login") },
                text            = {
                    // Bring focus to Text Field
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                    Column {
                        // Enter passkey
                        OutlinedTextField(
                            value = passkey,
                            onValueChange = { vm.updatePasskey(it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = PasswordVisualTransformation(),
                            label = { Text("Enter Passkey") },
                            modifier = Modifier.focusRequester(focusRequester),
                            trailingIcon = {
                                if (passkey.isNotEmpty()) {
                                    IconButton(onClick = { vm.clearPasskey() }) {
                                        Icon(Icons.Filled.Clear, contentDescription = "")
                                    }
                                }
                            }
                        )
                        if (passkeyErrorMsg.isNotEmpty()) {
                            Text(
                                passkeyErrorMsg,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            vm.checkPasskey()
                        },
                        enabled = passkey.isNotEmpty()
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton   = {
                    Button(
                        onClick = { vm.hideClinicianLoginDialog() }
                    ) {
                        Text("Close")
                    }
                }
            )
        }
    }
    // Intent to Clinician Screen on entering valid passkey
    if (validPasskey) {
        val intent = Intent(context, ClinicianScreen::class.java)
        context.startActivity(intent)
        vm.hideClinicianLoginDialog()
    }
}