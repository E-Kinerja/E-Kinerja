package com.arya.e_kinerja.ui.laporan_aktivitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.local.entity.SessionEntity
import com.arya.e_kinerja.data.remote.response.GetTotalAktivitasResponse
import com.arya.e_kinerja.data.remote.response.GetTugasAktivitasResponse
import com.arya.e_kinerja.utils.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaporanAktivitasViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _laporanAktivitas = MutableLiveData<Result<List<GetTugasAktivitasResponse>>>()
    val laporanAktivitas: LiveData<Result<List<GetTugasAktivitasResponse>>> = _laporanAktivitas

    private val _bulan = MutableLiveData<Int>()
    private val bulan: LiveData<Int> = _bulan

    private val _tahun = MutableLiveData<Int>()
    private val tahun: LiveData<Int> = _tahun

    init {
        _bulan.value = dateFormat(null, "MM").toInt()
        _tahun.value = dateFormat(null, "yyyy").toInt()

        getLaporanAktivitas()
    }

    fun setTahun(tahun: Int) {
        _tahun.value = tahun
    }

    fun setBulan(bulan: Int) {
        _bulan.value = bulan
    }

    fun getSession(): LiveData<SessionEntity> {
        return repository.getSession()
    }

    fun getTotalAktivitas(): LiveData<Result<GetTotalAktivitasResponse>> {
        return repository.getTotalAktivitas()
    }

    fun getLaporanAktivitas() {
        repository.getTugasAktivitas(
            null,
            bulan.value as Int,
            tahun.value as Int
        ).observeForever {
            _laporanAktivitas.value = it
        }
    }
}