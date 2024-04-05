package com.company.currencyapp.domain.model

import com.company.currencyapp.data.model.Currency


data class CurrencyUI(
    val date: String,
    val previousDate: String,
    val previousURL: String,
    val timestamp: String,
    val valute: List<ValuteUI>
)

fun Currency.toCurrencyUI(): CurrencyUI {
    return CurrencyUI(
        date = this.date,
        previousDate = this.previousDate,
        previousURL = this.previousURL,
        timestamp = this.timestamp,
        valute = valute.map { it.value.toValuteUI() },
    )
}
