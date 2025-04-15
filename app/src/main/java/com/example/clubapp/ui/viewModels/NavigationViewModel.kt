package com.example.clubapp.ui.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NavigationViewModel : ViewModel() {
    var selectedItemIndex = mutableStateOf(0)
        private set

    fun updateSelectedItemIndex(index: Int) {
        selectedItemIndex.value = index
    }
}