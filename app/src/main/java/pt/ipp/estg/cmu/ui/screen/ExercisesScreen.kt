package pt.ipp.estg.cmu.ui.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.cmu.fireStore.ExerciseAggregation
import pt.ipp.estg.cmu.fireStore.ExerciseAggregation.DifficultyLevel
import pt.ipp.estg.cmu.fireStore.ExerciseAggregation.Exercise
import pt.ipp.estg.cmu.fireStore.ExerciseAggregation.ExerciseDifficulty
import pt.ipp.estg.cmu.navigation.WorkoutFlow.AppRoute
import pt.ipp.estg.cmu.viewmodel.ExercisesViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ExercisesScreen(
    navController: NavController,
    exerciseId: String? = null,
    viewModel: ExercisesViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    exercisesViewModel: ExercisesViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    var snackbarVisible by remember { mutableStateOf(false) }

    var exerciseDetails by remember { mutableStateOf<Exercise?>(null) }
    var showDateDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var showDifficultyDropdown by remember { mutableStateOf(false) }
    var selectedDifficultyLevel by remember { mutableStateOf(DifficultyLevel.BEGINNER) }


    if (exerciseId != null) {
        viewModel.getExerciseById(exerciseId)
    }

    exerciseDetails = viewModel.exercise.value

    exerciseDetails?.let { exercise ->
        Column {
            ExerciseDetailsCard(exercise)

            Spacer(modifier = Modifier.height(16.dp))

            CustomExerciseDetails(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                selectedTime = selectedTime,
                onTimeSelected = { selectedTime = it },
                selectedLevel = selectedDifficultyLevel,
                onLevelSelected = { selectedDifficultyLevel = it },
                showDateDialog = showDateDialog,
                onShowDateDialogChange = { showDateDialog = it },
                showTimeDialog = showTimeDialog,
                onShowTimeDialogChange = { showTimeDialog = it },
                showDifficultyDropdown = showDifficultyDropdown,
                onShowDifficultyDropdownChange = { showDifficultyDropdown = it },
                exerciseDifficulty = getExerciseDifficulty(exercise, selectedDifficultyLevel),
            )
            Button(onClick = {
                val difficulty = getExerciseDifficulty(exercise, selectedDifficultyLevel)
                viewModel.addExercise(
                    pt.ipp.estg.cmu.datastore.Exercise(
                        exercise.exerciseId,
                        "",
                        exercise.name.toString(),
                        exercise.bodyDescription ?: "",
                        exercise.exerciseType ?: ExerciseAggregation.ExerciseType.BODYWEIGHT,
                        difficulty.difficultyLevel ?: DifficultyLevel.BEGINNER,
                        difficulty.repetitions ?: 0,
                        difficulty.sets ?: 0,
                        difficulty.restTimeSeconds ?: 0,
                        false,
                        selectedDate
                    )
                )
                // Set snackbarVisible to true to show the snackbar
                snackbarVisible = true
            }

            ) {
                Text(text = "ADD EXERCISE")
            }

            // Display the snackbar when snackbarVisible is true
            if (snackbarVisible) {
                // Show toast
                Toast.makeText(
                    LocalContext.current,
                    "Exercise Added",
                    Toast.LENGTH_SHORT
                ).show()
                snackbarVisible = false
            }
        }
    } ?: run {
        // Se o exercício não for encontrado
        Snackbar(modifier = Modifier.padding(16.dp), content = {
            Text("Exercício não encontrado")
        }, action = {
            // Adicione um botão de ação
            TextButton(onClick = {
                navController.navigate(AppRoute.HomeScreen.route)
            }) {
                Text("OK")
            }
        })
    }
}

fun getExerciseDifficulty(
    exercise: Exercise, difficultyLevel: DifficultyLevel
): ExerciseDifficulty {
    // Find the ExerciseDifficulty corresponding to the selected difficulty level
    return exercise.difficulty?.firstOrNull { it.difficultyLevel == difficultyLevel }
        ?: ExerciseDifficulty(
            difficultyLevel = DifficultyLevel.BEGINNER,
            repetitions = 0,
            sets = 0,
            restTimeSeconds = 0
        )
}


