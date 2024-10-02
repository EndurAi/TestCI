package com.github.se.bootcamp.model.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.*

open class ListToDosViewModel(private val repository: ToDosRepository) : ViewModel() {
  private val todos_ = MutableStateFlow<List<ToDo>>(emptyList())
  val todos: StateFlow<List<ToDo>> = todos_.asStateFlow()

  // Selected todo, i.e the todo for the detail view
  private val selectedTodo_ = MutableStateFlow<ToDo?>(null)
  open val selectedTodo: StateFlow<ToDo?> = selectedTodo_.asStateFlow()

  init {
    repository.init { getToDos() }
  }

  // create factory
  companion object {
    val Factory: ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
          @Suppress("UNCHECKED_CAST")
          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ListToDosViewModel(ToDosRepositoryFirestore(Firebase.firestore)) as T
          }
        }
  }

  /**
   * Generates a new unique ID.
   *
   * @return A new unique ID.
   */
  fun getNewUid(): String {
    return repository.getNewUid()
  }

  /** Gets all ToDo documents. */
  fun getToDos() {
    repository.getToDos(onSuccess = { todos_.value = it }, onFailure = {})
  }

  /**
   * Adds a ToDo document.
   *
   * @param toDo The ToDo document to be added.
   */
  fun addToDo(toDo: ToDo) {
    repository.addToDo(toDo = toDo, onSuccess = { getToDos() }, onFailure = {})
  }

  /**
   * Updates a ToDo document.
   *
   * @param toDo The ToDo document to be updated.
   */
  fun updateToDo(toDo: ToDo) {
    repository.updateToDo(toDo = toDo, onSuccess = { getToDos() }, onFailure = {})
  }

  /**
   * Deletes a ToDo document by its ID.
   *
   * @param id The ID of the ToDo document to be deleted.
   */
  fun deleteToDoById(id: String) {
    repository.deleteToDoById(id = id, onSuccess = { getToDos() }, onFailure = {})
  }

  /**
   * Selects a ToDo document.
   *
   * @param toDo The ToDo document to be selected.
   */
  fun selectToDo(toDo: ToDo) {
    selectedTodo_.value = toDo
  }
}
