package pt.ipp.estg.cmu.fireStore

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class BookmarksAggregation(
    private val firestore: FirebaseFirestore = Firebase.firestore,
) {
    data class BookmarkSegment(
        @DocumentId val segmentId: String = "",
        val segmentName: String = "",
        val latitude: Double? = null,
        val longitude: Double? = null,
    )


    val bookmarks: Flow<List<BookmarkSegment>> =
        firestore
            .collection(BOOKMARKS_COLLECTION)
            .snapshots()
            .map { snapshot -> snapshot.toObjects() }


    suspend fun isBookmarket(segmentId: String): Boolean =
        firestore
            .collection(BOOKMARKS_COLLECTION)
            .document(segmentId)
            .get()
            .await().exists()


    suspend fun get(segmentId: String): BookmarkSegment? =
        firestore
            .collection(BOOKMARKS_COLLECTION)
            .document(segmentId)
            .get()
            .await().toObject()

    suspend fun save(segment : BookmarkSegment) =
        firestore
            .collection(BOOKMARKS_COLLECTION)
            .add(segment)
            .await()


    suspend fun delete(segmentId: String) =
        firestore
            .collection(BOOKMARKS_COLLECTION)
            .document(segmentId)
            .delete()
            .await()

    companion object {
        private const val TAG = "BookmarksAggregation"
        private const val USER_COLLECTION = "users"
        private const val BOOKMARKS_COLLECTION = "bookmarks"
    }
}