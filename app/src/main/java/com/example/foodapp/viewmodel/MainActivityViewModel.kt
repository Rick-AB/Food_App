package com.example.foodapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Food
import com.example.foodapp.repository.FoodRepository

class MainActivityViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var foodRepo: FoodRepository
    private lateinit var foodListData: MediatorLiveData<List<Food>>
    private lateinit var isLoading: MediatorLiveData<Boolean>

    //create instance repo to get data
    fun init() {
        foodListData = MediatorLiveData()
        foodRepo = FoodRepository.getInstance()
        isLoading = MediatorLiveData()
    }

    //returns data to be observed
    fun getFoodList(): LiveData<List<Food>> {
       foodListData.addSource(foodRepo.getFoodList(getApplication() as Context)) {
           foodListData.postValue(it)
       }
        return foodListData
    }

    fun isLoading(): LiveData<Boolean> {
        isLoading.addSource(foodRepo.isLoading) {
            isLoading.postValue(it)
        }
        return isLoading
    }
}