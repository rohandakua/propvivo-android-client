package com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.PreferenceDataStoreInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException

private val Context.dataStore by preferencesDataStore(
    name = dsConstants.PREFERENCE_DATA_STORE_NAME
)

class PreferenceDataStoreHelper(context: Context) : PreferenceDataStoreInterface {

    private val dataStore = context.dataStore

    /** This returns us a flow of data from DataStore.
    Basically as soon we update the value in Datastore,
    the values returned by it also changes. */
    override suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val result = preferences[key] ?: defaultValue
            result
        }

    /** This returns the last saved value of the key. If we change the value,
    it wont effect the values produced by this function */
    override suspend fun <T> getFirstPreference(key: Preferences.Key<T>, defaultValue: T): T =
        dataStore.data.first()[key] ?: defaultValue

    override fun <T> getPreferenceSync(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }
    override suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    override suspend fun <T> clearAllPreference() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}

