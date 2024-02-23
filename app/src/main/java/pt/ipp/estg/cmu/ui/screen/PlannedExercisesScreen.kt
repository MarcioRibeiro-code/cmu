package pt.ipp.estg.cmu.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.ipp.estg.cmu.R
import pt.ipp.estg.cmu.datastore.Exercise
import pt.ipp.estg.cmu.fireStore.BookmarksAggregation
import pt.ipp.estg.cmu.fireStore.RatingsAgreggation
import pt.ipp.estg.cmu.viewmodel.BookmarksViewModel
import pt.ipp.estg.cmu.viewmodel.ExercisesViewModel
import pt.ipp.estg.cmu.viewmodel.UserViewModel

@Composable
fun PlannedExercisesScreen(
    exercisesViewModel: ExercisesViewModel = viewModel(),
) {
    val doneExercises by exercisesViewModel.getUndoneExercises.observeAsState(listOf())

    LazyColumn {
        if (doneExercises.isNotEmpty()) {
            items(doneExercises.subList(0, 1)) { groupedExercise ->
                GroupedExerciseItem(groupedExercise = groupedExercise)
            }
        } else {
            item {
                EmptyStateMessage()
            }
        }
    }
}

@Composable
fun EmptyStateMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically)
    ) {
        FilledTonalIconButton(onClick = { }) {
            Icon(Icons.Outlined.Add, contentDescription = null)
        }
        Text(
            text = "There is no Planned Exercises Yet!",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Start by plan a exercise.",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun GroupedExerciseItem(
    groupedExercise: Exercise,
    exerciseViewModel: ExercisesViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
) {
    var showDialog by remember { mutableStateOf(false) }
    var showPlacesDialog by remember { mutableStateOf(false) }


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
                    text = groupedExercise.date.toString(), // You might want to format the date
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            groupedExercise.isDone = true
                            exerciseViewModel.updateExercise(groupedExercise)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Done",
                        )
                    }

                    if (groupedExercise.place_id != "") {
                        IconButton(onClick = {
                            showDialog = true
                        }) {
                            Icon(imageVector = Icons.Default.RateReview, contentDescription = null)
                        }
                    } else {
                        IconButton(onClick = { showPlacesDialog = true }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                    }


                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            ExerciseItem(exercise = groupedExercise)
        }
    }

    if (showDialog) {
        CustomDialog(
            onRatingChanged = {
                val ratingsService = RatingsAgreggation(Firebase.firestore)
                ratingsService.addRating(groupedExercise.place_id.toInt(), it)
            },
            onDismiss = {
                showDialog = false
                // Reset other dialog-related states if needed
            }
        )
    }

    if (showPlacesDialog) {
        PlacesDialog(onBookmarkSelected = {
            exerciseViewModel.updateExercise(groupedExercise.copy(place_id = it))
        },
            onDismiss = {
                showPlacesDialog = false
            })
    }
}

@Composable
fun PlacesDialog(
    bookmarksViewModel: BookmarksViewModel = viewModel(),
    onBookmarkSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    //Get all bookmarks
    val bookmarks by bookmarksViewModel.bookmarks.collectAsState(emptyList())

    //State to hold the selected bookmark
    var selectedBookmark by remember {
        mutableStateOf<BookmarksAggregation.BookmarkSegment?>(null)
    }

    if (bookmarks.isNullOrEmpty()) {
        onDismiss()
        Toast.makeText(LocalContext.current, "Add bookmarks", Toast.LENGTH_SHORT).show()
        return
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Select a Bookmarked Place!",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            item { //Dropdown menu for bookmarks
                DropdownMenu(expanded = true,
                    onDismissRequest = { }
                ) {
                    bookmarks?.forEach { bookmark ->
                        DropdownMenuItem(
                            text = { bookmark.segmentName },
                            onClick = { selectedBookmark = bookmark })
                    }
                }
            }

        }
        //Submit Button
        Button(onClick = {
            selectedBookmark?.let {
                onBookmarkSelected(it.segmentId)
            }
        }) {
            Text("Submit")
        }
    }
}


@Composable
fun CustomDialog(
    onDismiss: () -> Unit,
    onRatingChanged: (newRating: Double) -> Unit
) {
    var newRating by remember { mutableDoubleStateOf(0.0) }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Rating")
            RatingBar(
                rating = newRating,
                onRatingChanged = {
                    newRating = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                // Buttons
                Button(
                    onClick = {
                        onRatingChanged(newRating)
                        //onWeightEntered(newWeight.toFloat())
                        onDismiss()
                    },
                ) {
                    Text("Submit")
                }
            }
        }
    }

}

@Composable
fun WeightInput(weight: String, onWeightChanged: (String) -> Unit, modifier: Modifier) {
    OutlinedTextField(
        value = weight,
        onValueChange = { onWeightChanged(it) },
        label = {
            Icon(
                imageVector = Icons.Default.MonitorWeight,
                contentDescription = "Weight"
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    onRatingChanged: (newRating: Double) -> Unit
) {
    var ratingState by remember { mutableDoubleStateOf(rating) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_round_star_24),
                contentDescription = "starRating",
                modifier = modifier
                    .width(48.dp)
                    .height(48.dp)
                    .clickable {
                        ratingState = i.toDouble()
                        onRatingChanged(ratingState)
                    },
                tint = if (i <= ratingState) Color.Yellow else Color.Gray
            )
        }
    }
}


@Composable
fun ExerciseItem(exercise: Exercise) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            exercise.name.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "${exercise.difficultyLevel}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Timer, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${exercise.restTimeSeconds} sec")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Repeat, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${exercise.repetitions} reps")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.AssistWalker, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${exercise.sets} sets")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlannedExerciseScreen() {
    PlannedExercisesScreen()
}