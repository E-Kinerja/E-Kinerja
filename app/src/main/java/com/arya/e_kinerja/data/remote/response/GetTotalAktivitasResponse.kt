package com.arya.e_kinerja.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetTotalAktivitasResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: String? = null
)
