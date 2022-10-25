package com.arya.e_kinerja.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.arya.e_kinerja.data.local.datastore.SessionDataStore
import com.arya.e_kinerja.data.local.entity.SessionEntity
import com.arya.e_kinerja.data.remote.response.Aktivitas
import com.arya.e_kinerja.data.remote.response.InputAktivitasResponse
import com.arya.e_kinerja.data.remote.response.LoginResponse
import com.arya.e_kinerja.data.remote.response.TugasAktivitasResponse
import com.arya.e_kinerja.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService,
    private val sessionDataStore: SessionDataStore
) {

    fun getSession(): LiveData<SessionEntity> {
        return sessionDataStore.getSession().asLiveData()
    }

    suspend fun deleteSession() {
        return sessionDataStore.deleteSession()
    }

    fun postLogin(username: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postLogin(username, password)
            sessionDataStore.postSession(
                SessionEntity(
                    "${response.tokenType} ${response.accessToken}",
                    response.data?.idPns.toString(),
                    response.data?.level,
                    response.data?.nip,
                    response.data?.nama,
                    response.data?.namaJabatan,
                    response.data?.unitKerja,
                    true
                )
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postCariAktivitas(term: String): LiveData<Result<List<Aktivitas>>> = liveData {
        emit(Result.Loading)
        try {
            val token = sessionDataStore.getSession().first().token.toString()
            val response = apiService.postCariAktivitas(token, term)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postInputAktivitas(
        tanggal: String,
        idAkt: String,
        catatan: String,
        output: String,
        jamMulai: String,
        jamBerakhir: String
    ): LiveData<Result<InputAktivitasResponse>> = liveData {
        emit(Result.Loading)
        try {
            val nip =  sessionDataStore.getSession().first().nip.toString()
            val response = apiService.postInputAktivitas(
                nip,
                tanggal,
                idAkt,
                catatan,
                output,
                jamMulai,
                jamBerakhir
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getTugasAktivitas(
        bulan: String,
        tahun: String
    ): LiveData<Result<List<TugasAktivitasResponse>>> = liveData {
        emit(Result.Loading)
        try {
            val token = sessionDataStore.getSession().first().token.toString()
            val idPns = sessionDataStore.getSession().first().idPns.toString()
            val response = apiService.getTugasAktivitas(token, idPns, bulan, tahun)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}