package com.example.testseamfix

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testseamfix.data.ServiceLocator
import com.example.testseamfix.model.Location
import com.example.testseamfix.model.UploadPictureRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class UploadState {
    data class Success(val data: String) : UploadState()
    data class Error(val error: Exception? = null) : UploadState()
    data class Loading(val message: String = "Loading") : UploadState()
}

class MainViewModel(private val repo: Repository = ServiceLocator.provideRepository()) :
    ViewModel() {
    private val TAG = this.javaClass.simpleName

    private var _uploadLiveData: MutableLiveData<UploadState> =
        MutableLiveData(UploadState.Error())
    val uploadLiveData: LiveData<UploadState> get() = _uploadLiveData


    fun uploadPicture(picture: String, location: Location) {
        viewModelScope.launch {
            _uploadLiveData.value = UploadState.Loading()
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    repo.upload(picture, location)
                }
            }.onSuccess {
                Log.i(TAG, "uploadPicture: Success")
                _uploadLiveData.value = UploadState.Success(it.status)
            }.onFailure {
                Log.i(TAG, "uploadPicture: Error $it")
                _uploadLiveData.value = UploadState.Error(Exception(it.message))
            }
        }
    }
}