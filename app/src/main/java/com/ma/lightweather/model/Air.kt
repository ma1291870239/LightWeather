package com.ma.lightweather.model

data class Air(
        var status:String="",
        var air_now_city: NowAir= NowAir(),
        var basic: BasicAir =BasicAir(),
        var update: UpdateAir = UpdateAir(),
        var air_now_station: List<NowStationAir> = arrayListOf()
) {
    data class NowAir(
            var aqi: String="",
            var qlty: String="",
            var main: String="",
            var pm25: String="",
            var pm10: String="",
            var no2: String="",
            var so2: String="",
            var co: String="",
            var o3: String="",
            var pub_time: String=""
    )

    data class BasicAir(
            var cid: String="",
            var location: String="",
            var parent_city: String="",
            var admin_area: String="",
            var cnty: String="",
            var lat: String="",
            var lon: String="",
            var tz: String=""
    )

    data class UpdateAir(
            var loc: String="",
            var utc: String=""
    )

    data class NowStationAir(
            var air_sta: String="",
            var aqi: String="",
            var asid: String="",
            var co: String="",
            var lat: String="",
            var lon: String="",
            var main: String="",
            var no2: String="",
            var o3: String="",
            var pm10: String="",
            var pm25: String="",
            var pub_time: String="",
            var qlty: String="",
            var so2: String=""
    )

}