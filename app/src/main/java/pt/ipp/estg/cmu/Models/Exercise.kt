package pt.ipp.estg.cmu.Models

data class Exercise(
    val id: String,
    val name: String,
    val difficultyLevels: List<ExerciseDifficulty>,
    val bodyDescription: String,
    val ExerciseType : ExerciseType
)

data class ExerciseDifficulty(
    val difficultyLevel: DifficultyLevel,
    val repetitions: Int,
    val sets: Int,
    val restTimeSeconds: Int
)


enum class DifficultyLevel {
    PRO,
    INTERMEDIATE,
    BEGINNER
}

enum class ExerciseType {
    STRETCHING,
    BODYWEIGHT
}