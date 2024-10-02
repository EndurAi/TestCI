package com.github.se.bootcamp.ui.overview

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.se.bootcamp.model.map.Location
import com.github.se.bootcamp.model.todo.ListToDosViewModel
import com.github.se.bootcamp.model.todo.ToDo
import com.github.se.bootcamp.model.todo.ToDoStatus
import com.github.se.bootcamp.model.todo.ToDosRepository
import com.github.se.bootcamp.ui.navigation.NavigationActions
import com.github.se.bootcamp.ui.navigation.Screen
import com.google.firebase.Timestamp
import java.util.Date
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

// ***************************************************************************** //
// ***                                                                       *** //
// *** THIS FILE WILL BE OVERWRITTEN DURING GRADING. IT SHOULD BE LOCATED IN *** //
// *** `app/src/androidTest/java/com/github/se/bootcamp/ui/overview`.        *** //
// *** DO **NOT** IMPLEMENT YOUR OWN TESTS IN THIS FILE                      *** //
// ***                                                                       *** //
// ***************************************************************************** //

class EditToDoScreenTest {
  private lateinit var toDosRepository: ToDosRepository
  private lateinit var navigationActions: NavigationActions
  private lateinit var listToDosViewModel: ListToDosViewModel

  @get:Rule val composeTestRule = createComposeRule()

  private val todo =
      ToDo(
          "1",
          "First ToDo",
          "Do something",
          "John Doe",
          Timestamp(Date(1725494400000)),
          Location(0.0, 0.0, "EPFL"),
          ToDoStatus.CREATED)

  @Before
  fun setUp() {
    toDosRepository = mock(ToDosRepository::class.java)
    navigationActions = mock(NavigationActions::class.java)
    listToDosViewModel = ListToDosViewModel(toDosRepository)

    `when`(navigationActions.currentRoute()).thenReturn(Screen.EDIT_TODO)
  }

  @Test
  fun displayAllComponents() {
    listToDosViewModel.selectToDo(todo)
    composeTestRule.setContent { EditToDoScreen(listToDosViewModel, navigationActions) }

    composeTestRule.onNodeWithTag("editScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("editTodoTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("editTodoTitle").assertTextEquals("Edit Task")
    composeTestRule.onNodeWithTag("goBackButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("todoSave").assertIsDisplayed()
    composeTestRule.onNodeWithTag("todoSave").assertTextEquals("Save")
    composeTestRule.onNodeWithTag("todoDelete").assertIsDisplayed()
    composeTestRule.onNodeWithTag("todoDelete").assertTextEquals("Delete")

    composeTestRule.onNodeWithTag("inputTodoTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("inputTodoDescription").assertIsDisplayed()
    composeTestRule.onNodeWithTag("inputTodoAssignee").assertIsDisplayed()
    composeTestRule.onNodeWithTag("inputTodoLocation").assertIsDisplayed()
    composeTestRule.onNodeWithTag("inputTodoDate").assertIsDisplayed()
    composeTestRule.onNodeWithTag("inputTodoStatus").assertIsDisplayed()
  }

  @Test
  fun inputsHaveInitialValue() {
    listToDosViewModel.selectToDo(todo)
    composeTestRule.setContent { EditToDoScreen(listToDosViewModel, navigationActions) }

    Thread.sleep(10000)

    composeTestRule.onNodeWithTag("inputTodoTitle").assertTextContains(todo.name)
    composeTestRule.onNodeWithTag("inputTodoDescription").assertTextContains(todo.description)
    composeTestRule.onNodeWithTag("inputTodoAssignee").assertTextContains(todo.assigneeName)
    composeTestRule.onNodeWithTag("inputTodoDate").assertTextContains("5/9/2024")
    composeTestRule.onNodeWithTag("inputTodoStatus").assertTextContains("Created")
  }

  @Test
  fun correctlyIteratesStatuses() {
    listToDosViewModel.selectToDo(todo)
    composeTestRule.setContent { EditToDoScreen(listToDosViewModel, navigationActions) }
    composeTestRule.onNodeWithTag("inputTodoStatus").assertTextContains("Created")

    composeTestRule.onNodeWithTag("inputTodoStatus").performClick()
    composeTestRule.onNodeWithTag("inputTodoStatus").assertTextContains("Started")

    composeTestRule.onNodeWithTag("inputTodoStatus").performClick()
    composeTestRule.onNodeWithTag("inputTodoStatus").assertTextContains("Ended")

    composeTestRule.onNodeWithTag("inputTodoStatus").performClick()
    composeTestRule.onNodeWithTag("inputTodoStatus").assertTextContains("Archived")
  }
}
