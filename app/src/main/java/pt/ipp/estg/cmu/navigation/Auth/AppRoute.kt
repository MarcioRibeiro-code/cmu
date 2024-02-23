package pt.ipp.estg.cmu.navigation.Auth

import androidx.compose.ui.graphics.vector.ImageVector
enum class AppRoute(
    val route: String,
    val navigationLabel: String,
    val topBarLabel: String?,
) {
    WelcomeScreen("welcome_screen", "Welcome_Screen", "Authentication"),
    SignUpScreen("signup_screen",  "Signup_Screen", "Sign In"),
    SignInScreen("signin_screen", "SignIn_Screen", null),
}

