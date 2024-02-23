package pt.ipp.estg.cmu.navigation.WorkoutFlow

import androidx.compose.ui.graphics.vector.ImageVector
import pt.ipp.estg.cmu.components.providers.WorkoutFlowIconProvider.HistoryExerciseIcon
import pt.ipp.estg.cmu.components.providers.WorkoutFlowIconProvider.bookmarkIcon

import pt.ipp.estg.cmu.components.providers.WorkoutFlowIconProvider.defaultIcon
import pt.ipp.estg.cmu.components.providers.WorkoutFlowIconProvider.profileIcon
import pt.ipp.estg.cmu.components.providers.WorkoutFlowIconProvider.searchIcon
import pt.ipp.estg.cmu.components.providers.WorkoutFlowIconProvider.workIcon

enum class AppRoute(
    val route: String,
    val icon: ImageVector,
    val navigationLabel: String,
    val topBarLabel: String?,
) {
    HomeScreen("Home_Screen", defaultIcon, "Home", "Home Screen"),
    ExercisesScreen("Exercises_Screen", workIcon, "Planned Exercises", "Exercises Screen"),
    SearchPlaces("Search_Screen", searchIcon, "Search Places", "Search Places"),
    ProfileScreen("Profile_Screen", profileIcon, "Profile", "Profile Screen"),
    BookmarkersScreen("Bookmarks_Screen", bookmarkIcon, "Bookmarks", "Bookmarks Screen"),
    HistoryScreen("History_Screen", HistoryExerciseIcon, "History", "Exercises History Screen")
}

