package pt.ipp.estg.cmu.datastore

import android.util.Log
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val PLACES_API_URL = "https://maps.googleapis.com/maps/api/"
private val API_KEY = "AIzaSyAG_ZbYaXPWRszfWCroidY4FwFVJLhxdVg";


data class PlaceDetails(
    @SerializedName("geometry")
    val geometry: Geometry? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("vicinity")
    val vicinity: String? = null,
    @SerializedName("place_id")
    val place_id: String? = null
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)

interface PlacesApiRequests {
    @GET("place/nearbysearch/json")
    fun getPlaceDetails(
        @Query("location") location: String,
        @Query("radius") radius: Int = 5000,
        @Query("type") type: String = "park",
        @Query("key") apiKey: String = API_KEY
    ): Call<List<PlaceDetails>>
}

data class SearchPlaceDetailsInput(
    val location: String,
    val radius: Int = 5000,
    val type: String = "park"
)


class PlacesAPIService {

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl(PLACES_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: PlacesApiRequests = retrofit.create(PlacesApiRequests::class.java)
    }

    fun lookupPlaceDetails(
        input: SearchPlaceDetailsInput,
        callback: (data: List<PlaceDetails>?, error: Throwable?) -> Unit
    ) {
        service.getPlaceDetails(
            location = input.location,
            radius = input.radius,
            type = input.type
        ).enqueue(object : Callback<List<PlaceDetails>> {
            override fun onResponse(
                call: Call<List<PlaceDetails>>,
                response: Response<List<PlaceDetails>>
            ) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("NetworkError"))
                }

                Log.d("PlacesAPIService#isSuccessful", response.isSuccessful.toString())
            }

            override fun onFailure(call: Call<List<PlaceDetails>>, t: Throwable) {
                Log.d("PlacesAPIService#onFailure", t.message, t)
                callback(null, t)
            }
        })
    }
}

