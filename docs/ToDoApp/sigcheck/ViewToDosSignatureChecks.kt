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
class ViewToDosSignatureChecks private constructor() {
  /** Check that the NavigationActions class is fully open, for testing purposes. */
  class FakeNavigationAction(navHostController: androidx.navigation.NavHostController) :
      com.github.se.bootcamp.ui.navigation.NavigationActions(navHostController) {

    override fun navigateTo(
        destination: com.github.se.bootcamp.ui.navigation.TopLevelDestination
    ) {}

    override fun navigateTo(screen: String) {}

    override fun goBack() {}

    override fun currentRoute(): String {
      return ""
    }
  }

  /**
   * Validates composable functions intended to represent the main screen of the application. It
   * utilizes the ToDo's ViewModel for managing and presenting todos.
   */
  @SuppressLint("ComposableNaming")
  @Composable
  fun checkMainComposable() {
    /**
     * Overview is a Jetpack compose `@Composable` representing the overview screen and displaying:
     * - the list of todos if there is at least one that exists
     * - some text stating that there is no todos created if the user never created any todos or
     *   deleted all of them
     *
     * takes two arguments, namely:
     * - listToDosViewModel: `com.github.se.bootcamp.model.todo.ListToDosViewModel`
     * - navigationActions: the `NavigationActions` used to navigate around the app
     */
    com.github.se.bootcamp.ui.overview.OverviewScreen(
        listToDosViewModel,
        navigationActions,
    )
  }

  /* Detailed documentation for each property, explaining its purpose and usage */

  /* ---------------------------------------------------
  -----------  UI RELATED CLASSED/OBJECTS  ----------
  --------------------------------------------------- */

  // ViewModel for the overview screen
  private val listToDosViewModel by
      Delegates.notNull<com.github.se.bootcamp.model.todo.ListToDosViewModel>()

  // Navigation actions
  private var navigationActions by
      Delegates.notNull<com.github.se.bootcamp.ui.navigation.NavigationActions>()
}
