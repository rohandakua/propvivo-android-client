package com.propvivotaskmanagmentapp.propvivoandroid.data.remote.repositoryImplementation

import com.google.firebase.firestore.FirebaseFirestore
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.TaskQueryStatus
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Message
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.TaskQueryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.util.FirebasePathConstants
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class TaskQueryRepoImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : TaskQueryInterface {
    private val taskCollection = firestore.collection(FirebasePathConstants.TASKS)
    override suspend fun getAllMessages(taskQueryId: String): List<Message> {
        val snapshot = firestore.collection(FirebasePathConstants.TASK_QUERIES)
            .document(taskQueryId)
            .collection(FirebasePathConstants.MESSAGES)
            .orderBy("timestamp")
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Message::class.java)?.copy(id = doc.id)
        }
    }

    override suspend fun sendMessage(message: Message) {
        val messageRef = firestore.collection(FirebasePathConstants.TASK_QUERIES)
            .document(message.taskQueryId)
            .collection(FirebasePathConstants.MESSAGES)
            .document()

        val messageWithId = message.copy(id = messageRef.id)
        messageRef.set(messageWithId).await()
    }

    override suspend fun updateQueryStatus(
        taskQueryId: String,
        taskQueryStatus: TaskQueryStatus
    ) {
        firestore.collection(FirebasePathConstants.TASK_QUERIES)
            .document(taskQueryId)
            .update("taskStatus", taskQueryStatus.name)
            .await()
    }
    override suspend fun getTask(taskId: String): Task? {
        val snapshot = taskCollection.document(taskId).get().await()
        return snapshot.toObject(Task::class.java)
    }
}
