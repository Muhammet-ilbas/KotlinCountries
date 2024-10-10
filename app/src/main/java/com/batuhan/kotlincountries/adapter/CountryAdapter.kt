package com.batuhan.kotlincountries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.kotlincountries.R
import com.batuhan.kotlincountries.databinding.ItemCountryBinding
import com.batuhan.kotlincountries.model.Country

import com.batuhan.kotlincountries.view.FeedFragmentDirections

class CountryAdapter(val countryList: ArrayList<Country>): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(),CountryClickListener {



    class CountryViewHolder(var view: ItemCountryBinding) : RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.item_country,parent,false)
        val view = DataBindingUtil.inflate<ItemCountryBinding>(inflater,R.layout.item_country,parent,false)
        return CountryViewHolder(view)

    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        holder.view.country = countryList[position]
        holder.view.listener = this

        /*


        holder.view.name.text = countryList[position].countryName
        holder.view.region.text = countryList[position].countryRegion

        holder.view.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)
            //action.countryUuid
            Navigation.findNavController(it).navigate(action)
        }

        holder.view.imageView.downloadFromUrl(countryList[position].imageUrl, placeholderProgressBar(holder.view.context))
 */
    }


    fun updateCountryList(newCountryList: List<Country>) {
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }

    override fun onCountryClicked(v: View) {
        // view'den countryUuidText öğesini bul
        val countryUuidTextView = v.findViewById<TextView>(R.id.countryUuidText)

        // TextView'den UUID'yi al
        val uuid = countryUuidTextView.text.toString().toIntOrNull() ?: return

        // Navigasyon işlemini gerçekleştirin
        val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(uuid)
        Navigation.findNavController(v).navigate(action)
    }


}