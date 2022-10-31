package com.arya.e_kinerja.ui.penilaian_aktivitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.remote.response.DeleteTugasAktivitasResponse
import com.arya.e_kinerja.data.remote.response.GetListBawahanResponse
import com.arya.e_kinerja.data.remote.response.GetTugasAktivitasResponse
import com.arya.e_kinerja.data.remote.response.PostVerifAktivitasResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PenilaianAktivitasViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun getListBawahan(): LiveData<Result<GetListBawahanResponse>> {
        return repository.getListBawahan()
    }

    fun deleteTugasAktivitas(id: Int): LiveData<Result<DeleteTugasAktivitasResponse>> {
        return repository.deleteTugasAktivitas(id)
    }

    fun getTugasAktivitas(
        idPns: Int?,
        bulan: Int,
        tahun: Int
    ): LiveData<Result<List<GetTugasAktivitasResponse>>> {
        return repository.getTugasAktivitas(
            idPns,
            bulan,
            tahun
        )
    }

    fun postVerifTugasAktivitas(
        id: Int,
        status: Boolean,
        idPns: Int,
        bulan: Int,
        tahun: Int
    ): LiveData<Result<PostVerifAktivitasResponse>> {
        return repository.postVerifTugasAktivitas(
            id,
            status,
            idPns,
            bulan,
            tahun
        )
    }
}