package com.example.testseamfix.model

class UploadPictureRequest(
    val phoneNumbers: Array<String>,
    val image: String,
    val location: Location
)

class Location(val longitude: String, val latitude: String)