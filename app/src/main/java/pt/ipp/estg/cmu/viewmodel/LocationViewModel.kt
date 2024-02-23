package pt.ipp.estg.cmu.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class  LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val _location = MutableLiveData<Location?>(null)
    val location: LiveData<Location?> get() = _location

    fun updateLocation(location: Location) {
        _location.value = location
    }
}