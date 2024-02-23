package pt.ipp.estg.cmu.ui.screen.auth

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipp.estg.cmu.components.PasswordToggleTextField


/*
* https://developer.android.com/jetpack/compose/text/user-input?hl=pt-br
* Ver o Ultimo exemplo
*  */



data class SignUp(
    var confirmPassword: String = "",
    var password: String = "",
    var email: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSubmit: (SignUp) -> Unit = {},
    onError: (String) -> Unit = {}
) {
    val coloScheme = MaterialTheme.colorScheme
    //Create a object signUp
    val signUp = remember { mutableStateOf(SignUp()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.headlineLarge,
            color = coloScheme.primary,
            modifier = Modifier.padding(16.dp)
        )

        TextField(
            value = signUp.value.email,
            onValueChange = { signUp.value.email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
        )

        PasswordToggleTextField(
            value = signUp.value.password,
            onValueChange = { signUp.value.password = it },
            label = { Text("Password") },
            keyboardType = KeyboardType.Password,
            modifier = Modifier
        )

        PasswordToggleTextField(
            value = signUp.value.confirmPassword,
            onValueChange = { signUp.value.confirmPassword = it },
            label = { Text("Confirm Password") },
            keyboardType = KeyboardType.Password,
            modifier = Modifier
        )

        Button(
            onClick = {
                if (isValidEmail(signUp.value.email) && isValidPassword(
                        signUp.value.password,
                        signUp.value.confirmPassword
                    )
                ) {
                    onSubmit(signUp.value)
                } else {
                    onError("Invalid email or password")
                }
            },
            modifier = Modifier
                .height(50.dp)
        ) {
            Text("SIGN UP")
        }
    }

}


// Validation functions
fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(password: String, confirmPassword: String): Boolean {
    return password.length >= 6 && password == confirmPassword
}


@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}
