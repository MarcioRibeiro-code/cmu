package pt.ipp.estg.cmu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pt.ipp.estg.cmu.fireStore.BookmarksAggregation

class BookmarksViewModel(application: Application) : AndroidViewModel(application) {

     private val aggregation = BookmarksAggregation()

     val bookmarks: Flow<List<BookmarksAggregation.BookmarkSegment>> = aggregation.bookmarks


     fun bookmarkSegment(segment: BookmarksAggregation.BookmarkSegment) {
         viewModelScope.launch {
             val documentId = segment.segmentId
             if (aggregation.isBookmarket(documentId)) {
                 aggregation.delete(documentId)
             } else {
                 aggregation.save(segment)
             }
         }
     }

    companion object {
        private const val TAG = "BookmarksViewModel"
    }
}