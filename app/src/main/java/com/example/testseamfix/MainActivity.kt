package com.example.testseamfix

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.testseamfix.model.Location
import com.example.testseamfix.utils.Exts.dismissProgress
import com.example.testseamfix.utils.Exts.onErrorMessage
import com.example.testseamfix.utils.Exts.showProgress
import com.example.testseamfix.utils.shortToast
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult


class MainActivity : AppCompatActivity() {
    private val audioPermission = Manifest.permission.RECORD_AUDIO

    private val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Audio Permission granted!", Toast.LENGTH_SHORT)
        } else {
            Toast.makeText(this, "Audio Permission needed!", Toast.LENGTH_SHORT)
            finish()
        }
    }

    private val TAG = this.javaClass.simpleName

    private val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val camera = findViewById<CameraView>(R.id.camera)
        camera.setLifecycleOwner(this)

        if (shouldShowRequestPermissionRationale(audioPermission)) {

        } else {
            requestPermissionLauncher.launch(audioPermission)
        }

        camera.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                // A Picture was taken!
                Log.i(TAG, "onPictureTaken: ")
                val bytes = result.data
                val encodedValue = Base64.encodeToString(bytes, Base64.DEFAULT)
                viewModel.uploadPicture(encodedValue, Location("", ""))
            }

            override fun onVideoTaken(result: VideoResult) {
                // A Video was taken!
                Log.i(TAG, "onVideoTaken: ")
            }

        })

        val fab: View = findViewById(R.id.floatingActionButton)
        fab.setOnClickListener { view ->
            camera.takePicture()
        }

        viewModel.uploadLiveData.observe(this) {
            when (it) {
                is UploadState.Loading -> {
                    showProgress(it.message, "Please wait")
                }
                is UploadState.Success -> {
                    shortToast(it.data)
                    dismissProgress()
                }
                is UploadState.Error -> {
                    it.error?.let { it1 -> onErrorMessage("Error", it1) }
                }
            }
        }
    }

}