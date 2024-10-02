package com.github.se.bootcamp.sigchecks

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlin.properties.Delegates

class MapSignatureChecks private constructor() {
  @Composable
  @SuppressLint("ComposableNaming")
  fun checkLocationViewModel() {
    locationViewModel = com.github.se.bootcamp.model.map.LocationViewModel(locationRepository)

    locationViewModel.setQuery(string)

    locationList = locationViewModel.locationSuggestions.collectAsState(locationList).value

    string = locationViewModel.query.collectAsState().value
  }

  fun checkLocationRepository() {
    locationRepository.search(string, onSuccess, onFailure)
  }

  fun checkNominatimLocationRepository() {
    nominatimLocationRepository =
        com.github.se.bootcamp.model.map.NominatimLocationRepository(httpClient)
  }

  @Composable
  @SuppressLint("ComposableNaming")
  fun checkAddAndEditScreens() {
    // AddToDoScreen and EditToDoScreen should take a LocationViewModel as parameter.
    // To avoid issues with the previous Signature Checks, it must be optional (i.e. have a default
    // value).

    com.github.se.bootcamp.ui.overview.AddToDoScreen(
        listToDosViewModel, navigationActions, locationViewModel)
    com.github.se.bootcamp.ui.overview.EditToDoScreen(
        listToDosViewModel, navigationActions, locationViewModel)
  }

  // String used to query the repository
  private var string by Delegates.notNull<String>()

  // Http client given to NominatimLocationRepository
  private var httpClient by Delegates.notNull<okhttp3.OkHttpClient>()

  private var locationViewModel by
      Delegates.notNull<com.github.se.bootcamp.model.map.LocationViewModel>()

  private var locationRepository by
      Delegates.notNull<com.github.se.bootcamp.model.map.LocationRepository>()

  private var nominatimLocationRepository by
      Delegates.notNull<com.github.se.bootcamp.model.map.NominatimLocationRepository>()

  private var locationList by Delegates.notNull<List<com.github.se.bootcamp.model.map.Location>>()

  private val listToDosViewModel by
      Delegates.notNull<com.github.se.bootcamp.model.todo.ListToDosViewModel>()

  private val navigationActions by
      Delegates.notNull<com.github.se.bootcamp.ui.navigation.NavigationActions>()

  private var onSuccess by
      Delegates.notNull<(List<com.github.se.bootcamp.model.map.Location>) -> Unit>()

  private var onFailure by Delegates.notNull<(Exception) -> Unit>()
}
