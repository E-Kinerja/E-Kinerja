package com.arya.e_kinerja.ui.pengaturan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.e_kinerja.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PengaturanViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun getNotifikasi(): LiveData<Boolean> {
        return repository.getNotifikasi()
    }

    fun postNotifikasi(state: Boolean) {
        viewModelScope.launch {
            repository.postNotifikasi(state)
        }
    }
}