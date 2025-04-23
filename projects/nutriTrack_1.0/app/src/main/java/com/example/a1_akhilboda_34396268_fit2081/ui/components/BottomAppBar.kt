import android.content.Intent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Insights
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.a1_akhilboda_34396268_fit2081.HomeScreen
import com.example.a1_akhilboda_34396268_fit2081.Insights

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
                // Avoid stacking intents with each call
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
                // Avoid stacking intents with each call
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }
        )

    }
}
