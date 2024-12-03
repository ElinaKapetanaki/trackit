package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                EditProfileScreen { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
fun EditProfileScreen(onSave: (String) -> Unit) {
    var fullName by remember { mutableStateOf("John Doe") }
    var username by remember { mutableStateOf("john_doe") }
    var password by remember { mutableStateOf("password123") }
    var profileImageId by remember { mutableStateOf<Int?>(null) } // Αρχικά καμία εικόνα
    var fixedIncome by remember { mutableStateOf("1000") } // Σταθερά Έσοδα
    var fixedExpenses by remember { mutableStateOf("500") } // Σταθερά Έξοδα

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Εικόνα Προφίλ ή Placeholder για Προσθήκη
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable {
                    // Προσθήκη εικόνας αν δεν υπάρχει
                    profileImageId = R.drawable.profile_placeholder
                },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageId != null) {
                Image(
                    painter = painterResource(id = profileImageId!!),
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Προσθήκη Εικόνας", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Πεδίο για το Ονοματεπώνυμο
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Ονοματεπώνυμο") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Πεδίο για το Username
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Πεδίο για τον Κωδικό
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Νέος Κωδικός") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Πεδίο για Σταθερά Έσοδα
        OutlinedTextField(
            value = fixedIncome,
            onValueChange = { fixedIncome = it },
            label = { Text("Σταθερά Έσοδα (€)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Πεδίο για Σταθερά Έξοδα
        OutlinedTextField(
            value = fixedExpenses,
            onValueChange = { fixedExpenses = it },
            label = { Text("Σταθερά Έξοδα (€)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Κουμπί για αποθήκευση αλλαγών
        Button(
            onClick = {
                onSave("Οι αλλαγές αποθηκεύτηκαν! Έσοδα: €$fixedIncome, Έξοδα: €$fixedExpenses")
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Αποθήκευση Αλλαγών")
        }
    }
}
