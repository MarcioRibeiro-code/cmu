package pt.ipp.estg.cmu.datastore

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FitnessAppDao {

    @Insert
    suspend fun insertExercise(exercise: Exercise)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): LiveData<List<Exercise?>>


    @Query("SELECT * FROM exercises WHERE isDone = 0 ORDER BY date")
    fun getUndoneExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE isDone = 1 ORDER BY date")
    fun getDoneExercises(): LiveData<List<Exercise>>

    @Insert
    suspend fun insertUserModel(userModel: UserModel)

    @Update
    suspend fun updateUserModel(userModel: UserModel)

    @Delete
    suspend fun deleteUserModel(userModel: UserModel)

    @Query("SELECT * FROM local_user")
    fun getUserModel(): LiveData<UserModel?>


    @Insert
    suspend fun insertSegment(segment: Segments)

    @Update
    suspend fun updateSegment(segment: Segments)

    @Delete
    suspend fun deleteSegment(segment: Segments)

    @Query("SELECT * FROM Segments")
    fun getAllSegments(): LiveData<List<Segments?>>

    @Query("SELECT * FROM Segments WHERE place_id = :id")
    fun getSegmentsById(id: Long): LiveData<Segments?>


    /*




        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertLocalGroupedExercise(localGroupedExercise: LocalGroupedExercise)


        @Update
        suspend fun updateLocalGroupedExercise(localGroupedExercise: LocalGroupedExercise)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertGroupedExerciseWithSegments(groupedExerciseWithSegments: Segments)

        @Query("SELECT * FROM Segments WHERE place_id = :placeId")
        fun getSegmentsById(placeId: String): LiveData<Segments>

        @Query("SELECT * FROM exercises WHERE groupID = :groupId")
        fun getExercisesByGroupId(groupId: String): LiveData<List<Exercise>>

        @Transaction
        @Query("SELECT * FROM local_grouped_exercises WHERE isCompleted = 1")
        fun getGroupedExercisesWithSegmentsCompleted(): LiveData<List<LocalGroupedExerciseWithExercise>>

        @Transaction
        @Query("SELECT * FROM local_grouped_exercises WHERE isCompleted = 0 ORDER BY date ASC")
        fun getGroupgetGroupedExercisesWithSegmentsUnCompleteded(): LiveData<List<GroupedExerciseWithSegments>>

        @Query("SELECT * FROM local_grouped_exercises WHERE isCompleted = 1")
        fun getAllCompletedLocalGroupedExercises(): LiveData<List<LocalGroupedExercise>>

        @Query("SELECT * FROM local_grouped_exercises WHERE isCompleted = 0 ORDER BY date ASC")
        fun getAllLocalGroupedExercises(): LiveData<List<LocalGroupedExercise>>


        @Query("SELECT * FROM local_grouped_exercises WHERE isCompleted = 0 AND date = :date")
        fun getLocalGroupedExercisesByDate(date: String): LiveData<LocalGroupedExercise?>

        @Query("SELECT * FROM local_grouped_exercises WHERE id = :id")
        fun getLocalGroupedExerciseById(id: Long): LiveData<LocalGroupedExercise?>

        @Query("DELETE FROM local_grouped_exercises WHERE id = :id")
        suspend fun deleteLocalGroupedExercise(id: Long)


        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertUser(user: UserModel)

        @Update
        suspend fun updateUser(user: UserModel)

        @Query("SELECT * FROM local_user")
        fun getUser(): LiveData<UserModel?>

        @Transaction
        @Query("SELECT * FROM local_grouped_exercises WHERE isCompleted = 1")
        fun getAllCompletedLocalGroupedExercisesWithExercises(): LiveData<List<LocalGroupedExerciseWithExercise>>


        /*
        * Exercises
        * */

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertExercise(exercise: Exercise)

        @Update
        suspend fun updateExercise(exercise: Exercise)

        @Delete
        suspend fun deleteExercise(exercise: Exercise)

        @Query("SELECT * FROM exercises")
        fun getAllExercises(): LiveData<List<Exercise>>*/
}
