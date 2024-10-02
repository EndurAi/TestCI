package com.github.se.bootcamp.model.todo

import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import com.github.se.bootcamp.model.map.Location
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import junit.framework.TestCase.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.atMostOnce
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.timeout
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

// ***************************************************************************** //
// ***                                                                       *** //
// *** THIS FILE WILL BE OVERWRITTEN DURING GRADING. IT SHOULD BE LOCATED IN *** //
// *** `app/src/test/java/com/github/se/bootcamp/model/todo/`                *** //
// *** DO **NOT** IMPLEMENT YOUR OWN TESTS IN THIS FILE                      *** //
// ***                                                                       *** //
// ***************************************************************************** //

@RunWith(RobolectricTestRunner::class)
class ToDosRepositoryFirestoreTest {

  @Mock private lateinit var mockFirestore: FirebaseFirestore
  @Mock private lateinit var mockDocumentReference: DocumentReference
  @Mock private lateinit var mockCollectionReference: CollectionReference
  @Mock private lateinit var mockDocumentSnapshot: DocumentSnapshot
  @Mock private lateinit var mockToDoQuerySnapshot: QuerySnapshot

  private lateinit var toDosRepositoryFirestore: ToDosRepositoryFirestore

  private val todo =
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
    MockitoAnnotations.openMocks(this)

    // Initialize Firebase if necessary
    if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
      FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    toDosRepositoryFirestore = ToDosRepositoryFirestore(mockFirestore)

    `when`(mockFirestore.collection(any())).thenReturn(mockCollectionReference)
    `when`(mockCollectionReference.document(any())).thenReturn(mockDocumentReference)
    `when`(mockCollectionReference.document()).thenReturn(mockDocumentReference)
  }

  @Test
  fun getNewUid() {
    `when`(mockDocumentReference.id).thenReturn("1")
    val uid = toDosRepositoryFirestore.getNewUid()
    assert(uid == "1")
  }

  /**
   * This test verifies that when fetching a ToDos list, the Firestore `get()` is called on the
   * collection reference and not the document reference.
   */
  @Test
  fun getToDos_callsDocuments() {
    // Ensure that mockToDoQuerySnapshot is properly initialized and mocked
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockToDoQuerySnapshot))

    // Ensure the QuerySnapshot returns a list of mock DocumentSnapshots
    `when`(mockToDoQuerySnapshot.documents).thenReturn(listOf())

    // Call the method under test
    toDosRepositoryFirestore.getToDos(
        onSuccess = {

          // Do nothing; we just want to verify that the 'documents' field was accessed
        },
        onFailure = { fail("Failure callback should not be called") })

    // Verify that the 'documents' field was accessed
    verify(timeout(100)) { (mockToDoQuerySnapshot).documents }
  }

  /**
   * This test verifies that when we add a new ToDo, the Firestore `set()` or `add` is called on the
   * document reference. This does NOT CHECK the actual data being added
   */
  @Test
  fun addToDo_shouldCallFirestoreCollection() {
    // Simulate a successful Firestore `set` operation
    `when`(mockDocumentReference.set(any())).thenReturn(Tasks.forResult(null))

    // Simulate a successful Firestore `add` operation, which returns a new DocumentReference
    `when`(mockCollectionReference.add(any())).thenReturn(Tasks.forResult(mockDocumentReference))

    // Call the method that adds the ToDo
    toDosRepositoryFirestore.addToDo(todo, onSuccess = {}, onFailure = {})

    // Ensure all asynchronous operations are completed
    shadowOf(Looper.getMainLooper()).idle()

    // Verify that either `set()` or `add()` was called
    verify(mockDocumentReference, atMostOnce()).set(any())
    verify(mockCollectionReference, atMostOnce()).add(any())
  }

  /**
   * This check that the correct Firestore method is called when deleting. Does NOT CHECK that the
   * correct data is deleted.
   */
  @Test
  fun deleteToDoById_shouldCallDocumentReferenceDelete() {
    `when`(mockDocumentReference.delete()).thenReturn(Tasks.forResult(null))

    toDosRepositoryFirestore.deleteToDoById("1", onSuccess = {}, onFailure = {})

    shadowOf(Looper.getMainLooper()).idle() // Ensure all asynchronous operations complete

    verify(mockDocumentReference).delete()
  }
}
