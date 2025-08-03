package com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object dsConstants {
    val USER_ID = stringPreferencesKey("user_id")
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_EMAIL = stringPreferencesKey("user_email")
    val USER_ROLE = stringPreferencesKey("user_role")
    val PREFERENCE_DATA_STORE_NAME = "user_data"


}