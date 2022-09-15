package com.example.testseamfix.data

import com.example.testseamfix.model.UploadPictureRequest
import com.example.testseamfix.model.UploadPictureResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("v1/create/")
    suspend fun uploadPicture(
        @Body payload: UploadPictureRequest
    ): Response<UploadPictureResponse>
}