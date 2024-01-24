package com.example.madcamp_week4_fe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _favoritesUpdated = MutableLiveData<Boolean>()
    val favoritesUpdated: LiveData<Boolean> = _favoritesUpdated

    fun setFavoritesUpdated(isUpdated: Boolean) {
        _favoritesUpdated.value = isUpdated
    }
}