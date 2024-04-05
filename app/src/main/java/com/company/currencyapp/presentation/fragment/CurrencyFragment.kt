package com.company.currencyapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.currencyapp.R
import com.company.currencyapp.databinding.FragmentCurrencyBinding
import com.company.currencyapp.domain.util.Resource
import com.company.currencyapp.presentation.CurrencyViewModel
import com.company.currencyapp.presentation.adapter.CurrencyAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class CurrencyFragment : Fragment() {

    private var _binding: FragmentCurrencyBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CurrencyViewModel>()
    private val currencyAdapter = CurrencyAdapter()
    private var updatingJob: Job? = null
    private val formatter = SimpleDateFormat("dd MM yyyy HH:mm:ss", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        observeCurrency()
    }

    override fun onStart() {
        super.onStart()
        updatingJob = viewModel.startUpdates()
    }

    override fun onStop() {
        super.onStop()
        updatingJob?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecycler() {
        binding.recycler.apply {
            adapter = currencyAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeCurrency() {
        observeFlow(viewModel.currency) { result ->
            when (result) {
                is Resource.Success -> {
                    hideProgressBar()
                    currencyAdapter.currencyList = result.data!!.valute
                    val date = formatter.format(Calendar.getInstance().time)
                    binding.lastUpdate.text = requireContext().getString(R.string.last_update, date)
                }

                is Resource.Error -> {
                    hideProgressBar()
                    showSnack(result.message)
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun showSnack(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun <T> observeFlow(flow: StateFlow<T>, collect: (T) -> Unit) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect(collect)
            }
        }
    }
}