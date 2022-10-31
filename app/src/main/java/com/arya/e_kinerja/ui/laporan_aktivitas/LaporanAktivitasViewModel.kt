package com.arya.e_kinerja.ui.laporan_aktivitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.local.entity.SessionEntity
import com.arya.e_kinerja.data.remote.response.GetTotalAktivitasResponse
import com.arya.e_kinerja.data.remote.response.GetTugasAktivitasResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaporanAktivitasViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    fun getSession(): LiveData<SessionEntity> {
        return repository.getSession()
    }

    fun getTotalAktivitas(): LiveData<Result<GetTotalAktivitasResponse>> {
        return repository.getTotalAktivitas()
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