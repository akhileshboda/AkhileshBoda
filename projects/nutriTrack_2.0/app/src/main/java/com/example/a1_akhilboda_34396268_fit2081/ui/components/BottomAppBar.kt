import android.content.Intent
import android.graphics.drawable.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sports
import androidx.compose.ui.platform.LocalContext
import com.example.a1_akhilboda_34396268_fit2081.view.HomeScreen
import com.example.a1_akhilboda_34396268_fit2081.view.Insights
import com.example.a1_akhilboda_34396268_fit2081.view.NutriCoach
import com.example.a1_akhilboda_34396268_fit2081.view.Settings

@Composable
fun BottomBar(selectedScreen: String, userID: String) {
    // create local context
    val context = LocalContext.current

    // assign

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 5.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedScreen == "home",
            onClick = {
                val intent = Intent(context, HomeScreen::class.java)
                intent.putExtra("userID", userID) // so each screen can load sp according to userID
                context.startActivity(intent)
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Insights, contentDescription = "Insights") },
            label = { Text("Insights") },
            selected = selectedScreen == "insights",
            onClick = {
                val intent = Intent(context, Insights::class.java)
                intent.putExtra("userID", userID) // so each screen can load sp according to userID
                context.startActivity(intent)
            }
        )
        NavigationBarItem(
            icon = {Icon(Icons.Filled.Settings, contentDescription = "Settings")},
            label = {Text("Settings")},
            selected = selectedScreen == "settings",
            onClick = {
                val intent = Intent(context, Settings::class.java)
                intent.putExtra("userID", userID) // so each screen can load sp according to userID
                context.startActivity(intent)
            }
        )
        NavigationBarItem(
            icon = {Icon(Icons.Filled.Sports, contentDescription = "NutriCoach")},
            label = {Text("NutriCoach")},
            selected = selectedScreen == "nutricoach",
            onClick = {
                val intent = Intent(context, NutriCoach::class.java)
                intent.putExtra("userID", userID) // so each screen can load sp according to userID
                context.startActivity(intent)
            }
        )

    }
}
