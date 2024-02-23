package pt.ipp.estg.cmu.fireStore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import pt.ipp.estg.cmu.Models.ExerciseType

class ExerciseAggregation(
    private val firestore: FirebaseFirestore = Firebase.firestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    data class Exercise(
        @DocumentId val exerciseId: String = "",
        val name: String? = null,
        val bodyDescription: String? = null,
        val exerciseType: ExerciseType? = null,
        val difficulty: List<ExerciseDifficulty>? = null
    )

    data class ExerciseDifficulty(
        val difficultyLevel: DifficultyLevel? = null,
        val repetitions: Int? = null,
        val sets: Int? = null,
        val restTimeSeconds: Int? = null
    )

    enum class DifficultyLevel {
        PRO, INTERMEDIATE, BEGINNER
    }

    enum class ExerciseType {
        STRETCHING, BODYWEIGHT
    }



    val exercises: Flow<List<Exercise>> =
        firestore.collection(EXERCISES).snapshots().map { snapshot -> snapshot.toObjects() }

    // get exercise by ID
    suspend fun get(exerciseId: String): Exercise? =
        firestore.collection(EXERCISES).document(exerciseId).get().await().toObject()

    // Filtra firestore e retorna os documentos do tipo bodyweight em formato Exercise Object
    suspend fun getBodyWeightExercises(): List<Exercise> =
        firestore.collection(EXERCISES).whereEqualTo("exerciseType", ExerciseType.BODYWEIGHT.name)
            .get().await().toObjects(Exercise::class.java)

    // Filtra firestore e retorna os documentos do tipo Stretchin em formato Exercise Object
    suspend fun getStretchingExercises(): List<Exercise> =
        firestore.collection(EXERCISES).whereEqualTo("exerciseType", ExerciseType.STRETCHING.name)
            .get().await().toObjects(Exercise::class.java)

    // Guarda um exercício
    suspend fun save(exercise: Exercise) {
        firestore.collection(EXERCISES).document(exercise.exerciseId).set(exercise).await()
    }

    /*suspend fun saveExerciseDifficulty(exerciseId: String, difficulty: ExerciseDifficulty) {
        firestore.collection(EXERCISES).document(exerciseId).update(
                "difficulty", mapOf(
                    "difficultyLevel" to difficulty.difficultyLevel?.name,
                    "repetitions" to difficulty.repetitions,
                    "sets" to difficulty.sets,
                    "restTimeSeconds" to difficulty.restTimeSeconds
                )
            ).await()
    }*/


    // se estiver vazio os exercícios do firestore ele injeta aquela lista estatica de exercícios
    suspend fun loadDummyBodyWeightExercises() {
        val bodyWeightExercises = getBodyWeightExercises()
        if (bodyWeightExercises.isEmpty()) {
            val dummyExercises = getDummyBodyWeightExercises()
            dummyExercises.forEach { exercise ->
                save(exercise)
            }
        }
    }

    // se estiber vazio os exercícios do firestore ele injeta aquela lista estatica de exercícios
    suspend fun loadDummyStretchingExercises() {
        val stretchingExercises = getStretchingExercises()
        if (stretchingExercises.isEmpty()) {
            val dummyExercises = getDummyStretchingExercises()
            dummyExercises.forEach { exercise ->
                save(exercise)
            }
        }
    }


    /**
     * Exercícios Estáticos a serem injetados no firestore
     */

    private fun getDummyBodyWeightExercises(): List<Exercise> {
        // Replace this with your actual data loading logic
        return listOf(
            Exercise(
                exerciseId = "1",
                name = "Push-ups",
                difficulty = listOf(
                    ExerciseDifficulty(
                        DifficultyLevel.BEGINNER, repetitions = 15, sets = 2, restTimeSeconds = 30
                    ), ExerciseDifficulty(
                        DifficultyLevel.INTERMEDIATE,
                        repetitions = 12,
                        sets = 3,
                        restTimeSeconds = 45
                    ), ExerciseDifficulty(
                        DifficultyLevel.PRO, repetitions = 10, sets = 4, restTimeSeconds = 60
                    )
                ),
                bodyDescription = "Chest, Shoulders, Triceps",
                exerciseType = ExerciseType.BODYWEIGHT
            ), Exercise(
                exerciseId = "2", name = "Pull-ups", difficulty = listOf(
                    ExerciseDifficulty(
                        DifficultyLevel.BEGINNER, repetitions = 10, sets = 3, restTimeSeconds = 30
                    ), ExerciseDifficulty(
                        DifficultyLevel.INTERMEDIATE,
                        repetitions = 8,
                        sets = 4,
                        restTimeSeconds = 45
                    ), ExerciseDifficulty(
                        DifficultyLevel.PRO, repetitions = 6, sets = 5, restTimeSeconds = 60
                    )
                ), bodyDescription = "Back, Biceps", exerciseType = ExerciseType.BODYWEIGHT
            ), Exercise(
                exerciseId = "3", name = "Squats", difficulty = listOf(
                    ExerciseDifficulty(
                        DifficultyLevel.BEGINNER, repetitions = 20, sets = 3, restTimeSeconds = 30
                    ), ExerciseDifficulty(
                        DifficultyLevel.INTERMEDIATE,
                        repetitions = 15,
                        sets = 4,
                        restTimeSeconds = 45
                    ), ExerciseDifficulty(
                        DifficultyLevel.PRO, repetitions = 12, sets = 5, restTimeSeconds = 60
                    )
                ), bodyDescription = "Legs", exerciseType = ExerciseType.BODYWEIGHT
            ), Exercise(
                exerciseId = "4", name = "Lunges", difficulty = listOf(
                    ExerciseDifficulty(
                        DifficultyLevel.BEGINNER, repetitions = 20, sets = 3, restTimeSeconds = 30
                    ), ExerciseDifficulty(
                        DifficultyLevel.INTERMEDIATE,
                        repetitions = 15,
                        sets = 4,
                        restTimeSeconds = 45
                    ), ExerciseDifficulty(
                        DifficultyLevel.PRO, repetitions = 12, sets = 5, restTimeSeconds = 60
                    )
                ), bodyDescription = "Legs", exerciseType = ExerciseType.BODYWEIGHT
            ), Exercise(
                exerciseId = "5", name = "Plank", difficulty = listOf(
                    ExerciseDifficulty(
                        DifficultyLevel.BEGINNER, repetitions = 10, sets = 3, restTimeSeconds = 30
                    ), ExerciseDifficulty(
                        DifficultyLevel.INTERMEDIATE,
                        repetitions = 8,
                        sets = 4,
                        restTimeSeconds = 45
                    ), ExerciseDifficulty(
                        DifficultyLevel.PRO, repetitions = 6, sets = 5, restTimeSeconds = 60
                    )
                ), bodyDescription = "Core", exerciseType = ExerciseType.BODYWEIGHT
            ), Exercise(
                exerciseId = "6", name = "Crunches", difficulty = listOf(
                    ExerciseDifficulty(
                        DifficultyLevel.BEGINNER, repetitions = 15, sets = 2, restTimeSeconds = 30
                    ), ExerciseDifficulty(
                        DifficultyLevel.INTERMEDIATE,
                        repetitions = 12,
                        sets = 3,
                        restTimeSeconds = 45
                    ), ExerciseDifficulty(
                        DifficultyLevel.PRO, repetitions = 10, sets = 4, restTimeSeconds = 60
                    )
                ), bodyDescription = "Core", exerciseType = ExerciseType.BODYWEIGHT
            ), Exercise(
                exerciseId = "7", name = "Dips", difficulty = listOf(
                    ExerciseDifficulty(
                        DifficultyLevel.BEGINNER, repetitions = 10, sets = 3, restTimeSeconds = 30
                    ), ExerciseDifficulty(
                        DifficultyLevel.INTERMEDIATE,
                        repetitions = 8,
                        sets = 4,
                        restTimeSeconds = 45
                    ), ExerciseDifficulty(
                        DifficultyLevel.PRO, repetitions = 6, sets = 5, restTimeSeconds = 60
                    )
                ), bodyDescription = "Triceps", exerciseType = ExerciseType.BODYWEIGHT
            ), Exercise(
                exerciseId = "8", name = "Bench Dips", difficulty = listOf(
                    ExerciseDifficulty(
                        DifficultyLevel.BEGINNER, repetitions = 15, sets = 2, restTimeSeconds = 30
                    ), ExerciseDifficulty(
                        DifficultyLevel.INTERMEDIATE,
                        repetitions = 12,
                        sets = 3,
                        restTimeSeconds = 45
                    ), ExerciseDifficulty(
                        DifficultyLevel.PRO, repetitions = 10, sets = 4, restTimeSeconds = 60
                    )
                ), bodyDescription = "Triceps", exerciseType = ExerciseType.BODYWEIGHT
            )
        )
    }

    private fun getDummyStretchingExercises(): List<Exercise> {
        return listOf(
            Exercise(
                exerciseId = "9", name = "Neck ", difficulty = listOf(
                    ExerciseDifficulty(
                        DifficultyLevel.BEGINNER, repetitions = 2, sets = 2, restTimeSeconds = 15
                    ), ExerciseDifficulty(
                        DifficultyLevel.INTERMEDIATE,
                        repetitions = 3,
                        sets = 3,
                        restTimeSeconds = 20
                    ), ExerciseDifficulty(
                        DifficultyLevel.PRO, repetitions = 4, sets = 4, restTimeSeconds = 25
                    )
                ), bodyDescription = "Neck", exerciseType = ExerciseType.STRETCHING
            ),
            Exercise(
                exerciseId = "10", name = "Shoulder", difficulty = listOf(
                    ExerciseDifficulty(
                        DifficultyLevel.BEGINNER, repetitions = 2, sets = 2, restTimeSeconds = 15
                    ), ExerciseDifficulty(
                        DifficultyLevel.INTERMEDIATE,
                        repetitions = 3,
                        sets = 3,
                        restTimeSeconds = 20
                    ), ExerciseDifficulty(
                        DifficultyLevel.PRO, repetitions = 4, sets = 4, restTimeSeconds = 25
                    )
                ), bodyDescription = "Shoulders", exerciseType = ExerciseType.STRETCHING
            ),
            Exercise(
                exerciseId = "11", name = "Hamstring", difficulty = listOf(
                    ExerciseDifficulty(
                        DifficultyLevel.BEGINNER, repetitions = 2, sets = 2, restTimeSeconds = 15
                    ), ExerciseDifficulty(
                        DifficultyLevel.INTERMEDIATE,
                        repetitions = 3,
                        sets = 3,
                        restTimeSeconds = 20
                    ), ExerciseDifficulty(
                        DifficultyLevel.PRO, repetitions = 4, sets = 4, restTimeSeconds = 25
                    )
                ), bodyDescription = "Hamstrings", exerciseType = ExerciseType.STRETCHING
            ),
        )
    }

    companion object {
        private const val TAG = "ExerciseAggregation"
        private const val EXERCISES = "exercises"
    }
}
