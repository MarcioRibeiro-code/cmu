package pt.ipp.estg.cmu.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu.viewmodel.ExercisesViewModel
import pt.ipp.estg.cmu.datastore.Exercise as LocalExercise

@Composable
fun HistoryExercisesContent(exercisesViewModel: ExercisesViewModel = viewModel()) {
    // Observe LiveData from ViewModel
    val doneExercises by exercisesViewModel.getDoneExercises.observeAsState(listOf())

    LazyColumn {
        if (doneExercises.isNotEmpty()) {
            items(doneExercises) { doneExercise ->
                ExerciseItem(exercise = doneExercise, exerciseViewModel = exercisesViewModel)
            }
        } else {
            item {
                EmptyStateMessage()
            }
        }
    }
}


@Composable
fun ExerciseItem(exercise: LocalExercise, exerciseViewModel: ExercisesViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = exercise.date.toString(), // You might want to format the date
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))


            ExerciseItem(exercise = exercise)

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryExercisesScreenPreview() {
    val exercisesViewModel = viewModel<ExercisesViewModel>()
    HistoryExercisesContent(exercisesViewModel)
}
