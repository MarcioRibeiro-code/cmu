package pt.ipp.estg.cmu.viewmodel

import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    /*fun generateStaticMapUrl(locations: List<Location>): String {
        val apiKey = context.getString(R.string.google_map_api_key)
        val markers =
            locations.joinToString(separator = "|") { "${it.latitude},${it.longitude}" }

        return "https://maps.googleapis.com/maps/api/staticmap" +
                "?center=${locations.firstOrNull()?.latitude},${locations.firstOrNull()?.longitude}" +
                "&zoom=12" +
                "&size=400x400" +
                "&markers=$markers" +
                "&key=$apiKey"
    }*/
}