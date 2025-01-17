package com.batuhan.kotlincountries.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.batuhan.kotlincountries.model.Country
import com.batuhan.kotlincountries.service.CountryApiService
import com.batuhan.kotlincountries.service.CountryDatabase
import com.batuhan.kotlincountries.util.CustomSharedPreferences
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application : Application) : BaseViewModel(application) {
    private val countryApiService = CountryApiService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refreshData() {

        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getDataFromSQLite()
        } else {
            getDataFromAPI()
        }

    }

    fun refreshFromAPI() {
        getDataFromAPI()
    }

    private fun getDataFromSQLite() {
        countryLoading.value = true
        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(),"Countries From SQLite",Toast.LENGTH_LONG).show()
        }
    }

    private fun getDataFromAPI(){
        countryLoading.value=true

        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        storeInSqlLite(t)
                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryError.value = true
                        e.printStackTrace()
                    }

                })
        )
    }
    private fun showCountries(countryList : List<Country>){
        countries.value = countryList
        countryError.value = false
        countryLoading.value = false
    }
    private fun storeInSqlLite(list : List<Country>){
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            val listeLong = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i<list.size){
                list[i].uuid = listeLong[i].toInt()
                i = i+1
            }
            showCountries(list)
        }
        customPreferences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}