package com.arya.e_kinerja.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.local.entity.SessionEntity
import com.arya.e_kinerja.data.remote.response.GetTotalAktivitasResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun getSession(): LiveData<SessionEntity> {
        return repository.getSession()
    }

    fun getTotalAktivitas(): LiveData<Result<GetTotalAktivitasResponse>> {
        return repository.getTotalAktivitas()
    }
}