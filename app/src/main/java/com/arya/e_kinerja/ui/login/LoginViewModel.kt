package com.arya.e_kinerja.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arya.e_kinerja.data.Result
import com.arya.e_kinerja.data.Repository
import com.arya.e_kinerja.data.remote.response.PostLoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun postLogin(
        username: String,
        password: String
    ): LiveData<Result<PostLoginResponse>> {
        return repository.postLogin(
            username,
            password
        )
    }
}