@Composable
fun ExerciseDetailsCard(exerciseDetails: Exercise) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            exerciseDetails.name?.let {
                Text(
                    text = it, style = MaterialTheme.typography.headlineMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            exerciseDetails.bodyDescription?.let {
                Text(
                    text = it, style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun DifficultyLevelDropdown(
    selectedLevel: DifficultyLevel,
    onLevelSelected: (DifficultyLevel) -> Unit,
    showDifficultyDropdown: Boolean,
    onShowDifficultyDropdownChange: (Boolean) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onShowDifficultyDropdownChange(!showDifficultyDropdown)
        }) {
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Difficulty Level: ${selectedLevel.name}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Icon(
                Icons.Filled.ArrowDropDown,
                contentDescription = "Expand dropdown",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        if (showDifficultyDropdown) {
            DropdownMenu(
                expanded = showDifficultyDropdown,
                onDismissRequest = { onShowDifficultyDropdownChange(false) },
                modifier = Modifier.fillMaxWidth()
            ) {
                DifficultyLevel.values().forEach { difficultyLevel ->
                    DropdownMenuItem(onClick = {
                        onLevelSelected(difficultyLevel)
                        onShowDifficultyDropdownChange(false)
                    }, text = { Text(text = difficultyLevel.name) })
                }
            }
        }
    }
}

@Composable
fun CustomExerciseDetails(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    selectedTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    selectedLevel: DifficultyLevel,
    onLevelSelected: (DifficultyLevel) -> Unit,
    showDateDialog: Boolean,
    onShowDateDialogChange: (Boolean) -> Unit,
    showTimeDialog: Boolean,
    onShowTimeDialogChange: (Boolean) -> Unit,
    showDifficultyDropdown: Boolean,
    onShowDifficultyDropdownChange: (Boolean) -> Unit,
    exerciseDifficulty: ExerciseDifficulty,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        DateDialog(selectedDate = selectedDate,
            onDateSelected = { onDateSelected(it) },
            showDialog = showDateDialog,
            onShowDialogChange = { onShowDateDialogChange(it) })

        Spacer(modifier = Modifier.height(16.dp))

        TimePicker(selectedTime = selectedTime,
            onTimeSelected = { onTimeSelected(it) },
            showDialog = showTimeDialog,
            onShowDialogChange = { onShowTimeDialogChange(it) })

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown for selecting difficulty level
        DifficultyLevelDropdown(
            selectedLevel = selectedLevel,
            onLevelSelected = onLevelSelected,
            showDifficultyDropdown = showDifficultyDropdown,
            onShowDifficultyDropdownChange = onShowDifficultyDropdownChange
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Display content based on selected difficulty level
        ExerciseDetails(exerciseDifficulty)

        Spacer(modifier = Modifier.height(16.dp))

        // Add components for other details or actions if needed


        Spacer(
            modifier = Modifier.height(16.dp)
        )


    }
}


@Composable
fun ExerciseDetails(exerciseDifficulty: ExerciseDifficulty) {
    // Display details specific to the selected difficulty level
    Text("Repetitions: ${exerciseDifficulty.repetitions}")
    Text("Sets: ${exerciseDifficulty.sets}")
    Text("Rest Time: ${exerciseDifficulty.restTimeSeconds} seconds")
}

@Composable
fun DateDialog(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .clickable {
            // Only open the dialog if it's not already open
            if (!showDialog) {
                onShowDialogChange(true)
            }
        }) {
        Text(
            text = "Date Selected: ${selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (showDialog) {
            val context = LocalContext.current

            // Use o DatePickerDialog diretamente aqui
            DatePickerDialog(
                context, { _, year, monthOfYear, dayOfMonth ->
                    val newDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                    onDateSelected(newDate)
                    onShowDialogChange(false)
                }, selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth
            ).apply {
                //handle cancel button click
                setButton(
                    DatePickerDialog.BUTTON_NEGATIVE, "Cancel"
                ) { _, _ -> onShowDialogChange(false) }
                show()
            }
        }
    }
}

@Composable
fun TimePicker(
    selectedTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .clickable {
            // Only open the dialog if it's not already open
            if (!showDialog) {
                onShowDialogChange(true)
            }
        }) {
        Text(
            text = "Hora Selecionada: ${selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (showDialog) {
            val context = LocalContext.current

            // Use o TimePickerDialog diretamente aqui
            TimePickerDialog(
                context, { _, hourOfDay, minute ->
                    val newTime = LocalTime.of(hourOfDay, minute)
                    onTimeSelected(newTime)
                    onShowDialogChange(false)
                }, selectedTime.hour, selectedTime.minute, true
            ).apply {
                //handle cancel button click
                setButton(
                    TimePickerDialog.BUTTON_NEGATIVE, "Cancel"
                ) { _, _ -> onShowDialogChange(false) }
                show()
            }
        }
    }
}

@Preview
@Composable
fun ExercisesScreenPreview() {
    ExercisesScreen(
        navController = rememberNavController(), exerciseId = "1"
    )
}