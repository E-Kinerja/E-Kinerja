package com.arya.e_kinerja.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
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
                preferences[ID_PNS] ?: 0,
                preferences[LEVEL_KEY] ?: "",
                preferences[NIP_KEY] ?: "",
                preferences[NAMA_KEY] ?: "",
                preferences[KODE_JABATAN_KEY] ?: "",
                preferences[NAMA_JABATAN_KEY] ?: "",
                preferences[UNIT_KERJA_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun postSession(sessionEntity: SessionEntity) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = sessionEntity.token ?: ""
            preferences[ID_PNS] = sessionEntity.idPns ?: 0
            preferences[LEVEL_KEY] = sessionEntity.level ?: ""
            preferences[NIP_KEY] = sessionEntity.nip ?: ""
            preferences[NAMA_KEY] = sessionEntity.nama ?: ""
            preferences[KODE_JABATAN_KEY] = sessionEntity.kodeJabatan ?: ""
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

    fun getNotifikasi(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[NOTIFIKASI_KEY] ?: false
        }
    }

    suspend fun postNotifikasi(state: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFIKASI_KEY] = state
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val ID_PNS = intPreferencesKey("id_pns")
        private val LEVEL_KEY = stringPreferencesKey("level")
        private val NIP_KEY = stringPreferencesKey("nip")
        private val NAMA_KEY = stringPreferencesKey("nama")
        private val KODE_JABATAN_KEY = stringPreferencesKey("kode_jabatan")
        private val NAMA_JABATAN_KEY = stringPreferencesKey("nama_jabatan")
        private val UNIT_KERJA_KEY = stringPreferencesKey("unit_kerja")
        private val STATE_KEY = booleanPreferencesKey("state")

        private val NOTIFIKASI_KEY = booleanPreferencesKey("notifikasi")
    }
}