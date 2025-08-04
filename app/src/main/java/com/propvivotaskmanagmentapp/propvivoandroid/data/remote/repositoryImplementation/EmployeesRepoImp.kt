package com.propvivotaskmanagmentapp.propvivoandroid.data.remote.repositoryImplementation

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.TaskQuery
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.EmployeesRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.util.FirebasePathConstants
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EmployeesRepoImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : EmployeesRepositoryInterface {

    private val taskCollection = firestore.collection(FirebasePathConstants.TASKS)
    private val taskQueryCollection = firestore.collection(FirebasePathConstants.TASK_QUERIES)
    private val recordCollection = firestore.collection(FirebasePathConstants.RECORDS)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override suspend fun getAllTask(
        employeeId: String,
        date: LocalDate
    ): List<Task> {
        val formattedDate = date.format(dateFormatter)
        val snapshot = taskCollection
            .whereEqualTo("id", employeeId)
            .whereEqualTo("date", formattedDate)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject<Task>()?.copy(id = it.id) }
    }

    override suspend fun getTaskQueryList(
        taskId: String
    ): List<TaskQuery> {
        val snapshot = taskQueryCollection
            .whereEqualTo("taskId", taskId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject<TaskQuery>()?.copy(id = it.id) }
    }

    override suspend fun addTask(task: Task) {
        val docRef = taskCollection.document()
        val taskWithId = task.copy(id = docRef.id)
        docRef.set(taskWithId).await()
    }

    override suspend fun updateTaskTimeSpent(updatedTime: Long, taskId: String) {
        taskCollection.document(taskId)
            .update("timeSpentInMs", updatedTime)
            .await()
        taskCollection.document(taskId)
            .update("updatedAt", System.currentTimeMillis())
            .await()
    }

    override suspend fun updateTotalTime(userId: String, updatedTime: Long, date: LocalDate) {
        val formattedDate = date.format(dateFormatter)
        val docRef = recordCollection
            .document(userId)
            .collection("dates")
            .document(formattedDate)

        docRef.update("totalTime", updatedTime)
            .addOnFailureListener {
                docRef.set(
                    mapOf(
                        "date" to formattedDate,
                        "totalTime" to updatedTime
                    )
                )
            }.await()
    }

}
