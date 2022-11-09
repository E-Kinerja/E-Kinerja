package com.arya.e_kinerja.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.arya.e_kinerja.data.local.datastore.SessionDataStore
import com.arya.e_kinerja.data.local.entity.SessionEntity
import com.arya.e_kinerja.data.remote.response.*
import com.arya.e_kinerja.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.*
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

    fun getNotifikasi(): LiveData<Boolean> {
        return sessionDataStore.getNotifikasi().asLiveData()
    }

    suspend fun postNotifikasi(state: Boolean) {
        sessionDataStore.postNotifikasi(state)
    }

    fun postLogin(
        username: String,
        password: String
    ): LiveData<Result<PostLoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postLogin(
                username,
                password
            )
            sessionDataStore.postSession(
                SessionEntity(
                    "${response.tokenType} ${response.accessToken}",
                    response.data?.idPns,
                    response.data?.level,
                    response.data?.nip,
                    response.data?.nama,
                    response.data?.kodeJabatan,
                    response.data?.namaJabatan,
                    response.data?.unitKerja,
                    true
                )
            )
            sessionDataStore.postNotifikasi(false)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postCariAktivitas(
        term: String
    ): LiveData<Result<List<Aktivitas>>> = liveData {
        emit(Result.Loading)
        try {
            val token = sessionDataStore.getSession().first().token.toString()
            val response = apiService.postCariAktivitas(
                token,
                term
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postInputAktivitas(
        nip: String?,
        tanggal: String,
        idAkt: Int,
        catatan: String,
        output: String,
        jamMulai: String,
        jamBerakhir: String
    ): LiveData<Result<PostInputAktivitasResponse>> = liveData {
        emit(Result.Loading)
        try {
            val mNip = if (nip.isNullOrEmpty()) {
                sessionDataStore.getSession().first().nip
            } else {
                nip
            }
            val response = apiService.postInputAktivitas(
                (mNip as String),
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
        idPns: Int?,
        bulan: Int,
        tahun: Int
    ): LiveData<Result<List<GetTugasAktivitasResponse>>> = liveData {
        emit(Result.Loading)
        try {
            val token = sessionDataStore.getSession().first().token.toString()
            val mIdPns = idPns ?: sessionDataStore.getSession().first().idPns
            val response = apiService.getTugasAktivitas(
                token,
                (mIdPns as Int),
                bulan,
                tahun
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun deleteTugasAktivitas(id: Int): LiveData<Result<DeleteTugasAktivitasResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = sessionDataStore.getSession().first().token.toString()
            val response = apiService.deleteTugasAktivitas(
                token,
                id
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postEditAktivitas(
        id: Int,
        nip: String?,
        tanggal: String,
        idAkt: Int,
        catatan: String,
        output: String,
        jamMulai: String,
        jamBerakhir: String
    ): LiveData<Result<PostEditAktivitasResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = sessionDataStore.getSession().first().token.toString()
            val mNip = if (nip.isNullOrEmpty()) {
                sessionDataStore.getSession().first().nip
            } else {
                nip
            }
            val response = apiService.postEditAktivitas(
                token,
                id,
                (mNip as String),
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
            val idPns = sessionDataStore.getSession().first().idPns
            val response = apiService.getTotalAktivitas(
                token,
                (idPns as Int)
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getListBawahan(): LiveData<Result<GetListBawahanResponse>> = liveData {
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

    fun postVerifTugasAktivitas(
        id: Int,
        status: Boolean,
        idPns: Int,
        bulan: Int,
        tahun: Int
    ): LiveData<Result<PostVerifAktivitasResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = sessionDataStore.getSession().first().token.toString()
            val response = apiService.postVerifTugasAktivitas(
                token,
                id,
                status,
                idPns,
                bulan,
                tahun
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}