package com.github.se.bootcamp.ui.overview

// ***************************************************************************** //
// ***                                                                       *** //
// *** THIS FILE WILL BE OVERWRITTEN DURING GRADING. IT SHOULD BE LOCATED IN *** //
// *** `app/src/androidTest/java/com/github/se/bootcamp/ui/overview`.        *** //
// *** DO **NOT** IMPLEMENT YOUR OWN TESTS IN THIS FILE                      *** //
// ***                                                                       *** //
// ***************************************************************************** //

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.se.bootcamp.model.todo.ListToDosViewModel
import com.github.se.bootcamp.model.todo.ToDo
import com.github.se.bootcamp.model.todo.ToDosRepository
import com.github.se.bootcamp.ui.navigation.NavigationActions
import com.github.se.bootcamp.ui.navigation.Route
import com.github.se.bootcamp.ui.navigation.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

class OverviewScreenTest {
  private lateinit var toDosRepository: ToDosRepository
  private lateinit var navigationActions: NavigationActions
  private lateinit var listToDosViewModel: ListToDosViewModel

  @get:Rule val composeTestRule = createComposeRule()

  @Before
  fun setUp() {
    toDosRepository = mock(ToDosRepository::class.java)
    navigationActions = mock(NavigationActions::class.java)
    listToDosViewModel = ListToDosViewModel(toDosRepository)

    `when`(navigationActions.currentRoute()).thenReturn(Route.OVERVIEW)
    composeTestRule.setContent { OverviewScreen(listToDosViewModel, navigationActions) }
  }

  @Test
  fun displayTextWhenEmpty() {
    `when`(toDosRepository.getToDos(any(), any())).then {
      it.getArgument<(List<ToDo>) -> Unit>(0)(listOf())
    }
    listToDosViewModel.getToDos()

    composeTestRule.onNodeWithTag("emptyTodoPrompt").assertIsDisplayed()
  }

  @Test
  fun hasRequiredComponents() {
    composeTestRule.onNodeWithTag("overviewScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("bottomNavigationMenu").assertIsDisplayed()
    composeTestRule.onNodeWithTag("createTodoFab").assertIsDisplayed()
  }

  @Test
  fun createTodoButtonCallsNavActions() {
    composeTestRule.onNodeWithTag("overviewScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("createTodoFab").assertIsDisplayed()
    composeTestRule.onNodeWithTag("createTodoFab").performClick()
    verify(navigationActions).navigateTo(screen = Screen.ADD_TODO)
  }
}
