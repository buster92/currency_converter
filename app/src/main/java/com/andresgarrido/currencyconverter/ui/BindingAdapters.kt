package com.andresgarrido.currencyconverter.ui

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.andresgarrido.currencyconverter.model.QuoteItem
import com.andresgarrido.currencyconverter.ui.adapter.CurrencyListAdapter
import com.andresgarrido.currencyconverter.ui.adapter.QuoteExchageAdapter


/**
 * fill the Spinner with all available projects.
 * Set the Spinner selection to selectedProject.
 * If the selection changes, call the InverseBindingAdapter
 */
@BindingAdapter(
    value = ["currencyList", "selectedCurrency", "selectedCurrencyAttrChanged"],
    requireAll = false
)
fun setCurrencyList(
    spinner: Spinner,
    currencyList: List<String>?,
    selectedCurrency: String?,
    listener: InverseBindingListener
) {
    if (currencyList.isNullOrEmpty()) return
    if (spinner.adapter == null) {
        spinner.adapter = CurrencyListAdapter(
            spinner.context,
            android.R.layout.simple_spinner_dropdown_item,
            currencyList
        )
        setCurrentSelection(spinner, "USD")
        setSpinnerListener(spinner, listener)
    }

    if (!selectedCurrency.isNullOrEmpty()) {
        setCurrentSelection(spinner, selectedCurrency)
    }
}

private fun setSpinnerListener(spinner: Spinner, listener: InverseBindingListener) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) = listener.onChange()

        override fun onNothingSelected(adapterView: AdapterView<*>) = listener.onChange()
    }
}

private fun setCurrentSelection(spinner: Spinner, selectedCurrency: String): Boolean {
    if (spinner.adapter == null)
        return false
    for (index in 0 until spinner.adapter.count) {
        if (spinner.getItemAtPosition(index) == selectedCurrency) {
            spinner.setSelection(index)
            return true
        }
    }
    return false
}

@InverseBindingAdapter(attribute = "selectedCurrency")
fun getSelectedCurrency(spinner: Spinner): String {
    return spinner.selectedItem as String
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<QuoteItem>?) {
    Log.d("BInd", "bindRecyclerView")
    val adapter = recyclerView.adapter as QuoteExchageAdapter
    adapter.submitList(data)
}
