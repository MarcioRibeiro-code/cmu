package pt.ipp.estg.cmu.components.providers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.graphics.vector.ImageVector

object WorkoutFlowIconProvider {
    val defaultIcon: ImageVector = Icons.Default.FitnessCenter
    val homeIcon: ImageVector = Icons.Default.Home
    val workIcon: ImageVector = Icons.Default.Work
    val searchIcon: ImageVector = Icons.Default.Search
    val back: ImageVector = Icons.AutoMirrored.Outlined.ArrowBack;
    val profileIcon: ImageVector = Icons.Default.Person
    val HistoryExerciseIcon: ImageVector = Icons.Outlined.Analytics
    val bookmarkIcon : ImageVector = Icons.Outlined.FavoriteBorder
}