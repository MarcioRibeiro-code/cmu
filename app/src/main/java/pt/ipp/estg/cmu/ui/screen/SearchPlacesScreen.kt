import android.location.Location
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import pt.ipp.estg.cmu.datastore.SearchPlaceDetailsInput
import pt.ipp.estg.cmu.datastore.Segments
import pt.ipp.estg.cmu.fireStore.BookmarksAggregation
import pt.ipp.estg.cmu.viewmodel.BookmarksViewModel
import pt.ipp.estg.cmu.viewmodel.LocationViewModel
import pt.ipp.estg.cmu.viewmodel.MapViewModel
import pt.ipp.estg.cmu.viewmodel.SegmentsViewModel


@Composable
fun MapScreen(
    navController: NavController,
    locationViewModel: LocationViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    mapViewModel: MapViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    segmentsViewModel: SegmentsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    bookmarksViewModel: BookmarksViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val location = locationViewModel.location.observeAsState()
    val segmentsList by segmentsViewModel.segmentsList.observeAsState(emptyList())

    LaunchedEffect(location) {
        Log.d("MapScreen", "location: $location")
    }

    MapScreen(
        location = location.value,
        viewModel = segmentsViewModel,
        segmentsList = segmentsList,
        bookmarksViewModel = bookmarksViewModel
    )
}

@Composable
fun MapScreen(
    location: Location?,
    viewModel: SegmentsViewModel,
    segmentsList: List<Segments?>,
    bookmarksViewModel: BookmarksViewModel
) {
    val cameraPositionState = rememberCameraPositionState {
        if (location != null) {
            position =
                CameraPosition.fromLatLngZoom(LatLng(location.latitude, location.longitude), 10f)
        }
    }

    var selectedIndex by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    var selectedSegment by remember { mutableStateOf<Segments?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .size(300.dp),
            cameraPositionState = cameraPositionState,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                location?.let { currentLocation ->
                    viewModel.fetchAndSavePlaceDetails(
                        SearchPlaceDetailsInput(
                            location = "${currentLocation.latitude},${currentLocation.longitude}",
                            radius = 5000,
                            type = "park"
                        )
                    )
                }
            }
        ) {
            Text("Obter Segmentos perto de Si")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = selectedSegment?.name ?: "Nenhum segmento selecionado",
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                segmentsList.forEachIndexed { index, segment ->
                    if (segment != null) {
                        DropdownMenuItem(
                            onClick = {
                                selectedIndex = index
                                selectedSegment = segment
                                expanded = false
                                showDialog = true
                            },
                            text = {
                                Text("ID: ${segment.name}")
                            }
                        )
                    }
                }
            }
        }

        // Mostra o Dialog quando showDialog for verdadeiro
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(text = "Detalhes do Segmento")
                },
                text = {
                    // Adicione detalhes específicos do segmento aqui conforme necessário
                    Text(text = "Nome: ${selectedSegment?.name ?: "N/A"}")
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                selectedSegment?.let {
                                    val bookmarkSegment = BookmarksAggregation.BookmarkSegment(
                                        // Ajuste conforme sua lógica
                                        segmentId = it.place_id,
                                        segmentName = it.name,
                                    )
                                    bookmarksViewModel.bookmarkSegment(bookmarkSegment)
                                    showDialog = false
                                }
                            }
                        ) {
                            Icon(Icons.Default.Star, contentDescription = "Adicionar aos Favoritos")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = { showDialog = false },
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            Text(text = "Fechar")
                        }
                    }
                }
            )
        }
    }
}


@Preview
@Composable
fun MapScreenPreview() {
    MapScreen(navController = rememberNavController())
}


/*
@Composable
fun loadBitmapFromUrl(url: String, context: android.content.Context): ImageBitmap {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(url) {
        val job = coroutineScope.launch {
            val inputStream: InputStream = URL(url).openConnection().getInputStream()
            imageBitmap = BitmapFactory.decodeStream(inputStream).asImageBitmap()
        }
        onDispose {
            job.cancel()
        }
    }

    return imageBitmap ?: ImageBitmap(1, 1)
}
 */
