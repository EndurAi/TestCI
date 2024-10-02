package com.github.se.bootcamp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.github.se.bootcamp.model.todo.ListToDosViewModel
import com.github.se.bootcamp.ui.authentication.SignInScreen
import com.github.se.bootcamp.ui.map.MapScreen
import com.github.se.bootcamp.ui.navigation.NavigationActions
import com.github.se.bootcamp.ui.navigation.Route
import com.github.se.bootcamp.ui.navigation.Screen
import com.github.se.bootcamp.ui.overview.AddToDoScreen
import com.github.se.bootcamp.ui.overview.EditToDoScreen
import com.github.se.bootcamp.ui.overview.OverviewScreen
import com.github.se.bootcamp.ui.theme.BootcampTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

  private lateinit var auth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Initialize Firebase Auth
    auth = FirebaseAuth.getInstance()
    auth.currentUser?.let {
      // Sign out the user if they are already signed in
      // This is useful for testing purposes
      auth.signOut()
    }

    setContent { BootcampTheme { Surface(modifier = Modifier.fillMaxSize()) { BootcampApp() } } }
  }
}

@Composable
fun BootcampApp() {
  val navController = rememberNavController()
  val navigationActions = NavigationActions(navController)

  val listToDosViewModel: ListToDosViewModel = viewModel(factory = ListToDosViewModel.Factory)

  NavHost(navController = navController, startDestination = Route.AUTH) {
    navigation(
        startDestination = Screen.AUTH,
        route = Route.AUTH,
    ) {
      composable(Screen.AUTH) { SignInScreen(navigationActions) }
    }

    navigation(
        startDestination = Screen.OVERVIEW,
        route = Route.OVERVIEW,
    ) {
      composable(Screen.OVERVIEW) { OverviewScreen(listToDosViewModel, navigationActions) }
      composable(Screen.ADD_TODO) { AddToDoScreen(listToDosViewModel, navigationActions) }
      composable(Screen.EDIT_TODO) { EditToDoScreen(listToDosViewModel, navigationActions) }
    }

    navigation(
        startDestination = Screen.MAP,
        route = Route.MAP,
    ) {
      composable(Screen.MAP) { MapScreen(navigationActions) }
    }
  }
}
