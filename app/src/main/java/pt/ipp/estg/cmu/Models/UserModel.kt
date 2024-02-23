package pt.ipp.estg.cmu.Models

import java.time.LocalDate

data class UserModel(
    val id: String,
    var name: String,
    val userBody: UserBody,
)

data class UserBody(
    var weight: Float,
    var height: Float,
    var age: Int
)


data class GroupedExercises(
    val id: String,
    val exercises: List<Exercise>,
    val date: LocalDate,
    val isCompleted: Boolean = false
)