package pt.ipp.estg.cmu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.ipp.estg.cmu.datastore.FitnessAppRepository
import pt.ipp.estg.cmu.datastore.FitnessAppRoomDatabase
import pt.ipp.estg.cmu.datastore.PlacesAPIService
import pt.ipp.estg.cmu.datastore.SearchPlaceDetailsInput
import pt.ipp.estg.cmu.datastore.Segments
import pt.ipp.estg.cmu.fireStore.RatingsAgreggation

class SegmentsViewModel(application: Application) : AndroidViewModel(application) {


    private val PlacesAPIService = PlacesAPIService()
    private val ratingsAgreggation = RatingsAgreggation()
    private var fitnessAppRepository: FitnessAppRepository

    init {
        val fitnessAppRoomDatabase = FitnessAppRoomDatabase.getInstance(application)
        val fitnessAppDao = fitnessAppRoomDatabase.fitnessAppDao()
        fitnessAppRepository = FitnessAppRepository(fitnessAppDao)
    }


    private val _selectedSegment = MutableLiveData<Segments?>()
    val selectedSegment: MutableLiveData<Segments?>
        get() = _selectedSegment


    private val _selectedSegmentRating = MutableLiveData<RatingsAgreggation.SegmentRating>()
    val selectedEvStationRating: LiveData<RatingsAgreggation.SegmentRating>
        get() = _selectedSegmentRating


    val segmentsList: LiveData<List<Segments?>> get() = fitnessAppRepository.getAllSegments()

    fun setCurrentSelectedSegment(placeID: Long) {
        fitnessAppRepository.getSegmentsById(placeID).observeForever { segment ->
            _selectedSegment.value = segment
        }
    }


    fun fetchSegmentRating(localID: String) {
        viewModelScope.launch {
            val rating = ratingsAgreggation.getRatingsById(localID.toString())
            _selectedSegmentRating.value = rating ?: RatingsAgreggation.SegmentRating()
        }
    }

    fun fetchAndSavePlaceDetails(
        params: SearchPlaceDetailsInput,
    ) {
        PlacesAPIService.lookupPlaceDetails(params) { placeDetails, _ ->
            viewModelScope.launch {

                placeDetails?.mapIndexed { i, placeDetails ->
                    placeDetails.geometry?.location.let {
                        it?.let { it1 ->
                            Segments(
                                place_id = placeDetails.place_id ?: "",
                                vicinity = placeDetails.vicinity ?: "",
                                name = placeDetails.name ?: "",
                                latitude = it1.lat,
                                longitude = it.lng
                            )
                        }
                    }?.let {
                        fitnessAppRepository.insertSegment(it)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "SegmentsViewModel"
    }
}

