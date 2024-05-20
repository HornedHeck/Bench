package com.hornedheck.bench.works.mapping

data class HotelsResponse(
    val error: String?,
    val hotels: List<HotelResponse>?,
)

data class HotelResponse(
    val id: Int,
    val name: String?,
    val rooms: List<RoomResponse>?,
    val description: String?,
    val rating: RatingResponse?,
    val images: List<String>?,
    val mapInfo: String?
)

data class RoomResponse(
    val id: Int?,
    val title : String?,
    val description: RoomDescriptionResponse?,
    val perks: List<PerkResponse>?,
    val rate: RoomRateResponse?,
    val impressionAnalytics: AnalyticsResponse?,
    val bookAnalytics: AnalyticsResponse?,
)

data class RoomDescriptionResponse(
    val title: String,
    val bed: String?,
    val surface: Int?,
)

data class RoomRateResponse(
    val price: String,
    val strikePrice: String?,
    val priceDetails: List<PriceDetailsResponse>?,
    val priceAnalytics: AnalyticsResponse?,
)

data class PriceDetailsResponse(
    val title: String,
    val description: String?,
    val priceDelta: String,
)

data class PerkResponse(
    val id: Int,
    val title: String,
    val icon: String?
)

data class RatingResponse(
    val rating: Int,
    val title: String,
    val totalReviews: String
)

data class AnalyticsResponse(
    val refId: String,
    val key: String
)