package com.arya.e_kinerja.ui.tugas_aktivitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.remote.response.DeleteTugasAktivitasResponse
import com.arya.e_kinerja.data.remote.response.GetTugasAktivitasResponse
import com.arya.e_kinerja.utils.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TugasAktivitasViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _tugasAktivitas = MutableLiveData<Result<List<GetTugasAktivitasResponse>>>()
    val tugasAktivitas: LiveData<Result<List<GetTugasAktivitasResponse>>> = _tugasAktivitas

    private val _bulan = MutableLiveData<Int>()
    private val bulan: LiveData<Int> = _bulan

    private val _tahun = MutableLiveData<Int>()
    private val tahun: LiveData<Int> = _tahun

    init {
        _bulan.value = dateFormat(null, "MM").toInt()
        _tahun.value = dateFormat(null, "yyyy").toInt()

        getTugasAktivitas()
    }

    fun setTahun(tahun: Int) {
        _tahun.value = tahun
    }

    fun setBulan(bulan: Int) {
        _bulan.value = bulan
    }

    fun deleteTugasAktivitas(id: Int): LiveData<Result<DeleteTugasAktivitasResponse>> {
        return repository.deleteTugasAktivitas(id)
    }

    fun getTugasAktivitas() {
        repository.getTugasAktivitas(
            null,
            bulan.value as Int,
            tahun.value as Int
        ).observeForever {
            _tugasAktivitas.value = it
        }
    }
}