package com.arya.e_kinerja.data.remote.response

import com.google.gson.annotations.SerializedName

data class CustomErrorResponse (
    @field:SerializedName("message")
    val message: String
)
