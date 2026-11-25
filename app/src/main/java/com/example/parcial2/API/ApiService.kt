package com.example.parcial2.API

import com.example.parcial2.Model.Category
import com.example.parcial2.Model.Expense
import com.example.parcial2.Model.NewExpense
import com.example.parcial2.Model.NewTrip
import com.example.parcial2.Model.Trip
import com.example.parcial2.Model.TripDetail
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/trips")
    suspend fun getTrips(): List<Trip>

    @POST("api/trips")
    suspend fun createTrip(@Body trip: NewTrip): Response<Trip>

    @GET("api/trips/{id}")
    suspend fun getTripDetail(@Path("id") id: Int): TripDetail

    @GET("api/expenses")
    suspend fun getExpenses(@Query("tripId") tripId: Int): List<Expense>

    @POST("api/expenses")
    suspend fun createExpense(@Body expense: NewExpense): Response<Expense>

    @GET("api/categories")
    suspend fun getCategories(): List<Category>
}

// El backend responde { "message": "..." } cuando hay error
data class ApiError(val message: String?)