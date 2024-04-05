package com.company.currencyapp.data

import com.company.currencyapp.data.model.Currency
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {

    @GET("daily_json.js")
    suspend fun getCurrencies(): Response<Currency>
}