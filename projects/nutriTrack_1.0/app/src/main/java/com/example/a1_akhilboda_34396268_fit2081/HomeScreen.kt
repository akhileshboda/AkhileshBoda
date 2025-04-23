package com.example.a1_akhilboda_34396268_fit2081

import BottomBar
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.fonts.Font
import android.os.Bundle
import android.util.Log
import androidx.compose.material3.Button
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.A1_AkhilBoda_34396268_FIT2081Theme
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.frameBoundaryPadding
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.heading1
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.heading2
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.individualElementPadding
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.paragraph
import com.example.a1_akhilboda_34396268_fit2081.ui.theme.paragraphLineHeight
import kotlin.coroutines.Continuation

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // get userID value from previous screen
            val userID = intent.getStringExtra("userID").toString() // Add call to bypass null safety
            // Get current context
            val context = LocalContext.current
            // Load shared preferences
            val sharedPreferences = context.getSharedPreferences("$userID.sp", Context.MODE_PRIVATE)
            // Load userID from shared preferences
            //val userID = sharedPreferences.getString("userID", "").toString()
            // Load user first name from shared preference
            val userFirstName = sharedPreferences.getString("FirstName", "").toString()


            A1_AkhilBoda_34396268_FIT2081Theme {
                Scaffold(
                    // Code bottom bar - call external file
                    bottomBar = {
                        BottomBar("home", userID)
                    }
                ){ innerPadding ->
                    Column(modifier = Modifier.fillMaxSize()
                        .padding(innerPadding).padding(frameBoundaryPadding)
                        .offset(x = 8.dp),
                    ) {
                        // Welcome message
                        Text(
                            buildAnnotatedString {
                                withStyle (style = SpanStyle(fontSize = 40.sp)) { append("Welcome ") }
                                withStyle (style = SpanStyle(fontSize = 40.sp, textDecoration = TextDecoration.Underline, color = Color.Gray)) { append(userFirstName, ",") }
                            }
                        )
                        Spacer(modifier = Modifier.height(individualElementPadding))

                        Row (modifier = Modifier.fillMaxWidth()) {
                            Text(
                                modifier = Modifier.weight(2f),
                                text = "You have already completed your Food Intake Questionnaire.",

                            )
                            Button(
                                modifier = Modifier.weight(1f).wrapContentWidth(),
                                onClick = {
                                    // Redirect to questionnaire while loading shared preferences
                                    val intent = Intent(context, FoodIntakeQuestionnaire::class.java)
                                    intent.putExtra("userID", userID)
                                    context.startActivity(intent)
                                }
                            ) {
                                Text("Edit")
                            }
                        }

                        // Add healthy eating image
                        Image(
                            painter = painterResource(id = R.drawable.homescreen_icon),
                            contentDescription = "HomeScreen Icon",
                            alignment = Alignment.Center
                        )

                        Column (
                            modifier = Modifier.offset(y = -(40).dp).fillMaxWidth()
                        ) {
                            Text(
                                text = "Your Food Quality Score:",
                                textAlign = TextAlign.Center,
                                fontSize = heading1,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                            //Display food quality score
                            // Retrieve food quality score
                            val foodQualityScore = getFoodQualityScore(userID)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                                //Text in card
                                Card(modifier = Modifier.wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color.DarkGray)) {
                                    Text(
                                        modifier = Modifier.padding(all = 12.dp),
                                        text = "$foodQualityScore/100",
                                        fontSize = 20.sp,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(individualElementPadding))

                            // Write disclaimer for what is food quality score
                            Row(modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically) {
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
        }
    }
}