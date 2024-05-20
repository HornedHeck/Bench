package com.hornedheck.bench.works.mapping

interface Mapper<T, V> {

    fun map(source: T): V?

}

interface HotelsMapper : Mapper<HotelsResponse, List<Hotel>>

class HotelsMapperImpl(
    private val hotelMapper: HotelMapper
) : HotelsMapper {

    override fun map(source: HotelsResponse) = if (source.error != null) {
        null
    } else {
        source.hotels?.mapNotNull(hotelMapper::map)
    }
}

interface HotelMapper : Mapper<HotelResponse, Hotel>

class HotelMapperImpl(
    private val priceDetailsMapper: PriceDetailsMapper
) : HotelMapper {

    override fun map(source: HotelResponse): Hotel? = with(source) {

        if (name == null || description == null) {
            return@with null
        }

        val hotelRating = rating?.let {
            Rating(
                it.rating,
                it.title,
                it.totalReviews
            )
        }

        val priceSection = rooms?.firstOrNull { it.rate != null }?.let {
            HotelRate(
                price = it.rate!!.price,
                strikePrice = it.rate.strikePrice,
                priceDetails = it.rate.priceDetails?.mapNotNull(priceDetailsMapper::map)
            )
        }

        return Hotel(
            id,
            name,
            rating = hotelRating,
            image = images?.firstOrNull(),
            mapInfo = mapInfo,
            visiblePrice = priceSection
        )
    }
}

interface PriceDetailsMapper : Mapper<PriceDetailsResponse, PriceDetails>

class PriceDetailsMapperImpl() : PriceDetailsMapper {

    override fun map(source: PriceDetailsResponse) = with(source) {
        PriceDetails(
            title,
            description,
            priceDelta
        )
    }
}

interface HotelDetailMapper : Mapper<HotelResponse, HotelDetails>

class HotelDetailMapperImpl(
    private val roomMapper: RoomMapper
) : HotelDetailMapper {

    override fun map(source: HotelResponse): HotelDetails? {

        if (source.name == null || source.description == null) {
            return null
        }

        val hotelRating = source.rating?.let {
            Rating(
                it.rating,
                it.title,
                it.totalReviews
            )
        }

        val rooms = source.rooms?.mapNotNull(roomMapper::map) ?: emptyList()

        return HotelDetails(
            name = source.name,
            rating = hotelRating,
            images = source.images ?: emptyList(),
            mapInfo = source.mapInfo,
            description = source.description,
            rooms = rooms
        )
    }
}

interface RoomMapper : Mapper<RoomResponse, Room>

class RoomMapperImpl(
    private val priceDetailsMapper: PriceDetailsMapper
) : RoomMapper {

    private fun mapAnalytics(from: AnalyticsResponse?) = from?.let {
        Analytics(
            from.refId,
            from.key
        )
    } ?: Analytics("", "")

    override fun map(source: RoomResponse): Room? {

        if (source.id == null || source.title == null || source.description == null) {
            return null
        }

        val perks = source.perks?.map {
            Perk(
                it.title,
                it.icon
            )
        }

        val rate = source.rate?.let { r ->
            RoomRate(
                r.price,
                r.strikePrice,
                r.priceDetails?.mapNotNull(priceDetailsMapper::map),
                mapAnalytics(r.priceAnalytics),
            )
        }

        val description = source.description.let {
            listOfNotNull(it.bed, it.surface).joinToString(separator = "*")
        }.takeIf(String::isNotEmpty)

        return Room(
            id = source.id,
            title = source.title,
            description = description,
            perks = perks,
            rate = rate,
            impressionAnalytics = mapAnalytics(source.impressionAnalytics),
            bookAnalytics = mapAnalytics(source.bookAnalytics)
        )
    }
}
