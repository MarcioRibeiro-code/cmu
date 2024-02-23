package pt.ipp.estg.cmu.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu.fireStore.BookmarksAggregation
import pt.ipp.estg.cmu.viewmodel.BookmarksViewModel

@Composable
fun BookmarksScreen(bookmarksViewModel: BookmarksViewModel = viewModel()) {

    val lazyListState = rememberLazyListState()
    val bookmarks by bookmarksViewModel.bookmarks.collectAsState(initial = emptyList())

    when {
        bookmarks.isEmpty() -> {
            BookmarksEmptyState()
        }

        else -> {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bookmarks) {
                    BookmarkItem(
                        bookmark = it,
                    )
                }

            }
        }
    }
}


@Composable
fun BookmarkItem(
    bookmarksViewModel: BookmarksViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    bookmark: BookmarksAggregation.BookmarkSegment
) {
    ListItem(
        headlineContent = {
            Text(text = bookmark.segmentName)

        },
        supportingContent = {
            Text(text = bookmark.latitude.toString())
            Text(text = bookmark.longitude.toString())
        },
        leadingContent = {
            FilledTonalIconButton(onClick = {
                bookmarksViewModel.bookmarkSegment(bookmark)
            }) {
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Saved Place")
            }
        },
    )
}


@Composable
fun BookmarksEmptyState(
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically)
    ) {
        FilledTonalIconButton(onClick = { }) {
            Icon(Icons.Outlined.Park, contentDescription = null)
        }
        Text(
            text = "There is no saved Places yet!",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Start by save a Place.",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}
