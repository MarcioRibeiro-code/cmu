package pt.ipp.estg.cmu.fireStore

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RatingsAgreggation(private val firestore: FirebaseFirestore = Firebase.firestore, private val auth: FirebaseAuth = Firebase.auth) {

    companion object {
        private const val TAG = "RatingsAggregation"
        private const val RATINGS_COLLECTION = "ratings"
    }


    data class SegmentRating(
        @DocumentId val segmentID: Int = 0,
        var avgRating: Double = 0.0,
        var numRatings:Int = 0
    )

    suspend fun getRatingsById(segmentID: String): SegmentRating? {
        return firestore.collection(RATINGS_COLLECTION)
            .document(segmentID)
            .get().await().toObject()
    }

    fun addRating(segmentID: Int, rating: Double): Task<Void> {
        val evStationRef = firestore.collection(RATINGS_COLLECTION).document(segmentID.toString())

        return firestore.runTransaction { transaction ->

            // In a transaction, add the new rating and update the aggregate totals
            val evStation =
                transaction.get(evStationRef).toObject<SegmentRating>()
                    ?: SegmentRating(segmentID, 0.0, 0)

            // Compute new number of ratings
            val newNumRatings = evStation.numRatings + 1

            // Compute new average rating
            val oldRatingTotal = evStation.avgRating * evStation.numRatings
            val newAvgRating = (oldRatingTotal + rating) / newNumRatings

            // Set new Segment Rating Info
            evStation.numRatings = newNumRatings
            evStation.avgRating = newAvgRating

            transaction.set(evStationRef, evStation, SetOptions.merge())

            // Success
            null
        }
    }




}