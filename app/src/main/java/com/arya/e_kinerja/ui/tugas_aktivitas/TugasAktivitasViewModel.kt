package com.arya.e_kinerja.ui.tugas_aktivitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.remote.response.DeleteTugasAktivitasResponse
import com.arya.e_kinerja.data.remote.response.TugasAktivitasResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TugasAktivitasViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun getTugasAktivitas(bulan: String, tahun: String): LiveData<Result<List<TugasAktivitasResponse>>> {
        return repository.getTugasAktivitas(null, bulan, tahun)
    }

    fun deleteTugasAktivitas(id: String): LiveData<Result<DeleteTugasAktivitasResponse>> {
        return repository.deleteTugasAktivitas(id)
    }
}