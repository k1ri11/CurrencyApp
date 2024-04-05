package com.company.currencyapp.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.company.currencyapp.R
import com.company.currencyapp.data.CurrencyApi
import com.company.currencyapp.domain.model.toCurrencyUI
import com.company.currencyapp.domain.util.AppDispatchers
import com.company.currencyapp.domain.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class Repository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: CurrencyApi,
    private val dispatchers: AppDispatchers,
) {

    fun getCurrency() = flow {
        if (hasInternetConnection()) {
            val response = api.getCurrencies()
            if (response.isSuccessful) {
                val currency = response.body()!!.toCurrencyUI()
                emit(Resource.Success(currency))
            }
            when (response.code()) {
                400 -> emit(Resource.Error(context.resources.getString(R.string.incorrect_request)))
                401 -> emit(Resource.Error(context.resources.getString(R.string.incorrect_authorization)))
                404 -> emit(Resource.Error(context.resources.getString(R.string.element_not_found)))
                500 -> emit(Resource.Error(context.resources.getString(R.string.server_error)))
            }
        } else {
            emit(Resource.Error(context.getString(R.string.no_connection)))
        }
    }.flowOn(dispatchers.io)


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}