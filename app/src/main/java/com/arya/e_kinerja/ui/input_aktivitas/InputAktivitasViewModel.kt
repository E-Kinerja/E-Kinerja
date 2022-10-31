package com.arya.e_kinerja.ui.input_aktivitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.remote.response.Aktivitas
import com.arya.e_kinerja.data.remote.response.PostEditAktivitasResponse
import com.arya.e_kinerja.data.remote.response.PostInputAktivitasResponse
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
        nip: String?,
        tanggal: String,
        idAkt: Int,
        catatan: String,
        output: String,
        jamMulai: String,
        jamBerakhir: String
    ): LiveData<Result<PostInputAktivitasResponse>> {
        return repository.postInputAktivitas(
            nip,
            tanggal,
            idAkt,
            catatan,
            output,
            jamMulai,
            jamBerakhir
        )
    }

    fun postEditTugasAktivitas(
        id: Int,
        nip: String?,
        tanggal: String,
        idAkt: Int,
        catatan: String,
        output: String,
        jamMulai: String,
        jamBerakhir: String
    ): LiveData<Result<PostEditAktivitasResponse>> {
        return repository.postEditAktivitas(
            id,
            nip,
            tanggal,
            idAkt,
            catatan,
            output,
            jamMulai,
            jamBerakhir
        )
    }
}