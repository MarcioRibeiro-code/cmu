package pt.ipp.estg.cmu.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.ipp.estg.cmu.datastore.FitnessAppRepository
import pt.ipp.estg.cmu.datastore.FitnessAppRoomDatabase
import pt.ipp.estg.cmu.fireStore.ExerciseAggregation
import pt.ipp.estg.cmu.fireStore.ExerciseAggregation.Exercise
import pt.ipp.estg.cmu.datastore.Exercise as LocalExercise

class ExercisesViewModel(application: Application) : AndroidViewModel(application) {


    private val exerciseAggregation = ExerciseAggregation()

    private val _exerciseList = MutableLiveData<List<Exercise>>(emptyList())
    val exerciseList: LiveData<List<Exercise>> get() = _exerciseList

    private val _stretchingExerciseList = MutableLiveData<List<Exercise>>(emptyList())
    val stretchingExerciseList: LiveData<List<Exercise>> get() = _stretchingExerciseList

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val fitnessAppRepository: FitnessAppRepository


    private val _exercise = MutableLiveData<Exercise?>()
    val exercise: LiveData<Exercise?> get() = _exercise

    val getDoneExercises: LiveData<List<LocalExercise>>
        get() = fitnessAppRepository.getDoneExercises()

    val getUndoneExercises: LiveData<List<LocalExercise>>
        get() = fitnessAppRepository.getUndoneExercises()


    init {
        val fitnessAppRoomDatabase = FitnessAppRoomDatabase.getInstance(application)
        val fitnessAppDao = fitnessAppRoomDatabase.fitnessAppDao()
        fitnessAppRepository = FitnessAppRepository(fitnessAppDao)
    }


    fun addExercise(exercise: LocalExercise) {
        viewModelScope.launch {
            try {
                fitnessAppRepository.addExercise(exercise)
            } catch (e: SQLiteConstraintException) {
                Log.e(TAG, "SQLiteConstraintException: UNIQUE constraint failed", e)

            }
        }
    }


    fun getExerciseById(exerciseId: String) {
        viewModelScope.launch {
            val exercise = exerciseAggregation.get(exerciseId)
            _exercise.value = exercise
        }
    }

    fun loadExerciseList() {
        viewModelScope.launch {
            _isLoading.value = true

            if (_exerciseList.value.isNullOrEmpty()) {
                _exerciseList.value = exerciseAggregation.getBodyWeightExercises()
            }

            if (_stretchingExerciseList.value.isNullOrEmpty()) {
                _stretchingExerciseList.value = exerciseAggregation.getStretchingExercises()
            }

            _isLoading.value = false
        }
    }

    fun updateExercise(exercise: LocalExercise) {
        viewModelScope.launch {
            fitnessAppRepository.updateExercise(exercise)
        }
    }


    companion object {
        val TAG = "ExercisesViewModel"
    }
}
