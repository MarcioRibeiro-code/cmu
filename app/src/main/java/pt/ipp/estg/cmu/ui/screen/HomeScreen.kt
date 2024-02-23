package pt.ipp.estg.cmu.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.cmu.R
import pt.ipp.estg.cmu.fireStore.ExerciseAggregation.*
import pt.ipp.estg.cmu.navigation.WorkoutFlow.AppRoute
import pt.ipp.estg.cmu.ui.theme.Gray300
import pt.ipp.estg.cmu.viewmodel.ExercisesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: ExercisesViewModel = viewModel()) {
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadExerciseList()
    }

    val exerciseList by viewModel.exerciseList.observeAsState(emptyList())
    val stretchingExerciseList by viewModel.stretchingExerciseList.observeAsState(emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp)
    ) {
        item {
            HeaderItem()
        }

        item {
            TextHeaderItem(text = "Body Weight Exercises")
        }

        item {
            // Use LazyColumn only if exerciseList is not empty
            if (exerciseList.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    exerciseList.forEach { exercise ->
                        item {
                            ExerciseItem(exercise = exercise, onItemClick = {
                                selectedExercise = exercise
                                navController.navigate(AppRoute.ExercisesScreen.route + "/${exercise.exerciseId}")
                            })
                        }
                    }
                }
            } else if (viewModel.isLoading.value == true) {
                // Optionally, show a loading indicator or a message when still loading
                Text(text = "Loading exercises...")
            } else {
                // Optionally, show a message when exerciseList is empty
                Text(text = "No exercises available.")
            }
        }

        item {
            TextHeaderItem(text = "Stretching Exercises")
        }

        item {
            // Use LazyColumn only if stretchingExerciseList is not empty
            if (stretchingExerciseList.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stretchingExerciseList.forEach { stretchingExercise ->
                        item {
                            ExerciseItem(exercise = stretchingExercise, onItemClick = {
                                selectedExercise = stretchingExercise
                                navController.navigate(AppRoute.ExercisesScreen.route + "/${stretchingExercise.exerciseId}")
                            })
                        }
                    }
                }
            } else if (viewModel.isLoading.value == true) {
                // Optionally, show a loading indicator or a message when still loading
                Text(text = "Loading stretching exercises...")
            } else {
                // Optionally, show a message when stretchingExerciseList is empty
                Text(text = "No stretching exercises available.")
            }
        }
    }
}


@Composable
fun HeaderItem() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_header_img),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Choose your workout!",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(0.6f)
                )
                Spacer(modifier = Modifier.weight(0.4f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Your regular weekly exercise routine should include both aerobic exercise...",
                    color = Gray300,
                    modifier = Modifier.weight(0.65f)
                )
                Spacer(modifier = Modifier.weight(0.35f))
            }
        }
    }
}

@Composable
fun TextHeaderItem(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        color = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun ExerciseItem(exercise: Exercise, onItemClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp),
        modifier = Modifier.size(100.dp),
        color = Color.Black,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = onItemClick
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            exercise.name?.let { Text(text = it) }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {

    HomeScreen(navController = rememberNavController())
}
