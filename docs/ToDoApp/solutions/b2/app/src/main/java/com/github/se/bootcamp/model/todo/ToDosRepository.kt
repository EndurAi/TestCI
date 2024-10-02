package com.github.se.bootcamp.model.todo

interface ToDosRepository {

  fun getNewUid(): String

  fun init(onSuccess: () -> Unit)

  fun getToDos(onSuccess: (List<ToDo>) -> Unit, onFailure: (Exception) -> Unit)

  fun addToDo(toDo: ToDo, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)

  fun updateToDo(toDo: ToDo, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)

  fun deleteToDoById(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
}
