package com.github.se.bootcamp.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.bootcamp.model.map.LocationViewModel
import com.github.se.bootcamp.model.todo.ListToDosViewModel
import com.github.se.bootcamp.ui.navigation.BottomNavigationMenu
import com.github.se.bootcamp.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.bootcamp.ui.navigation.NavigationActions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(
    listToDosViewModel: ListToDosViewModel = viewModel(factory = ListToDosViewModel.Factory),
    navigationActions: NavigationActions,
    locationViewModel: LocationViewModel = viewModel(factory = LocationViewModel.Factory)
) {
  Scaffold(
      modifier = Modifier.testTag("mapScreen"),
      bottomBar = {
        BottomNavigationMenu(
            onTabSelect = { route -> navigationActions.navigateTo(route) },
            tabList = LIST_TOP_LEVEL_DESTINATION,
            selectedItem = navigationActions.currentRoute())
      }) { pd ->
        Text("Map Screen", modifier = Modifier.padding(pd))

        val todos = listToDosViewModel.todos.collectAsState()

        val cameraPositionState = rememberCameraPositionState {
          position = CameraPosition.fromLatLngZoom(LatLng(37.7749, -122.4194), 10f) // San Francisco
        }
        GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState) {
          todos.value.forEach { todo ->
            Marker(
                state =
                    rememberMarkerState(
                        position =
                            LatLng(
                                todo.location?.latitude ?: 0.0, todo.location?.longitude ?: 0.0)),
                title = todo.name, // The name of the task will appear as the main title
                snippet = todo.description,
            ) // The description will appear in a secondary line
          }
        }
      }
}
