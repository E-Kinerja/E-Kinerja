package com.arya.e_kinerja.ui.penilaian_aktivitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.remote.response.GetListBawahanResponse
import com.arya.e_kinerja.data.remote.response.TugasAktivitasResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PenilaianAktivitasViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun getListBawahan(): LiveData<Result<GetListBawahanResponse>> {
        return repository.getListBawahanResponse()
    }

    fun getTugasAktivitas(idPns: String?, bulan: String, tahun: String): LiveData<Result<List<TugasAktivitasResponse>>> {
        return repository.getTugasAktivitas(idPns, bulan, tahun)
    }
}