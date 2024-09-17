package com.dev.freetoplay.data.remote.api

import com.dev.freetoplay.data.remote.dto.GameDetailDto
import com.dev.freetoplay.data.remote.dto.GameDto
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("games")
    suspend fun getAllGames(): List<GameDto>

    @GET("game")
    suspend fun getGame(
        @Query("id") id: Int
    ): GameDetailDto?

    @GET("game")
    suspend fun getGameByPlatform(
        @Query("platform") platform: String
    ): List<GameDto>

    @GET("game")
    suspend fun sortGames(
        @Query("sort-by") criteria: String
    ): List<GameDto>
}