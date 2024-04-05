package com.company.currencyapp.domain.model

import com.company.currencyapp.data.model.Valute

data class ValuteUI(
    val charCode: String,
    val id: String,
    val name: String,
    val nominal: Int,
    val numCode: String,
    val previous: Double,
    val value: String
)

fun Valute.toValuteUI(): ValuteUI {
    return ValuteUI(
        charCode = this.charCode,
        id = this.id,
        name = this.name,
        nominal = this.nominal,
        numCode = this.numCode,
        previous = this.previous,
        value = String.format("%.4f", value),
    )
}