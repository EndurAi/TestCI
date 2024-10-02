package com.github.se.bootcamp.ui.overview

import android.icu.util.GregorianCalendar
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToDoScreen(
    listToDosViewModel: ListToDosViewModel = viewModel(factory = ListToDosViewModel.Factory),
    navigationActions: NavigationActions,
    locationViewModel: LocationViewModel = viewModel(factory = LocationViewModel.Factory)
) {
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var assignee by remember { mutableStateOf("") }
  var selectedLocation by remember { mutableStateOf<Location?>(null) }
  var locationQuery by remember { mutableStateOf("") }
  var dueDate by remember { mutableStateOf("") }
  var isDropdownExpanded by remember { mutableStateOf(true) }

  val context = LocalContext.current
  val locationSuggestions by locationViewModel.locationSuggestions.collectAsState()

  Scaffold(
      modifier = Modifier.testTag("addScreen"),
      topBar = {
        TopAppBar(
            title = { Text("Create a new task", Modifier.testTag("addTodoTitle")) },
            navigationIcon = {
              IconButton(
                  onClick = { navigationActions.goBack() }, Modifier.testTag("goBackButton")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back")
                  }
            })
      },
      content = { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
              // Title Input
              OutlinedTextField(
                  value = title,
                  onValueChange = { title = it },
                  label = { Text("Title") },
                  placeholder = { Text("Name the task") },
                  modifier = Modifier.fillMaxWidth().testTag("inputTodoTitle"))

              // Description Input
              OutlinedTextField(
                  value = description,
                  onValueChange = { description = it },
                  label = { Text("Description") },
                  placeholder = { Text("Describe the task") },
                  modifier = Modifier.fillMaxWidth().height(200.dp).testTag("inputTodoDescription"))

              // Assignee Input
              OutlinedTextField(
                  value = assignee,
                  onValueChange = { assignee = it },
                  label = { Text("Assignee") },
                  placeholder = { Text("Assign a person") },
                  modifier = Modifier.fillMaxWidth().testTag("inputTodoAssignee"))

              // Location Input
              OutlinedTextField(
                  value = locationQuery,
                  onValueChange = {
                    locationQuery = it
                    isDropdownExpanded = true
                    locationViewModel.setQuery(it)
                  },
                  label = { Text("Location") },
                  placeholder = { Text("Search for a location") },
                  modifier = Modifier.fillMaxWidth().testTag("inputTodoLocation"),
                  keyboardOptions =
                      KeyboardOptions.Default.copy(
                          imeAction = ImeAction.Search // Set the action button to Search (Loupe)
                          ),
              )

              // Dropdown Menu only shows when there are suggestions
              if (isDropdownExpanded && locationSuggestions.isNotEmpty()) {
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier.fillMaxWidth()) {
                      locationSuggestions.take(5).forEach { location -> // Limiting to 5 items
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

                        val loc = selectedLocation

                        listToDosViewModel.addToDo(
                            ToDo(
                                name = title,
                                description = description,
                                assigneeName = assignee,
                                dueDate = Timestamp(calendar.time),
                                location = loc,
                                status = ToDoStatus.CREATED,
                                uid = listToDosViewModel.getNewUid()))
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
            }
      })
}
