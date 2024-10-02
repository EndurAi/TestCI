package com.github.se.bootcamp.sigchecks

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import kotlin.properties.Delegates

// ************************************************************************* //
// ******                                                             ****** //
// ******  THIS FILE SHOULD NOT BE MODIFIED. IT SHOULD BE LOCATED IN  ****** //
// ******  `app/src/main/java/com/github/se/bootcamp/sigchecks`.      ****** //
// ******  DO **NOT** CHANGE ANY SIGNATURE IN THIS FILE               ****** //
// ******                                                             ****** //
// ************************************************************************* //

/**
 * SignatureChecks is a utility class designed for ensuring the consistency and correctness of the
 * app's architecture and data models. It's structured to validate the implementation of the main
 * components used within the Bootcamp's ToDo app. This class is intended for educational purposes,
 * providing a blueprint for students to understand and implement the required components and their
 * interactions within a Jetpack Compose single activity application. You can add more parameters to
 * the classes and methods as long as the following signature checks are correct (e.g. adding an
 * optional parameter).
 */
class EditToDosSignatureChecks private constructor() {
  /**
   * Validates composable functions intended to represent the main screen of the application. It
   * utilizes the ToDo's ViewModel for managing and presenting todos.
   */
  @SuppressLint("ComposableNaming")
  @Composable
  fun checkMainComposable() {
    /**
     * EditToDoScreen is a Jetpack compose `@Composable` representing the screen where users can
     * edit todos. It should feature:
     * - An input for each ToDo attribute
     * - A confirm button
     * - A button to go back to the overview screen
     *
     * takes two arguments, namely:
     * - listToDosViewModel: `com.github.se.bootcamp.model.todo.ListToDosViewModel`
     * - navigationActions: the `NavigationActions` used to navigate around the app
     */
    com.github.se.bootcamp.ui.overview.EditToDoScreen(
        listToDosViewModel,
        navigationActions,
    )
  }

  fun selectToDos() {
    listToDosViewModel.selectToDo(todo)
    todo = listToDosViewModel.selectedTodo.value!!
  }

  // ViewModel for the EditToDo screen
  private val listToDosViewModel by
      Delegates.notNull<com.github.se.bootcamp.model.todo.ListToDosViewModel>()

  // Navigation actions
  private val navigationActions by
      Delegates.notNull<com.github.se.bootcamp.ui.navigation.NavigationActions>()

  // Represents a single todo
  private var todo by Delegates.notNull<com.github.se.bootcamp.model.todo.ToDo>()
}
