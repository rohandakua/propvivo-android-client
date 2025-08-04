package com.propvivotaskmanagmentapp.propvivoandroid.data.remote.repositoryImplementation

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.TaskQuery
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.SupervisorRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.util.FirebasePathConstants
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.ZoneId

class SupervisorRepoImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : SupervisorRepositoryInterface {

    private val userCollection = firestore.collection(FirebasePathConstants.USERS)
    private val taskCollection = firestore.collection(FirebasePathConstants.TASKS)
    private val taskQueryCollection = firestore.collection(FirebasePathConstants.TASK_QUERIES)

    override suspend fun getAllEmployee(supervisorId: String): List<User> {
        val taskSnapshot = firestore.collection(FirebasePathConstants.TASKS)
            .whereEqualTo("assignedBy", supervisorId)
            .get()
            .await()

        val employeeIds = taskSnapshot.documents
            .mapNotNull { it.getString("assignedTo") }
            .toSet()

        val usersCollection = firestore.collection(FirebasePathConstants.USERS)
        val userList = mutableListOf<User>()

        employeeIds.forEach { uid ->
            val userSnap = usersCollection.document(uid).get().await()
            userSnap.toObject(User::class.java)?.let {
                userList.add(it.copy(uid = userSnap.id))
            }
        }

        return userList
    }


    override suspend fun getAllTaskOfADayByEmployeeId(
        employeeId: String,
        date: LocalDate
    ): List<Task> {
        val startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
        val endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000

        val snapshot = taskCollection
            .whereEqualTo("assignedTo", employeeId)
            .whereGreaterThanOrEqualTo("createdAt", startOfDay)
            .whereLessThan("createdAt", endOfDay)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject<Task>()?.copy(id = it.id) }
    }

    override suspend fun addTask(task: Task) {
        val docRef = taskCollection.document()
        val taskWithId = task.copy(id = docRef.id)
        docRef.set(taskWithId).await()
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

}
