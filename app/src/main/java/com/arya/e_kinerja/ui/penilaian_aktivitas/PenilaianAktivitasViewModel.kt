package com.arya.e_kinerja.ui.penilaian_aktivitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.remote.response.DeleteTugasAktivitasResponse
import com.arya.e_kinerja.data.remote.response.GetListBawahanResponse
import com.arya.e_kinerja.data.remote.response.GetTugasAktivitasResponse
import com.arya.e_kinerja.data.remote.response.PostVerifAktivitasResponse
import com.arya.e_kinerja.utils.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PenilaianAktivitasViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _penilaianAktivitas = MutableLiveData<Result<List<GetTugasAktivitasResponse>>>()
    val penilaianAktivitas: LiveData<Result<List<GetTugasAktivitasResponse>>> = _penilaianAktivitas

    private val _idPns = MutableLiveData<Int>()
    val idPns: LiveData<Int> = _idPns

    private val _bulan = MutableLiveData<Int>()
    private val bulan: LiveData<Int> = _bulan

    private val _tahun = MutableLiveData<Int>()
    private val tahun: LiveData<Int> = _tahun

    private val _nip = MutableLiveData<String>()
    val nip: LiveData<String> = _nip

    init {
        _bulan.value = dateFormat(null, "MM", null).toInt()
        _tahun.value = dateFormat(null, "yyyy", null).toInt()
    }

    fun setIdPns(idPns: Int) {
        _idPns.value = idPns
    }

    fun setBulan(bulan: Int) {
        _bulan.value = bulan
    }

    fun setTahun(tahun: Int) {
        _tahun.value = tahun
    }

    fun setNip(nip: String) {
        _nip.value = nip
    }

    fun getListBawahan(): LiveData<Result<GetListBawahanResponse>> {
        return repository.getListBawahan()
    }

    fun deleteTugasAktivitas(id: Int): LiveData<Result<DeleteTugasAktivitasResponse>> {
        return repository.deleteTugasAktivitas(id)
    }

    fun getLaporanAktivitas() {
        if (idPns.value == null) return
        repository.getTugasAktivitas(
            idPns.value as Int,
            bulan.value as Int,
            tahun.value as Int
        ).observeForever {
            _penilaianAktivitas.value = it
        }
    }

    fun postVerifTugasAktivitas(
        id: Int,
        status: Boolean
    ): LiveData<Result<PostVerifAktivitasResponse>> {
        return repository.postVerifTugasAktivitas(
            id,
            status,
            idPns.value as Int,
            bulan.value as Int,
            tahun.value as Int
        )
    }
}