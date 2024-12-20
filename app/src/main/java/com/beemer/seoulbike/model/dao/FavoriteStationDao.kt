package com.beemer.seoulbike.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.beemer.seoulbike.model.entity.FavoriteStationEntity

@Dao
interface FavoriteStationDao {
    @Query("SELECT * FROM favorite")
    fun getAllFavoriteStation(): LiveData<List<FavoriteStationEntity>>

    @Query("SELECT * FROM favorite WHERE stationId = :stationId LIMIT 1")
    suspend fun getFavoriteStationByStationId(stationId: String): FavoriteStationEntity?

    @Insert
    suspend fun insertFavoriteStation(favoriteStation: FavoriteStationEntity)

    @Query("DELETE FROM favorite WHERE stationId = :stationId")
    suspend fun deleteFavoriteStationByStationId(stationId: String)
}