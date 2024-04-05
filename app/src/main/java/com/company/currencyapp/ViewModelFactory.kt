package com.company.currencyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.company.currencyapp.domain.Repository
import com.company.currencyapp.domain.util.AppDispatchers
import com.company.currencyapp.presentation.CurrencyViewModel
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val repository: Repository,
    private val dispatchers: AppDispatchers,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrencyViewModel(repository, dispatchers) as T
    }
}