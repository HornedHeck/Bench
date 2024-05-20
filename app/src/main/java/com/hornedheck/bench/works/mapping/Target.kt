package com.hornedheck.bench.works.mapping

data class Hotel(
    val id: Int,
    val name: String,
    val visiblePrice: HotelRate?,
    val rating: Rating?,
    val image: String?,
    val mapInfo: String?
)

data class HotelRate(
    val price: String,
    val strikePrice: String?,
    val priceDetails: List<PriceDetails>?,
)

data class PriceDetails(
    val title: String,
    val description: String?,
    val priceDelta: String,
)

data class Analytics(
    val refId: String,
    val key: String
)

data class HotelDetails(
    val name: String,
    val rating: Rating?,
    val images: List<String>,
    val mapInfo: String?,
    val description: String,
    val rooms : List<Room>
)

data class Room(
    val id: Int,
    val title: String,
    val description: String?,
    val perks: List<Perk>?,
    val rate: RoomRate?,
    val impressionAnalytics: Analytics,
    val bookAnalytics: Analytics,
)

data class Perk(
    val title: String,
    val icon: String?
)

data class RoomRate(
    val price: String,
    val strikePrice: String?,
    val priceDetails: List<PriceDetails>?,
    val priceAnalytics: Analytics,
)

data class Rating(
    val rating: Int,
    val title: String,
    val totalReviews: String
)