package pt.ipp.estg.cmu.ui.screen.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipp.estg.cmu.components.PasswordToggleTextField

data class SignIn(
    var password: String = "",
    var email: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    onSubmit: (SignIn) -> Unit = {},
    onError: (String) -> Unit = {}
) {
    val coloScheme = MaterialTheme.colorScheme


    // Create Compose state for email and password
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign In",
            style = MaterialTheme.typography.headlineLarge,
            color = coloScheme.primary,
            modifier = Modifier.padding(16.dp)
        )

        TextField(
            value = email,
            onValueChange = {
                email = it
                Log.d("EMAIL", email)
            },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
        )

        PasswordToggleTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardType = KeyboardType.Password,
            modifier = Modifier
        )

        Button(
            onClick = {
                try {
                    onSubmit(SignIn(email, password))
                } catch (e: Exception) {
                    onError(e.message ?: "An unexpected error occurred")
                }
            },
            modifier = Modifier
                .height(50.dp)
        ) {
            Text("SIGN IN")
        }
    }
}


@Preview
@Composable
fun SignInScreenPreview() {
    SignInScreen()
}
