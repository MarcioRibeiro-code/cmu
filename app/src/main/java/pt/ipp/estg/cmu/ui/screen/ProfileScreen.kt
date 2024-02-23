package pt.ipp.estg.cmu.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu.datastore.UserModel
import pt.ipp.estg.cmu.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    viewModel: UserViewModel = viewModel()
) {
    val user by viewModel.user.observeAsState()
    var showCreate by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(user) {
        // Este bloco será executado sempre que o user for atualizado
        println("User updated in Composable: $user")
    }

    if (user == null) {
        //if the user is null, display a button to create a user profile

        Button(
            onClick = {
                showCreate = true
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Create User Profile")
        }

        if (showCreate) {
            CreateProfile(viewModel)
        }

    } else {
        LazyColumn(content = {
            item {
                user?.let { EditProfileInfo(it, viewModel) }
            }
        })

        /*
                //Display Grouped Exercises
                val groupedExercises by viewModel.groupedExercises.observeAsState()

                if (groupedExercises != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text("Completed Exercises", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        groupedExercises?.forEach { groupedExercise ->
                            Text(
                                groupedExercise.date.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            groupedExercise.exercises.forEach { exercise ->
                                Text(exercise.name, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }*/

    }
}

@Composable
fun CreateProfile(
    viewModel: UserViewModel
) {
    var newName by remember { mutableStateOf("") }
    var newWeight by remember { mutableStateOf("") }
    var newHeight by remember { mutableStateOf("") }
    var newAge by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Create Your Profile",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProfileInput(label = "Name", value = newName, onValueChange = { newName = it })

            Spacer(modifier = Modifier.height(8.dp))

            ProfileInput(
                label = "Weight (kg)",
                value = newWeight,
                onValueChange = { newWeight = it },
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileInput(
                label = "Height (cm)",
                value = newHeight,
                onValueChange = { newHeight = it },
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileInput(
                label = "Age",
                value = newAge,
                onValueChange = { newAge = it },
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.addUser(
                        UserModel(
                            name = newName,
                            weight = newWeight.toFloatOrNull() ?: 0f,
                            height = newHeight.toFloatOrNull() ?: 0f,
                            age = newAge.toIntOrNull() ?: 0
                        )
                    )
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Profile")
            }
        }
    }
}

@Composable
fun ProfileInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Text(
            label, style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}


@Composable
fun EditProfileInfo(userModel: UserModel, viewModel: UserViewModel) {
    var isEditingName by remember { mutableStateOf(false) }
    var isEditingWeight by remember { mutableStateOf(false) }
    var isEditingHeight by remember { mutableStateOf(false) }
    var isEditingAge by remember { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {
            Text(
                "Profile Information",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )
            // Profile Information
            Spacer(modifier = Modifier.height(16.dp))

            ProfileInfoItem(
                "Name",
                userModel.name,
                onEditConfirmed = { confirmedName ->
                    /*println("Confirmed name: $confirmedName")*/
                    // Save the confirmed name to the viewModel or perform any other action
                    viewModel.updateUser(userModel.copy(name = confirmedName))
                    isEditingName = false
                },
                isEditingName,
                onEditClick = { isEditingName = !isEditingName },
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInfoItem(
                "Weight",
                userModel.weight.toString(),
                onEditConfirmed = { confirmedWeight ->
                    // Save the edited weight to the viewModel
                    viewModel.updateUser(userModel.copy(weight = confirmedWeight.toFloat()))
                    isEditingWeight = false
                },
                isEditingWeight,
                onEditClick = { isEditingWeight = !isEditingWeight },
            )
            //Spacer(modifier = Modifier.height(8.dp))

            ProfileInfoItem(
                "Height",
                userModel.height.toString(),
                onEditConfirmed = { confirmedHeight ->
                    // Save the edited height to the viewModel
                    viewModel.updateUser(userModel.copy(height = confirmedHeight.toFloat()))
                    isEditingHeight = false
                },
                isEditingHeight,
                onEditClick = { isEditingHeight = !isEditingHeight },
            )

            //Spacer(modifier = Modifier.height(8.dp))

            ProfileInfoItem(
                "Age",
                userModel.age.toString(),
                onEditConfirmed = { confirmedAge ->
                    // Save the edited height to the viewModel
                    viewModel.updateUser(userModel.copy(age = confirmedAge.toInt()))
                    isEditingAge = false
                },
                isEditingAge,
                onEditClick = { isEditingAge = !isEditingAge },
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoItem(
    title: String,
    value: String,
    onEditConfirmed: (String) -> Unit,
    isEditing: Boolean = false,
    onEditClick: () -> Unit = {}
) {
    var editedValue by remember { mutableStateOf(value) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            )
            if (isEditing) {
                TextField(
                    value = editedValue,
                    onValueChange = {
                        editedValue = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .width(100.dp),
                )
            } else {
                Text(
                    value,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        if (!isEditing) {
            IconButton(onClick = {
                onEditClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Edit, contentDescription = "Edit"
                )
            }
        } else {
            // Display a check icon when editing to confirm changes
            IconButton(onClick = {
                // Confirm changes and trigger the callback
                onEditConfirmed(editedValue)
            }) {
                Icon(
                    imageVector = Icons.Default.Done, contentDescription = "Done"
                )
            }
        }
    }
}


@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ProfileScreenPreview()
}




