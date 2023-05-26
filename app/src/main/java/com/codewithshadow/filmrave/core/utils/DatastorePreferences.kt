package com.codewithshadow.filmrave.core.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.map


const val USER_DATASTORE = "user_datastore"
private val Context.createDataStore:
        DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)
private val IS_INTRO_COMPLETED = booleanPreferencesKey("IS_INTRO_COMPLETED")

@InstallIn(SingletonComponent::class)
@Module
object DatastorePreferences {

    suspend fun updateIntroCompleted(context: Context, value: Boolean = false) {
        context.createDataStore.edit {
            it[IS_INTRO_COMPLETED] = value
        }
    }


    fun getIsIntroCompleted(context: Context) = context.createDataStore.data.map {
        it[IS_INTRO_COMPLETED] ?: false
    }

}