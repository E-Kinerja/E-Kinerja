package com.arya.e_kinerja.ui.cari_aktivitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.remote.response.Aktivitas
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CariAktivitasViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun postCariAktivitas(term: String): LiveData<Result<List<Aktivitas>>> {
        return repository.postCariAktivitas(term)
    }
}