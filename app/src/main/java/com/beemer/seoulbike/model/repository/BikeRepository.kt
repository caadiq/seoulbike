package com.beemer.seoulbike.model.repository

import com.beemer.seoulbike.model.api.BikeApi
import com.beemer.seoulbike.model.dto.NearbyStationListDto
import com.beemer.seoulbike.model.utils.ApiUtils
import retrofit2.Retrofit
import javax.inject.Inject

class BikeRepository @Inject constructor(retrofit: Retrofit) {
    private val bikeApi: BikeApi = retrofit.create(BikeApi::class.java)

    suspend fun getNearByStations(lat: Double, lon: Double, distance: Double): ApiUtils.Results<List<NearbyStationListDto>> {
        return ApiUtils.safeApiCall(bikeApi.getNearByStations(lat, lon, distance))
    }
}