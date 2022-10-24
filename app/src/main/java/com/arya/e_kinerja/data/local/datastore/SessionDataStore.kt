package com.arya.e_kinerja.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.arya.e_kinerja.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionDataStore(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Session.ds")

    fun getSession(): Flow<SessionEntity> {
        return context.dataStore.data.map { preferences ->
            SessionEntity(
                preferences[TOKEN_KEY] ?: "",
                preferences[ID_PNS] ?: "",
                preferences[LEVEL_KEY] ?: "",
                preferences[NIP_KEY] ?: "",
                preferences[NAMA_KEY] ?: "",
                preferences[NAMA_JABATAN_KEY] ?: "",
                preferences[UNIT_KERJA_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun postSession(sessionEntity: SessionEntity) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = sessionEntity.token ?: ""
            preferences[ID_PNS] = sessionEntity.idPns ?: ""
            preferences[LEVEL_KEY] = sessionEntity.level ?: ""
            preferences[NIP_KEY] = sessionEntity.nip ?: ""
            preferences[NAMA_KEY] = sessionEntity.nama ?: ""
            preferences[NAMA_JABATAN_KEY] = sessionEntity.namaJabatan ?: ""
            preferences[UNIT_KERJA_KEY] = sessionEntity.unitKerja ?: ""
            preferences[STATE_KEY] = sessionEntity.state ?: false
        }
    }

    suspend fun deleteSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val ID_PNS = stringPreferencesKey("id_pns")
        private val LEVEL_KEY = stringPreferencesKey("level")
        private val NIP_KEY = stringPreferencesKey("nip")
        private val NAMA_KEY = stringPreferencesKey("nama")
        private val NAMA_JABATAN_KEY = stringPreferencesKey("nama_jabatan")
        private val UNIT_KERJA_KEY = stringPreferencesKey("unit_kerja")
        private val STATE_KEY = booleanPreferencesKey("state")
    }
}