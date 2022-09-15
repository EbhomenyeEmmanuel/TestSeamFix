package com.example.testseamfix.data

import com.example.testseamfix.Repository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object ServiceLocator {

    private val SERVICE_REPOSITORY: Repository? = null
    private val moshi: Moshi? = null


    fun provideRepository(): Repository {
        synchronized(this) {
            return SERVICE_REPOSITORY ?: createServiceRepository()
        }
    }

    private fun provideMoshi(): Moshi {
        synchronized(this) {
            return moshi ?: Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        }
    }

    private fun createServiceRepository(): Repository {
        val apiService = ApiServiceFactory.createApiService(provideMoshi())
        return Repository(apiService)
    }
}