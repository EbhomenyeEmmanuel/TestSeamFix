package com.example.testseamfix.model

class UploadPictureResponse(val status: String, val data: UploadData, val message: String)
class UploadData(
    val phoneNumbers: Array<String>,
    val image: String,
    val location: Location, val id: String
)