package com.github.se.bootcamp.model.todo

import android.util.Log
import com.github.se.bootcamp.model.map.Location
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ToDosRepositoryFirestore(private val db: FirebaseFirestore) : ToDosRepository {

  private val collectionPath = "todos"

  override fun getNewUid(): String {
    return db.collection(collectionPath).document().id
  }

  // Clearly ask TODO it
  override fun init(onSuccess: () -> Unit) {
    Firebase.auth.addAuthStateListener {
      if (it.currentUser != null) {
        onSuccess()
      }
    }
  }

  override fun getToDos(onSuccess: (List<ToDo>) -> Unit, onFailure: (Exception) -> Unit) {
    Log.d("TodosRepositoryFirestore", "getToDos")
    db.collection(collectionPath).get().addOnCompleteListener { task ->
      if (task.isSuccessful) {
        val todos = task.result?.mapNotNull { document -> documentToToDo(document) } ?: emptyList()
        onSuccess(todos)
      } else {
        task.exception?.let { e ->
          Log.e("TodosRepositoryFirestore", "Error getting documents", e)
          onFailure(e)
        }
      }
    }
  }

  override fun addToDo(toDo: ToDo, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    performFirestoreOperation(
        db.collection(collectionPath).document(toDo.uid).set(toDo), onSuccess, onFailure)
  }

  override fun updateToDo(toDo: ToDo, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    performFirestoreOperation(
        db.collection(collectionPath).document(toDo.uid).set(toDo), onSuccess, onFailure)
  }

  override fun deleteToDoById(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    performFirestoreOperation(
        db.collection(collectionPath).document(id).delete(), onSuccess, onFailure)
  }

  /**
   * Performs a Firestore operation and calls the appropriate callback based on the result.
   *
   * @param task The Firestore task to perform.
   * @param onSuccess The callback to call if the operation is successful.
   * @param onFailure The callback to call if the operation fails.
   */
  private fun performFirestoreOperation(
      task: Task<Void>,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    task.addOnCompleteListener { result ->
      if (result.isSuccessful) {
        onSuccess()
      } else {
        result.exception?.let { e ->
          Log.e("TodosRepositoryFirestore", "Error performing Firestore operation", e)
          onFailure(e)
        }
      }
    }
  }

  /**
   * Converts a Firestore document to a ToDo object.
   *
   * @param document The Firestore document to convert.
   * @return The ToDo object.
   */
  private fun documentToToDo(document: DocumentSnapshot): ToDo? {
    return try {
      val uid = document.id
      val name = document.getString("name") ?: return null
      val description = document.getString("description") ?: return null
      val assigneeName = document.getString("assigneeName") ?: return null
      val dueDate = document.getTimestamp("dueDate") ?: return null
      val locationData = document.get("location") as? Map<*, *>
      val location =
          locationData?.let {
            Location(
                latitude = it["latitude"] as? Double ?: 0.0,
                longitude = it["longitude"] as? Double ?: 0.0,
                name = it["name"] as? String ?: "")
          }
      val statusString = document.getString("status") ?: return null
      val status = ToDoStatus.valueOf(statusString)

      ToDo(
          uid = uid,
          name = name,
          assigneeName = assigneeName,
          dueDate = dueDate,
          location = location,
          status = status,
          description = description)
    } catch (e: Exception) {
      Log.e("TodosRepositoryFirestore", "Error converting document to ToDo", e)
      null
    }
  }
}
