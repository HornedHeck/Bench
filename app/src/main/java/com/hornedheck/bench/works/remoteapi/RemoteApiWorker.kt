package com.hornedheck.bench.works.remoteapi

import android.content.Context
import com.hornedheck.bench.works.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteApiWorker : Worker() {

    override val batchSize: Int
        get() = 1

    private val client = OkHttpClient.Builder()
        .build()

    private val api = Retrofit.Builder()
        .baseUrl("https://dog.ceo/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(DogsApi::class.java)

    override fun run(context: Context, batchIteration: Int, iteration : Int) {
        runBlocking {
            withContext(Dispatchers.IO) {
                val breeds = api.getAllBreeds()
                val (breed, subBreed) = breeds.message.firstNotNullOf {
                    if (it.value.isNotEmpty()) {
                        it.key to it.value[0]
                    } else {
                        null
                    }
                }
                api.getBreedImages(breed)
                api.getAllSubBreeds(breed)
                api.getSubBreedImages(breed, subBreed)
            }
        }
    }
}