package com.batuhan.kotlincountries.service

import com.batuhan.kotlincountries.model.Country
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface CountryAPI {

    //https://raw.githubusercontent.com/

    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")

    fun getCountries(): Single<List<Country>>

}