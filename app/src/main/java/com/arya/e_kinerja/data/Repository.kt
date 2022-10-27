package com.arya.e_kinerja.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.arya.e_kinerja.data.local.datastore.SessionDataStore
import com.arya.e_kinerja.data.local.entity.SessionEntity
import com.arya.e_kinerja.data.remote.response.*
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
                    response.data?.kodeJabatan,
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
        idPns: String?,
        bulan: String,
        tahun: String
    ): LiveData<Result<List<TugasAktivitasResponse>>> = liveData {
        emit(Result.Loading)
        try {
            val token = sessionDataStore.getSession().first().token.toString()
            val response = if (idPns != null) {
                apiService.getTugasAktivitas(token, idPns, bulan, tahun)
            } else {
                apiService.getTugasAktivitas(token, sessionDataStore.getSession().first().idPns.toString(), bulan, tahun)
            }
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun deleteTugasAktivitas(
        id: String
    ): LiveData<Result<DeleteTugasAktivitasResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = sessionDataStore.getSession().first().token.toString()
            val response = apiService.deleteTugasAktivitas(token, id)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postEditAktivitas(
        id: String,
        tanggal: String,
        idAkt: String,
        catatan: String,
        output: String,
        jamMulai: String,
        jamBerakhir: String
    ): LiveData<Result<EditAktivitasResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = sessionDataStore.getSession().first().token.toString()
            val nip = sessionDataStore.getSession().first().nip.toString()
            val response = apiService.postEditAktivitas(
                token,
                id,
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

    fun getTotalAktivitas(): LiveData<Result<GetTotalAktivitasResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = sessionDataStore.getSession().first().token.toString()
            val idPns = sessionDataStore.getSession().first().idPns.toString()
            val response = apiService.getTotalAktivitas(
                token,
                idPns
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getListBawahanResponse(): LiveData<Result<GetListBawahanResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = sessionDataStore.getSession().first().token.toString()
            val nip = sessionDataStore.getSession().first().nip.toString()
            val kodeJabatan = sessionDataStore.getSession().first().kodeJabatan.toString()
            val response = apiService.getListBawahan(
                token,
                nip,
                kodeJabatan
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}