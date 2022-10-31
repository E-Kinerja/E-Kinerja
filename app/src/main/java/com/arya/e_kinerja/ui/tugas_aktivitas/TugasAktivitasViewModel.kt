package com.arya.e_kinerja.ui.tugas_aktivitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.remote.response.DeleteTugasAktivitasResponse
import com.arya.e_kinerja.data.remote.response.GetTugasAktivitasResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TugasAktivitasViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

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
}