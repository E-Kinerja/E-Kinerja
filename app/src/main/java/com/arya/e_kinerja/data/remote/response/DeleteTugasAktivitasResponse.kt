package com.arya.e_kinerja.data.remote.response

import com.google.gson.annotations.SerializedName

data class DeleteTugasAktivitasResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
