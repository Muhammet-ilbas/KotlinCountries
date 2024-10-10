package com.batuhan.kotlincountries.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.batuhan.kotlincountries.databinding.FragmentCountryBinding
import com.batuhan.kotlincountries.util.downloadFromUrl
import com.batuhan.kotlincountries.util.placeholderProgressBar
import com.batuhan.kotlincountries.viewmodel.CountryViewModel

class CountryFragment : Fragment() {

    private lateinit var viewModel: CountryViewModel
    private var countryUuid = 0
    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View Binding ile Fragment layout'ını oluştur
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            countryUuid = CountryFragmentArgs.fromBundle(it).countryUuid
        }

        viewModel = ViewModelProvider(this).get(CountryViewModel::class.java)
        viewModel.getDataFromRoom(countryUuid)



        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->
            country?.let {
                binding.selectedCountry = country
                /*
                binding.countryName.text = country.countryName
                binding.countryCapital.text = country.countryCapital
                binding.countryCurrency.text = country.countryCurrency
                binding.countryLanguage.text = country.countryLanguage
                binding.countryRegion.text = country.countryRegion
                context?.let {
                    binding.countryImage.downloadFromUrl(country.imageUrl, placeholderProgressBar(it))
                }

                 */
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Memory leak önlemek için binding referansını temizle
    }
}
