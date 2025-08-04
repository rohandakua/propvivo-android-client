package com.propvivotaskmanagmentapp.propvivoandroid.data.remote.repositoryImplementation

import com.google.firebase.firestore.FirebaseFirestore
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Employee
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.AdminRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.util.FirebasePathConstants
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction.getDayBounds
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.util.Date

class AdminRepoImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : AdminRepositoryInterface {

    private val taskCollection = firestore.collection(FirebasePathConstants.TASKS)
    val usersCollection = firestore.collection(FirebasePathConstants.USERS)
    val recordsCollection = firestore.collection(FirebasePathConstants.RECORDS)

    override suspend fun getTotalNoOfTask(date: Date): Int {
        val (start, end) = getDayBounds(date)

        val snapshot = taskCollection
            .whereGreaterThanOrEqualTo("createdAt", start)
            .whereLessThan("createdAt", end)
            .get()
            .await()

        return snapshot.size()
    }

    override suspend fun getAllEmployeeWithStatus(date: Date): List<Employee> {
        val (start, end) = getDayBounds(date)

        // Step 1: Fetch all tasks created on the given date
        val taskSnapshot = firestore.collection(FirebasePathConstants.TASKS)
            .whereGreaterThanOrEqualTo("createdAt", start)
            .whereLessThan("createdAt", end)
            .get()
            .await()

        val tasks = taskSnapshot.documents.mapNotNull { it.toObject(Task::class.java) }

        val uniqueEmployeeIds = tasks.mapNotNull { it.assignedTo }.toSet()


        val employees = mutableListOf<Employee>()
        val eightHoursInMillis = 8 * 60 * 60 * 1000

        for (employeeId in uniqueEmployeeIds) {
            val userSnapshot = usersCollection.document(employeeId).get().await()
            val name = userSnapshot.getString(FirebasePathConstants.NAME) ?: "Unknown"
            val recordSnapshot = recordsCollection
                .document(employeeId)
                .collection("dates")
                .whereGreaterThanOrEqualTo("date", start)
                .whereLessThan("date", end)
                .get()
                .await()

            val totalDuration = recordSnapshot.documents.sumOf {
                it.getLong("duration") ?: 0L
            }

            val timeCompleted = totalDuration >= eightHoursInMillis

            employees.add(Employee(id = employeeId, name = name, timeCompleted = timeCompleted))
        }

        return employees
    }




}
