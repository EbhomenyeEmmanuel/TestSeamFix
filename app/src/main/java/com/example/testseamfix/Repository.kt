package com.example.testseamfix

import com.example.testseamfix.data.ApiService
import com.example.testseamfix.model.ApiResult
import com.example.testseamfix.model.Location
import com.example.testseamfix.model.UploadPictureRequest
import com.example.testseamfix.model.domain.UploadPictureModel
import com.example.testseamfix.utils.SafeResult.safeApiResult

class Repository(private val service: ApiService) {
    suspend fun upload(picture: String, location: Location): UploadPictureModel {
        return when (val result =
            safeApiResult { service.uploadPicture(UploadPictureRequest(sosNumbers, picture, location)) }) {
            is ApiResult.Success -> {
                UploadPictureModel(result.data.status)
            }
            is ApiResult.Error -> throw result.exception
        }
    }

    companion object{
       private val sosNumbers = arrayOf("+23423456789", "+23487378900", "911")
    }
}