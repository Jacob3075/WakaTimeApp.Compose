package com.jacob.wakatimeapp.core.network

import com.jacob.wakatimeapp.core.common.AuthStateManager
import com.jacob.wakatimeapp.core.network.dtos.AllTimeDataDTO
import com.jacob.wakatimeapp.core.network.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.core.network.dtos.GetLast7DaysStatsResDTO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthorizationService
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import javax.inject.Inject
import javax.inject.Singleton

interface HomePageAPI {
    @GET("/api/v1/users/current/all_time_since_today")
    suspend fun getData(@Header("Authorization") token: String): Response<AllTimeDataDTO>

    @GET("/api/v1/users/current/summaries?range=today")
    suspend fun getStatsForToday(@Header("Authorization") token: String): Response<GetDailyStatsResDTO>

    @GET("/api/v1/users/current/summaries?range=last_7_days")
    suspend fun getLast7DaysStats(@Header("Authorization") token: String): Response<GetLast7DaysStatsResDTO>
}

@Singleton
class HomePageNetworkData @Inject constructor(
    retrofit: Retrofit,
    private val authStateManager: AuthStateManager,
    private val authService: AuthorizationService,
) {
    private val homePageAPI = retrofit.create(HomePageAPI::class.java)

    private val token: String
        get() = runBlocking { authStateManager.getFreshToken(authService).first() }

    suspend fun getLast7DaysStats(): Response<GetLast7DaysStatsResDTO> =
        homePageAPI.getLast7DaysStats("Bearer $token")

    suspend fun getStatsForToday(): Response<GetDailyStatsResDTO> =
        homePageAPI.getStatsForToday("Bearer $token")

    suspend fun getData(): Response<AllTimeDataDTO> =
        homePageAPI.getData("Bearer $token")
}
