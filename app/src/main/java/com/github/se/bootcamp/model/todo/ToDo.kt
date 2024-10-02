package com.github.se.bootcamp.model.todo

import com.github.se.bootcamp.model.map.Location
import com.google.firebase.Timestamp

data class ToDo(
    val uid: String,
    val name: String,
    val description: String,
    val assigneeName: String,
    val dueDate: Timestamp,
    val location: Location?,
    val status: ToDoStatus,
)

enum class ToDoStatus {
  CREATED,
  STARTED,
  ENDED,
  ARCHIVED
}
