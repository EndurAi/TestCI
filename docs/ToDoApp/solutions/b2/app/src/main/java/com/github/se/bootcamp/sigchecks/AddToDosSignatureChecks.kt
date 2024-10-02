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
class AddToDosSignatureChecks private constructor() {
  /** Validates the structure and initialization of a Location model object. */
  fun checkLocation() {
    todoLocation =
        com.github.se.bootcamp.model.map.Location(
            latitude,
            longitude,
            locationName,
        )
  }

  /** Validates the structure and initialization of a ToDo model object. */
  fun checkToDo() {
    todo =
        com.github.se.bootcamp.model.todo.ToDo(
            todoUID,
            todoName,
            todoDescription,
            todoAssignee,
            todoDueDate,
            todoLocation,
            todoStatus,
        )
  }

  /**
   * Validates the enumeration values of ToDoStatus to ensure they cover all expected todo states
   */
  fun checkToDoStatus() {
    todoStatusCreated = com.github.se.bootcamp.model.todo.ToDoStatus.CREATED
    todoStatusStarted = com.github.se.bootcamp.model.todo.ToDoStatus.STARTED
    todoStatusEnded = com.github.se.bootcamp.model.todo.ToDoStatus.ENDED
    todoStatusArchived = com.github.se.bootcamp.model.todo.ToDoStatus.ARCHIVED
  }

  fun checkToDoRepository() {
    todoUID = todosRepository.getNewUid()
    todosRepository.init {}
    todosRepository.getToDos({ todoList = it }, { exception = it })
    todosRepository.addToDo(todo, {}, { exception = it })
    todosRepository.updateToDo(todo, {}, { exception = it })
    todosRepository.deleteToDoById(todoUID, {}, { exception = it })
  }

  fun checkToDoRepositoryFirestore() {
    todosRepositoryFirestore = com.github.se.bootcamp.model.todo.ToDosRepositoryFirestore(firestore)
    todosRepository = todosRepositoryFirestore
  }

  /** Validates the structure of an com.github.se.bootcamp.model.todo.ListToDosViewModel */
  fun checkListToDosViewModel() {
    /**
     * An `com.github.se.bootcamp.model.todo.ListToDosViewModel` represents the list of todos in the
     * app. It does not accept any argument. Feel free to design and implement this view model in
     * whichever manner you find most suitable
     */
    com.github.se.bootcamp.model.todo.ListToDosViewModel(todosRepository)
  }

  /**
   * Validates composable functions intended to represent the main screen of the application. It
   * utilizes the ToDo's ViewModel for managing and presenting todos.
   */
  @SuppressLint("ComposableNaming")
  @Composable
  fun checkAddToDoComposable() {
    /**
     * AddToDoScreen is a Jetpack compose `@Composable` representing the screen where user can
     * create new todos. It should feature:
     * - An input for each ToDo attribute
     * - A confirm button
     * - A button to go back to the overview screen
     *
     * takes two arguments, namely:
     * - listToDosViewModel: `com.github.se.bootcamp.model.todo.ListToDosViewModel`
     * - navigationActions: the `NavigationActions` used to navigate around the app
     */
    com.github.se.bootcamp.ui.overview.AddToDoScreen(
        listToDosViewModel,
        navigationActions,
    )
  }

  fun checkRoutes() {
    overviewRoute = com.github.se.bootcamp.ui.navigation.Route.OVERVIEW
    mapRoute = com.github.se.bootcamp.ui.navigation.Route.MAP
    authRoute = com.github.se.bootcamp.ui.navigation.Route.AUTH
  }

  fun checkScreens() {
    authScreen = com.github.se.bootcamp.ui.navigation.Screen.AUTH
    overviewScreen = com.github.se.bootcamp.ui.navigation.Screen.OVERVIEW
    mapScreen = com.github.se.bootcamp.ui.navigation.Screen.MAP
    addScreen = com.github.se.bootcamp.ui.navigation.Screen.ADD_TODO
    editScreen = com.github.se.bootcamp.ui.navigation.Screen.EDIT_TODO
  }

  fun checkTopLevelDestinations() {
    overviewTopLevelDestination = com.github.se.bootcamp.ui.navigation.TopLevelDestinations.OVERVIEW
    mapTopLevelDestination = com.github.se.bootcamp.ui.navigation.TopLevelDestinations.MAP

    topLevelDestination =
        com.github.se.bootcamp.ui.navigation.TopLevelDestination("", imageVector, "")
  }

  /** Validates the structure of the navigation actions */
  fun checkNavigationActions() {
    /** Instantiate a `NavigationActions` from a `NavHostController */
    navigationActions = com.github.se.bootcamp.ui.navigation.NavigationActions(navHostController)

    /**
     * The `navigateTo` method enables navigation to any of the top-level destinations specified in
     * `TOP_LEVEL_DESTINATIONS`
     *
     * Hint: You can refer to the Reply sample app linked in the documentation (under
     * "2-ListOfTodos.md) to aid in implementing this feature effectively
     */
    navigationActions.navigateTo(topLevelDestination)

    /**
     * The `navigateTo` method enables navigation to any of the screen specified in `Screen`
     *
     * Hint: You can refer to the Reply sample app linked in the documentation (under
     * "2-ListOfTodos.md) to aid in implementing this feature effectively
     */
    navigationActions.navigateTo(authScreen)

    /**
     * The `goBack` method takes zero argument and facilitates simple navigation by reverting to the
     * previous screen with a single command. For instance, invoking `goBack` while the user is on
     * the CreateToDo or EditToDo screen transitions them back to the Overview.
     *
     * Hint: this method can easily be implemented as a one-liner
     */
    navigationActions.goBack()
  }

