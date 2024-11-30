package com.example.activity_signup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.activity_signup.ui.theme.SignUpActivityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignUpActivityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SignupScreen(
                        modifier = Modifier.padding(innerPadding)
                    ) { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
fun SignupScreen(modifier: Modifier = Modifier, onSignupComplete: (String) -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Ονοματεπώνυμο") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Όνομα Χρήστη") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Κωδικός") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Επιβεβαίωση Κωδικού") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    onSignupComplete("Παρακαλώ συμπληρώστε όλα τα πεδία")
                } else if (password != confirmPassword) {
                    onSignupComplete("Οι κωδικοί δεν ταιριάζουν")
                } else {
                    onSignupComplete("Εγγραφή επιτυχής!")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Εγγραφή")
        }
        Spacer(modifier = Modifier.height(8.dp)) // Κενό ανάμεσα στα κουμπιά
        Button(
            onClick = {
                onSignupComplete("Μεταφορά στην είσοδο")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Έχετε ήδη λογαριασμό; Συνδεθείτε")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignUpActivityTheme {
        SignupScreen { }
    }
}
