package com.github.se.bootcamp.ui.overview

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.bootcamp.model.map.Location
import com.github.se.bootcamp.model.map.LocationViewModel
import com.github.se.bootcamp.model.todo.ListToDosViewModel
import com.github.se.bootcamp.model.todo.ToDo
import com.github.se.bootcamp.model.todo.ToDoStatus
import com.github.se.bootcamp.ui.navigation.NavigationActions
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditToDoScreen(
    listToDosViewModel: ListToDosViewModel = viewModel(factory = ListToDosViewModel.Factory),
    navigationActions: NavigationActions,
    locationViewModel: LocationViewModel = viewModel(factory = LocationViewModel.Factory)
) {
  val task =
      listToDosViewModel.selectedTodo.collectAsState().value
          ?: return Text(text = "No ToDo selected. Should not happen", color = Color.Red)

  var title by remember { mutableStateOf(task.name) }
  var description by remember { mutableStateOf(task.description) }
  var assignee by remember { mutableStateOf(task.assigneeName) }
  var locationQuery by remember { mutableStateOf(task.location?.name ?: "") }
  var selectedLocation by remember { mutableStateOf<Location?>(task.location) }
  var dueDate by remember {
    mutableStateOf(
        task.dueDate.let {
          val calendar = GregorianCalendar()
          calendar.time = task.dueDate.toDate()
          return@let "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
        })
  }
  var status by remember { mutableStateOf(task.status) }
  var isDropdownExpanded by remember { mutableStateOf(false) }

  val context = LocalContext.current
  val locationSuggestions by locationViewModel.locationSuggestions.collectAsState()

  Scaffold(
      modifier = Modifier.testTag("editScreen"),
      topBar = {
        TopAppBar(
            title = { Text("Edit Task", modifier = Modifier.testTag("editTodoTitle")) },
            navigationIcon = {
              IconButton(
                  modifier = Modifier.testTag("goBackButton"),
                  onClick = { navigationActions.goBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back")
                  }
            })
      },
      content = { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
              // Title Input
              OutlinedTextField(
                  value = title,
                  onValueChange = { title = it },
                  label = { Text("Title") },
                  placeholder = { Text("Task Title") },
                  modifier = Modifier.fillMaxWidth().testTag("inputTodoTitle"))

              // Description Input
              OutlinedTextField(
                  value = description,
                  onValueChange = { description = it },
                  label = { Text("Description") },
                  placeholder = { Text("Describe the task") },
                  modifier = Modifier.fillMaxWidth().height(100.dp).testTag("inputTodoDescription"))

              // Assignee Input
              OutlinedTextField(
                  value = assignee,
                  onValueChange = { assignee = it },
                  label = { Text("Assignee") },
                  placeholder = { Text("Assign a person") },
                  modifier = Modifier.fillMaxWidth().testTag("inputTodoAssignee"))

              // Location Input with suggestions
              OutlinedTextField(
                  value = locationQuery,
                  onValueChange = {
                    locationViewModel.setQuery(it)
                    isDropdownExpanded = true
                    locationQuery = it
                  },
                  label = { Text("Location") },
                  placeholder = { Text("Search for a location") },
                  modifier = Modifier.fillMaxWidth().testTag("inputTodoLocation"),
                  keyboardOptions =
                      KeyboardOptions.Default.copy(
                          imeAction = ImeAction.Search // Set the action button to Search (Loupe)
                          ),
                  keyboardActions =
                      KeyboardActions(
                          onSearch = {
                            locationViewModel.setQuery(locationQuery)
                            isDropdownExpanded = locationSuggestions.isNotEmpty()
                          }))

              // Dropdown Menu for location suggestions
              if (isDropdownExpanded && locationSuggestions.isNotEmpty()) {
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier.fillMaxWidth()) {
                      locationSuggestions.take(5).forEach { location ->
                        DropdownMenuItem(
                            text = { Text(location.name) },
                            modifier = Modifier.testTag("itemTodoResult"),
                            onClick = {
                              selectedLocation = location
                              locationQuery = location.name
                              isDropdownExpanded = false
                            })
                      }
                    }
              }

              // Due Date Input
              OutlinedTextField(
                  value = dueDate,
                  onValueChange = { dueDate = it },
                  label = { Text("Due date") },
                  placeholder = { Text("01/01/1970") },
                  modifier = Modifier.fillMaxWidth().testTag("inputTodoDate"))

              Button(
                  onClick = {
                    // Update status to the next enum value
                    status = getNextStatus(status)
                  },
                  modifier =
                      Modifier.fillMaxWidth().padding(vertical = 8.dp).testTag("inputTodoStatus")) {
                    Text(
                        text =
                            status.name.replace("_", " ").lowercase(Locale.ROOT).replaceFirstChar {
                              if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                              else it.toString()
                            })
                  }

              Spacer(modifier = Modifier.height(16.dp))

              // Save Button
              Button(
                  onClick = {
                    val calendar = GregorianCalendar()
                    val parts = dueDate.split("/")
                    if (parts.size == 3) {
                      try {
                        calendar.set(
                            parts[2].toInt(),
                            parts[1].toInt() - 1, // Months are 0-based
                            parts[0].toInt(),
                            0,
                            0,
                            0)

                        listToDosViewModel.updateToDo(
                            ToDo(
                                uid = task.uid,
                                name = title,
                                description = description,
                                assigneeName = assignee,
                                dueDate = Timestamp(calendar.time),
                                status = status,
                                location = selectedLocation))
                        navigationActions.goBack()

                        return@Button
                      } catch (_: NumberFormatException) {}
                    }

                    Toast.makeText(
                            context, "Invalid format, date must be DD/MM/YYYY.", Toast.LENGTH_SHORT)
                        .show()
                  },
                  modifier = Modifier.fillMaxWidth().testTag("todoSave"),
                  enabled = title.isNotBlank()) {
                    Text("Save")
                  }

              Spacer(modifier = Modifier.height(8.dp))

              // Delete Button
              Button(
                  onClick = {
                    listToDosViewModel.deleteToDoById(task.uid)
                    navigationActions.goBack()
                  },
                  modifier = Modifier.fillMaxWidth().testTag("todoDelete")) {
                    Text("Delete", color = Color.White)
                  }
            }
      })
}

// Function to get the next status in the enum sequence
fun getNextStatus(currentStatus: ToDoStatus): ToDoStatus {
  return when (currentStatus) {
    ToDoStatus.CREATED -> ToDoStatus.STARTED
    ToDoStatus.STARTED -> ToDoStatus.ENDED
    ToDoStatus.ENDED -> ToDoStatus.ARCHIVED
    ToDoStatus.ARCHIVED -> ToDoStatus.CREATED
  }
}
