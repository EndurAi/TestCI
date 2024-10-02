package com.github.se.bootcamp.model.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient

open class LocationViewModel(val repository: LocationRepository) : ViewModel() {

  companion object {
    val Factory: ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
          @Suppress("UNCHECKED_CAST")
          override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return LocationViewModel(NominatimLocationRepository(OkHttpClient())) as T
          }
        }
  }

  val TAG = "LocationViewModel"

  private val locationSuggestions_ = MutableStateFlow<List<Location>>(emptyList())
  val locationSuggestions: StateFlow<List<Location>> = locationSuggestions_.asStateFlow()

  private val query_ = MutableStateFlow<String>("")
  val query: StateFlow<String> = query_.asStateFlow()

  fun setQuery(newQuery: String) {
    query_.value = newQuery
    Log.d("Nomina", "changed")

    repository.search(
        query = query_.value,
        onSuccess = {
          locationSuggestions_.value = it
          Log.d("Nomina", "Success ,size: ${it.size}")
          it.forEach { loc -> Log.d("Nomina", "${loc.name} ${loc.latitude} ${loc.longitude}") }
        },
        onFailure = { e -> Log.d("Nomina", "Error : $e") })
  }
}
