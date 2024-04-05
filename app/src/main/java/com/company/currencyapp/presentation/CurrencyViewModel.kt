package com.company.currencyapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.currencyapp.domain.Repository
import com.company.currencyapp.domain.model.CurrencyUI
import com.company.currencyapp.domain.util.AppDispatchers
import com.company.currencyapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: Repository,
    private val dispatchers: AppDispatchers,
) : ViewModel() {

    private val _currency = MutableStateFlow<Resource<CurrencyUI>>(Resource.Loading())
    val currency: StateFlow<Resource<CurrencyUI>> = _currency

    private fun getCurrency() = viewModelScope.launch {
        repository.getCurrency().collect { result ->
            _currency.value = result
        }
    }

    fun startUpdates() = viewModelScope.launch(dispatchers.io) {
        while (true) {
            getCurrency()
            delay(30000)
        }
    }

}