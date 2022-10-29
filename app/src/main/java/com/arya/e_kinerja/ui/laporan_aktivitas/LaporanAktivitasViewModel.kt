package com.arya.e_kinerja.ui.laporan_aktivitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.local.entity.SessionEntity
import com.arya.e_kinerja.data.remote.response.GetTotalAktivitasResponse
import com.arya.e_kinerja.data.remote.response.TugasAktivitasResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaporanAktivitasViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    fun getSession(): LiveData<SessionEntity> {
        return repository.getSession()
    }

    fun getTugasAktivitas(bulan: String, tahun: String): LiveData<Result<List<TugasAktivitasResponse>>> {
        return repository.getTugasAktivitas(null, bulan, tahun)
    }

    fun getTotalAktivitas(): LiveData<Result<GetTotalAktivitasResponse>> {
        return repository.getTotalAktivitas()
    }
}