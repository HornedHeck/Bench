package com.hornedheck.bench.works.mapping

import com.google.gson.GsonBuilder
import java.io.File
import java.util.UUID
import kotlin.random.Random

fun main() {

    val src = List(50) {
        generateHotelsResponse()
    }

    val res = GsonBuilder().create().toJson(src)
    File("hotels.json").writeText(res)
}

fun <T> withChance(chance: Double = 0.1, generator: () -> T) = if (booleanWithChance(chance)) {
    null
} else {
    generator()
}

fun randomString() = UUID.randomUUID().toString()

fun randomNullString(chance: Double = 0.1) = withChance(chance) { randomString() }

fun booleanWithChance(chance: Double = 0.1) = Random.nextDouble() < chance

fun generateHotelsResponse(): HotelsResponse {

    val isError = booleanWithChance(0.1)
    if (isError) {
        return HotelsResponse(
            error = randomString(),
            hotels = null
        )
    }

    val hotels = List(Random.nextInt(1, 20)) { _ -> generateHotel() }

    return HotelsResponse(null, hotels)
}

fun generateHotel(): HotelResponse {
    val id = Random.nextInt()
    val name = randomNullString()
    val description = randomNullString()

    val rooms = withChance {
        List(10) { _ ->
            RoomResponse(
                withChance { Random.nextInt() },
                randomNullString(),
                RoomDescriptionResponse(
                    randomString(),
                    randomNullString(),
                    withChance(0.5) { Random.nextInt() }
                ),
                withChance(0.2) {
                    List(Random.nextInt(5)) { _ ->
                        PerkResponse(
                            Random.nextInt(),
                            randomString(),
                            randomNullString(0.4)
                        )
                    }
                },
                withChance { generateRoomRate() },
                generateAnalytics(),
                generateAnalytics()
            )
        }
    }

    val rating = withChance {
        RatingResponse(
            Random.nextInt(),
            randomString(),
            randomString()
        )
    }

    val images = withChance {
        List(Random.nextInt(5)) { _ ->
            randomString()
        }
    }

    return HotelResponse(
        id = id,
        name = name,
        description = description,
        rooms = rooms,
        rating = rating,
        images = images,
        mapInfo = randomNullString()
    )
}

fun generateAnalytics() = withChance {
    AnalyticsResponse(
        randomString(),
        randomString()
    )
}

fun generateRoomRate(): RoomRateResponse {

    val priceDetails = withChance {
        List(Random.nextInt(1, 5)) {
            PriceDetailsResponse(
                randomString(),
                randomNullString(),
                randomString()
            )
        }
    }

    return RoomRateResponse(
        randomString(),
        randomNullString(),
        priceDetails,
        generateAnalytics()
    )
}