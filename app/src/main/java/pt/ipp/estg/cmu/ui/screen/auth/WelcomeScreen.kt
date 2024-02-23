package pt.ipp.estg.cmu.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PrivateConnectivity
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipp.estg.cmu.R


@Composable
fun WelcomeScreen(
    onGoogleSignIn: () -> Unit,
    onAnonymousSignIn: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(300.dp),
            shape = RoundedCornerShape(40.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.fitness_welcome_img),
                contentDescription = "Welcome Image",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = "Welcome to Fitness App",
            style = MaterialTheme.typography.titleLarge,
            color = colorScheme.primary
        )

        Text(
            text = "Plan your workouts",
            color = colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium
        )


        //Criar a função para o botão (LoginButton?)
        FilledTonalIconButton(
            onClick = { onGoogleSignIn() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "Google Login Logo"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign in with Google", color = colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        //Criar a função para o botão (Anonymous)
        FilledTonalIconButton(
            onClick = { onAnonymousSignIn() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.PrivateConnectivity,
                    contentDescription = "Anonymous icon logo"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Go Anonymous", color = colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen({}, {})
}

