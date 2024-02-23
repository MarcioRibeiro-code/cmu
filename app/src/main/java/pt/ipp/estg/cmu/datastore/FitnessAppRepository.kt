package pt.ipp.estg.cmu.datastore

import androidx.lifecycle.LiveData

class FitnessAppRepository(private val fitnessAppDao: FitnessAppDao) {


    // Exercícios
    suspend fun addExercise(exercise: Exercise) {
        fitnessAppDao.insertExercise(exercise)
    }

    suspend fun updateExercise(exercise: Exercise) {
        fitnessAppDao.updateExercise(exercise)
    }

    suspend fun deleteExercise(exercise: Exercise) {
        fitnessAppDao.deleteExercise(exercise)
    }

    fun getExercises(): LiveData<List<Exercise?>> {
        return fitnessAppDao.getAllExercises()
    }


    fun getUndoneExercises(): LiveData<List<Exercise>> {
        return fitnessAppDao.getUndoneExercises()
    }


    fun getDoneExercises(): LiveData<List<Exercise>> {
        return fitnessAppDao.getDoneExercises()
    }
    // Segmentos

    suspend fun insertSegment(segment: Segments) {
        return fitnessAppDao.insertSegment(segment)
    }

    suspend fun updateSegment(segment: Segments) {
        return fitnessAppDao.updateSegment(segment)
    }


    suspend fun deleteSegment(segment: Segments) {
        return fitnessAppDao.deleteSegment(segment)
    }


    fun getSegmentsById(placeId: Long): LiveData<Segments?> {
        return fitnessAppDao.getSegmentsById(placeId)
    }

    fun getAllSegments(): LiveData<List<Segments?>> {
        return fitnessAppDao.getAllSegments()
    }


    // Usuário
    suspend fun addUser(user: UserModel) {
        fitnessAppDao.insertUserModel(user)
    }

    suspend fun updateUser(user: UserModel) {
        fitnessAppDao.updateUserModel(user)
    }

    suspend fun deleteUser(user: UserModel) {
        fitnessAppDao.deleteUserModel(user)
    }

    fun getUser(): LiveData<UserModel?> {
        return fitnessAppDao.getUserModel()
    }


    /*
    suspend fun addLocalGroupedExercise(localGroupedExercise: LocalGroupedExercise) {
        fitnessAppDao.insertLocalGroupedExercise(localGroupedExercise)
    }

    fun getAllLocalGroupedExercises(): LiveData<List<LocalGroupedExercise>> {
        return fitnessAppDao.getAllLocalGroupedExercises()
    }

    suspend fun updateLocalGroupedExercise(localGroupedExercise: LocalGroupedExercise) {
        fitnessAppDao.updateLocalGroupedExercise(localGroupedExercise)
    }

    fun getLocalGroupedExerciseById(id: Long): LiveData<LocalGroupedExercise?> {
        return fitnessAppDao.getLocalGroupedExerciseById(id)
    }

    suspend fun deleteLocalGroupedExercise(id: Long) {
        fitnessAppDao.deleteLocalGroupedExercise(id)
    }

    fun getLocalGroupedExercisesByDate(date: String): LiveData<LocalGroupedExercise?> {
        return fitnessAppDao.getLocalGroupedExercisesByDate(date)
    }

    fun getAllCompletedLocalGroupedExercises(): LiveData<List<LocalGroupedExercise>> {
        return fitnessAppDao.getAllCompletedLocalGroupedExercises()
    }

    /*
    * Segments
    * */
    fun getSegmentsById(placeId: String): LiveData<Segments> {
        return fitnessAppDao.getSegmentsById(placeId)
    }


    fun getGroupedExercisesWithSegmentsCompleted(): LiveData<List<LocalGroupedExerciseWithExercise>> {
        return fitnessAppDao.getGroupedExercisesWithSegmentsCompleted()
    }

    suspend fun addGroupedExerciseWithSegment(groupedExerciseWithSegments: Segments){
        return fitnessAppDao.insertGroupedExerciseWithSegments(groupedExerciseWithSegments)
    }

    fun getGroupedExercisesWithSegmentsUnCompleted(): LiveData<List<GroupedExerciseWithSegments>> {
        return fitnessAppDao.getGroupgetGroupedExercisesWithSegmentsUnCompleteded()
    }


    /*
    * Grouped Exercises with List of Exercises
    * */
    fun getAllCompletedLocalGroupedExercisesWithExercises(): LiveData<List<LocalGroupedExerciseWithExercise>> {
        return fitnessAppDao.getAllCompletedLocalGroupedExercisesWithExercises()
    }


    /*
    * USER
    * */
    suspend fun addUser(user: UserModel) {
        fitnessAppDao.insertUserModel(user)
    }

    suspend fun updateUser(user: UserModel) {
        fitnessAppDao.updateUserModel(user)
    }

    fun getUser(): LiveData<UserModel?> {
        return fitnessAppDao.getUserModel()
    }

    /*
    * Exercises
    * */

    fun getExercisesByGroupId(groupId: String): LiveData<List<Exercise>> {
        return fitnessAppDao.getExercisesByGroupId(groupId)
    }

    suspend fun addExercise(exercise: Exercise) {
        fitnessAppDao.insertExercise(exercise)
    }

    suspend fun updateExercise(exercise: Exercise) {
        fitnessAppDao.updateExercise(exercise)
    }

    suspend fun deleteExercise(exercise: Exercise) {
        fitnessAppDao.deleteExercise(exercise)
    }

     fun getExercises(): LiveData<List<Exercise>> {
        return fitnessAppDao.getAllExercises()
    }*/
}
