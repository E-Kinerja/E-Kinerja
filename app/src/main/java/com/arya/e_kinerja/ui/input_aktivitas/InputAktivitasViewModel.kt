package com.arya.e_kinerja.ui.input_aktivitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.remote.response.Aktivitas
import com.arya.e_kinerja.data.remote.response.InputAktivitasResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InputAktivitasViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun postCariAktivitas(term: String): LiveData<Result<List<Aktivitas>>> {
        return repository.postCariAktivitas(term)
    }

    fun postInputAktivitas(
        tanggal: String,
        idAkt: String,
        catatan: String,
        output: String,
        jamMulai: String,
        jamBerakhir: String
    ): LiveData<Result<InputAktivitasResponse>> {
        return repository.postInputAktivitas(
            tanggal,
            idAkt,
            catatan,
            output,
            jamMulai,
            jamBerakhir
        )
    }
}