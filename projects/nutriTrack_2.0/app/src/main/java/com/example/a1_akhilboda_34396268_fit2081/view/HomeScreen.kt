package com.example.a1_akhilboda_34396268_fit2081.view

import BottomBar
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.a1_akhilboda_34396268_fit2081.R
import com.example.a1_akhilboda_34396268_fit2081.model.AppDatabase
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.UserRepository
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.A1_AkhilBoda_34396268_FIT2081Theme
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.frameBoundaryPadding
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.heading1
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.individualElementPadding
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.paragraph
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.paragraphLineHeight
import com.example.a1_akhilboda_34396268_fit2081.viewmodel.UserViewModel



class HomeScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get dao
        val dao = AppDatabase.getDatabase(this).userDao()
        // Get repo
        val repo = UserRepository(dao)
        // Get viewmodel
        val userVm = ViewModelProvider(this, UserViewModel.Factory(repo))[UserViewModel::class.java]
        enableEdgeToEdge()

        val userId = intent.getStringExtra("userID")
        userVm.getUser(userId!!.toInt())

        setContent {
            A1_AkhilBoda_34396268_FIT2081Theme {
                HomeContent(userVm = userVm, userId = userId)
            }
        }
    }
}

@Composable
fun HomeContent(userVm: UserViewModel, userId: String) {
    val user       by userVm.selectedUser.collectAsState()
    val score      by userVm.foodQualityScore.collectAsState()
    val context    = LocalContext.current

    Scaffold(
        bottomBar = { BottomBar("home", userId.toString()) }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .offset(x = 12.dp)
                .verticalScroll(rememberScrollState())

        ) {
            val name = user!!.firstName
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(fontSize = 40.sp)) { append("Welcome ") }
                    withStyle(
                        SpanStyle(
                            fontSize = 40.sp,
                            textDecoration = TextDecoration.Underline,
                            color = Color.Gray
                        )
                    ) { append("$name,") }
                }
            )
            Spacer(Modifier.height(individualElementPadding))

            Row(Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(2f),
                    text = "You have already completed your Food Intake Questionnaire."
                )
                Button(
                    modifier = Modifier.weight(1f).wrapContentWidth(),
                    onClick = {
                        context.startActivity(
                            Intent(context, FoodIntakeQuestionnaire::class.java)
                                .putExtra("userID", userId.toString())
                        )
                    }
                ) { Text("Edit") }
            }

            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.homescreen_icon),
                contentDescription = "HomeScreen Icon",
                alignment = Alignment.Center
            )
            Column(modifier = Modifier.fillMaxWidth().offset(y= -(50).dp)) {
                Text(
                    "Your Food Quality Score:",
                    Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = heading1,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(individualElementPadding))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card(
                        Modifier.wrapContentSize(),
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                    ) {
                        Text(
                            "$score/100",
                            Modifier.padding(12.dp),
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(Modifier.height(individualElementPadding))

                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "The Food Quality Score offers a quick overview of how closely your eating habits follow established nutritional guidelines. It highlights both the positive aspects of your diet and areas where you can improve.\n" +
                                "\n" +
                                "This personalised score evaluates your intake across key food groups—such as vegetables, fruits, whole grains, and proteins—providing actionable insights to help you make healthier food choices.",
                        fontSize = paragraph,
                        lineHeight = paragraphLineHeight,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}