package com.hornedheck.bench.works.remoteapi

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface DogsApi {

    @GET("breed/{breed}/list")
    suspend fun getAllSubBreeds(
        @Path("breed") breed: String
    ) : SubBreedsResponse

    @GET("breed/{breed}/{sub_breed}/images")
    suspend fun getSubBreedImages(
        @Path("breed") breed: String,
        @Path("sub_breed") subBreed: String,
    ) : SubBreedImagesResponse

    @GET("breed/{breed}/images")
    suspend fun getBreedImages(
        @Path("breed") breed: String,
    ) : BreedImagesResponse

    @GET("breeds/list/all")
    suspend fun getAllBreeds() : BreedsResponse

}

data class SubBreedsResponse(
    val message: List<String>,
    val status: String
)

data class SubBreedImagesResponse(
    val message: List<String>,
    val status: String
)

data class BreedImagesResponse(
    val message: List<String>,
    val status: String
)

data class BreedsResponse(
    val message: Map<String, List<String>>,
    val status: String
)