package com.github.se.bootcamp.model.todo

import com.github.se.bootcamp.model.map.Location
import com.google.firebase.Timestamp
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

// ***************************************************************************** //
// ***                                                                       *** //
// *** THIS FILE WILL BE OVERWRITTEN DURING GRADING. IT SHOULD BE LOCATED IN *** //
// *** `app/src/test/java/com/github/se/bootcamp/model/todo/`                *** //
// *** DO **NOT** IMPLEMENT YOUR OWN TESTS IN THIS FILE                      *** //
// ***                                                                       *** //
// ***************************************************************************** //

class ListToDosViewModelTest {
  private lateinit var toDosRepository: ToDosRepository
  private lateinit var listToDosViewModel: ListToDosViewModel

  val todo =
      ToDo(
          name = "Todo",
          uid = "1",
          status = ToDoStatus.CREATED,
          location = Location(name = "EPFL", latitude = 0.0, longitude = 0.0),
          dueDate = Timestamp.now(),
          assigneeName = "me",
          description = "Do something")

  @Before
  fun setUp() {
    toDosRepository = mock(ToDosRepository::class.java)
    listToDosViewModel = ListToDosViewModel(toDosRepository)
  }

  @Test
  fun getNewUid() {
    `when`(toDosRepository.getNewUid()).thenReturn("uid")
    assertThat(listToDosViewModel.getNewUid(), `is`("uid"))
  }

  @Test
  fun getTodosCallsRepository() {
    listToDosViewModel.getToDos()
    verify(toDosRepository).getToDos(any(), any())
  }

  @Test
  fun addToDoCallsRepository() {
    listToDosViewModel.addToDo(todo)
    verify(toDosRepository).addToDo(eq(todo), any(), any())
  }
}
