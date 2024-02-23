package pt.ipp.estg.cmu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.ipp.estg.cmu.datastore.FitnessAppRepository
import pt.ipp.estg.cmu.datastore.FitnessAppRoomDatabase
import pt.ipp.estg.cmu.datastore.UserModel

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val _user = MutableLiveData<UserModel?>(null)
    val user: LiveData<UserModel?> get() = fitnessAppRepository.getUser()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var fitnessAppRepository: FitnessAppRepository

    init {
        val fitnessAppRoomDatabase = FitnessAppRoomDatabase.getInstance(application)
        val fitnessAppDao = fitnessAppRoomDatabase.fitnessAppDao()
        fitnessAppRepository = FitnessAppRepository(fitnessAppDao)
    }

    fun addUser(user: UserModel) {
        viewModelScope.launch {
            fitnessAppRepository.addUser(user)
        }
    }

    // Function to update the user's weight
    /* fun updateUserWeight(newWeight: Float) {
         viewModelScope.launch {
             val currentUser = user.value
             currentUser?.let {
                 it.weight = newWeight
                 updateUser(it)
                 println("User Weight updated: ${it.weight}")
             }
         }
     }



    /* fun updateUserHeight(newHeight: Float) {
         println("updateUserHeight:$newHeight")
         viewModelScope.launch {
             val currentUser = user.value
             currentUser?.let {
                 it.height = newHeight
                 updateUser(it)
             }
         }
     }*/

    /* fun updateUserAge(newAge: Int) {
         viewModelScope.launch {
             val currentUser = user.value
             currentUser?.let {
                 it.age = newAge
                 updateUser(it)
             }
         }
     }*/

     /*fun updateUserName(newName: String) {
         viewModelScope.launch {
             val currentUser = user.value
             currentUser?.let {
                 it.name = newName
                 updateUser(it)
             }
         }
     }*/
 */

    // Function to update the entire user
    fun updateUser(user: UserModel) {
        _user.value = user
        viewModelScope.launch {
            fitnessAppRepository.updateUser(user)
            println("User updated 1: $user")
        }
        println("User updated 2: $user")
    }

}