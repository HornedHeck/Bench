package com.hornedheck.bench.works.mapping

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.hornedheck.bench.works.Worker
import kotlin.system.measureTimeMillis

class MapperWorker : Worker() {

    private lateinit var dataToParse: List<HotelsResponse>

    private val priceDetailsMapper = PriceDetailsMapperImpl()
    private val hotelMapper = HotelMapperImpl(priceDetailsMapper)
    private val hotelsMapper = HotelsMapperImpl(hotelMapper)
    private val roomMapper = RoomMapperImpl(priceDetailsMapper)
    private val hotelDetailsMapper = HotelDetailMapperImpl(roomMapper)

    override val batchCount: Int
        get() = 1
    override val batchSize: Int
        get() = 5

    override fun run(context: Context, batchNum: Int, i: Int) {
        prepare(context)
        dataToParse.map {
            hotelsMapper.map(it) to it.hotels?.map { hotel -> hotelDetailsMapper.map(hotel) }
        }
    }

    private fun prepare(context: Context) {
        val gson = GsonBuilder().create()
        val src = context.assets.open("hotels.json").reader().readText()
        dataToParse = gson.fromJson(src, Array<HotelsResponse>::class.java).asList()
    }
}