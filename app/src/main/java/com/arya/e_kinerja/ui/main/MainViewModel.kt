package com.arya.e_kinerja.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.local.entity.SessionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun getSession(): LiveData<SessionEntity> {
        return repository.getSession()
    }

    fun deleteSession() {
        viewModelScope.launch {
            repository.deleteSession()
        }
    }

    fun postNotifikasi(state: Boolean) {
        viewModelScope.launch {
            repository.postNotifikasi(state)
        }
    }
}