package com.example.clubapp.ui.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class NavigationViewModel : ViewModel() {
    var selectedItemIndex = mutableStateOf(0)
        private set

    private val _showEventRoleDialog = MutableStateFlow<Boolean>(false)
    val showEventRoleDialog: MutableStateFlow<Boolean> = _showEventRoleDialog
    private val _eventRoleUser = MutableStateFlow<String>("")
    val eventRoleUser: MutableStateFlow<String> = _eventRoleUser

    fun showEventRoleDialog(userId: String) {
        _showEventRoleDialog.value = true
        _eventRoleUser.value = userId
    }
    fun hideEventRoleDialog() {
        _showEventRoleDialog.value = false
    }

    private val _showClubRoleDialog = MutableStateFlow<Boolean>(false)
    val showClubRoleDialog: MutableStateFlow<Boolean> = _showClubRoleDialog
    private val _clubRoleUser = MutableStateFlow<String>("")
    val clubRoleUser: MutableStateFlow<String> = _clubRoleUser

    fun showClubRoleDialog(userId: String) {
        _showClubRoleDialog.value = true
        _clubRoleUser.value = userId
    }
    fun hideClubRoleDialog() {
        _showClubRoleDialog.value = false
    }

    fun updateSelectedItemIndex(index: Int) {
        selectedItemIndex.value = index
    }
}