  /* Detailed documentation for each property, explaining its purpose and usage */

  /* ---------------------------------------------------
  ---------  TODOS RELATED CLASSED/OBJECTS  ---------
  --------------------------------------------------- */

  /* ToDos related */

  // Represents a single todo
  private var todo by Delegates.notNull<com.github.se.bootcamp.model.todo.ToDo>()

  // Represents a list of todos
  private var todoList by Delegates.notNull<List<com.github.se.bootcamp.model.todo.ToDo>>()

  // The UID (unique id) of a todo
  private var todoUID by Delegates.notNull<String>()

  // The name of a todo
  private val todoName by Delegates.notNull<String>()

  // The assignee name for a todo
  private val todoAssignee by Delegates.notNull<String>()

  // The detailed description of a todo
  private val todoDescription by Delegates.notNull<String>()

  // The due date (deadline) of the todo
  private val todoDueDate by Delegates.notNull<com.google.firebase.Timestamp>()

  // Represents the geographical location of a todo
  private var todoLocation by Delegates.notNull<com.github.se.bootcamp.model.map.Location>()

  // Latitude of the location
  private val latitude by Delegates.notNull<Double>()

  // Longitude of the location
  private val longitude by Delegates.notNull<Double>()

  // A descriptive name representing the location
  private val locationName by Delegates.notNull<String>()

  // The current status of the todo
  private val todoStatus by Delegates.notNull<com.github.se.bootcamp.model.todo.ToDoStatus>()

  // Status indicating the todo was created
  private var todoStatusCreated by Delegates.notNull<com.github.se.bootcamp.model.todo.ToDoStatus>()

  // Status indicating the todo was started.
  private var todoStatusStarted by Delegates.notNull<com.github.se.bootcamp.model.todo.ToDoStatus>()

  // Status indicating the todo ended.
  private var todoStatusEnded by Delegates.notNull<com.github.se.bootcamp.model.todo.ToDoStatus>()

  // Status indicating the todo archive.
  private var todoStatusArchived by
      Delegates.notNull<com.github.se.bootcamp.model.todo.ToDoStatus>()

  // Repository interface storing the todos.
  private var todosRepository by
      Delegates.notNull<com.github.se.bootcamp.model.todo.ToDosRepository>()

  // Repository storing the todos.
  private var todosRepositoryFirestore by
      Delegates.notNull<com.github.se.bootcamp.model.todo.ToDosRepositoryFirestore>()

  // Database.
  private var firestore by Delegates.notNull<com.google.firebase.firestore.FirebaseFirestore>()

  // ViewModel for the AddToDo screen
  private val listToDosViewModel by
      Delegates.notNull<com.github.se.bootcamp.model.todo.ListToDosViewModel>()

  /* ---------------------------------------------------
  -----------  UI RELATED CLASSED/OBJECTS  ----------
  --------------------------------------------------- */

  // Image displayed for top level destinations
  private val imageVector by Delegates.notNull<androidx.compose.ui.graphics.vector.ImageVector>()

  // Represents one of main destination in the app (e.g. Overview / Map)
  private var topLevelDestination by
      Delegates.notNull<com.github.se.bootcamp.ui.navigation.TopLevelDestination>()

  // Navigation Controller for the Navigation actions
  private val navHostController by Delegates.notNull<androidx.navigation.NavHostController>()

  // Navigation actions
  private var navigationActions by
      Delegates.notNull<com.github.se.bootcamp.ui.navigation.NavigationActions>()

  // Route to the overview screen
  private var overviewRoute by Delegates.notNull<String>()

  // Route to the Map screen
  private var mapRoute by Delegates.notNull<String>()

  // Route to the authentication screen
  private var authRoute by Delegates.notNull<String>()

  // Authentication screen
  private var authScreen by Delegates.notNull<String>()

  // Overview screen
  private var overviewScreen by Delegates.notNull<String>()

  // Map screen
  private var mapScreen by Delegates.notNull<String>()

  // Add ToDo screen
  private var addScreen by Delegates.notNull<String>()

  // Edit ToDo screen
  private var editScreen by Delegates.notNull<String>()

  // Overview `TopLevelDestination`
  private var overviewTopLevelDestination by
      Delegates.notNull<com.github.se.bootcamp.ui.navigation.TopLevelDestination>()

  // Map `TopLevelDestination`
  private var mapTopLevelDestination by
      Delegates.notNull<com.github.se.bootcamp.ui.navigation.TopLevelDestination>()

  /* ---------------------------------------------------
  ------------------------  MISC -----------------------
  --------------------------------------------------- */

  private var exception by Delegates.notNull<Exception>()
}
