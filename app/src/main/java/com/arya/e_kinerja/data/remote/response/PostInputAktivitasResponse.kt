package com.arya.e_kinerja.data.remote.response

import com.google.gson.annotations.SerializedName

data class PostInputAktivitasResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("log_id")
    val logId: String? = null
)
