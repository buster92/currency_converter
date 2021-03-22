package com.andresgarrido.currencyconverter.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.andresgarrido.currencyconverter.databinding.FragmentConvertBinding
import com.andresgarrido.currencyconverter.ui.adapter.QuoteExchageAdapter


class ConvertFragment : Fragment() {

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return OverviewViewModel(requireActivity().application) as T
            }
        })[OverviewViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentConvertBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.exchangeRecycler.adapter = QuoteExchageAdapter()
        setHasOptionsMenu(true)
        return binding.root
    }
}