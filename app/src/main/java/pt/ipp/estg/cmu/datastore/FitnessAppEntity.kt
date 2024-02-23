package pt.ipp.estg.cmu.datastore

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.ipp.estg.cmu.fireStore.ExerciseAggregation
import java.time.LocalDate

@Entity(
    tableName = "exercises",
    primaryKeys = ["exerciseId", "date"]
)
data class Exercise(
    val exerciseId: String,
    var place_id: String,
    val name: String,
    val bodyDescription: String,
    val exerciseType: ExerciseAggregation.ExerciseType,
    val difficultyLevel: ExerciseAggregation.DifficultyLevel,
    val repetitions: Int,
    val sets: Int,
    val restTimeSeconds: Int,
    var isDone: Boolean,
    val date: LocalDate
)


@Entity(tableName = "local_user")
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    var weight: Float,
    var height: Float,
    var age: Int
)

@Entity(tableName = "Segments")
data class Segments(
    @PrimaryKey
    val place_id: String = "",
    val vicinity: String = "",
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)


