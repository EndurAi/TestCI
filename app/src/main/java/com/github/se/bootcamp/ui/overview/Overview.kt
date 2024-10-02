package com.github.se.bootcamp.ui.overview

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.bootcamp.model.todo.ListToDosViewModel
import com.github.se.bootcamp.model.todo.ToDo
import com.github.se.bootcamp.model.todo.ToDoStatus
import com.github.se.bootcamp.ui.navigation.BottomNavigationMenu
import com.github.se.bootcamp.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.bootcamp.ui.navigation.NavigationActions
import com.github.se.bootcamp.ui.navigation.Screen
import java.util.Locale

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OverviewScreen(
    listToDosViewModel: ListToDosViewModel = viewModel(factory = ListToDosViewModel.Factory),
    navigationActions: NavigationActions
) {

  val todos = listToDosViewModel.todos.collectAsState()
  Scaffold(
      modifier = Modifier.testTag("overviewScreen"),
      floatingActionButton = {
        FloatingActionButton(
            onClick = { navigationActions.navigateTo(Screen.ADD_TODO) },
            modifier = Modifier.testTag("createTodoFab")) {
              Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
      },
      bottomBar = {
        BottomNavigationMenu(
            onTabSelect = { route -> navigationActions.navigateTo(route) },
            tabList = LIST_TOP_LEVEL_DESTINATION,
            selectedItem = navigationActions.currentRoute())
      },
      content = { pd ->
        if (todos.value.isNotEmpty()) {
          LazyColumn(
              contentPadding = PaddingValues(vertical = 8.dp),
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(pd)) {
                items(todos.value.size) { index ->
                  ToDoItem(todo = todos.value[index]) {
                    listToDosViewModel.selectToDo(todos.value[index])
                    navigationActions.navigateTo(Screen.EDIT_TODO)
                  }
                }
              }
        } else {
          Text(
              modifier = Modifier.padding(pd).testTag("emptyTodoPrompt"),
              text = "You have no ToDo yet.")
        }
      })
}

@Composable
fun ToDoItem(todo: ToDo, onClick: () -> Unit) {
  Card(
      modifier =
          Modifier.testTag("todoListItem")
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clickable(onClick = onClick),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
      // Date and Status Row
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(todo.dueDate.toDate()),
            style = MaterialTheme.typography.bodySmall)

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
              text =
                  when (todo.status) {
                    ToDoStatus.CREATED -> "Created"
                    ToDoStatus.STARTED -> "Started"
                    ToDoStatus.ENDED -> "Ended"
                    ToDoStatus.ARCHIVED -> "Archived"
                  },
              style = MaterialTheme.typography.bodySmall,
              color =
                  when (todo.status) {
                    ToDoStatus.CREATED -> Color.Blue
                    ToDoStatus.STARTED -> Color(0xFFFFA500) // Orange
                    ToDoStatus.ENDED -> Color.Green
                    ToDoStatus.ARCHIVED -> Color.Gray
                  })
          Icon(
              imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Task Name
      Text(
          text = todo.name,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold)

      // Assignee Name
      Text(text = todo.assigneeName, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
  }
}
