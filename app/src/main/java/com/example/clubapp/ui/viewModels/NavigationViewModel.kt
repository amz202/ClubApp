package com.example.clubapp.ui.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/*
 * Copyright 2025 Abdul Majid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class NavigationViewModel : ViewModel() {
    var selectedItemIndex = mutableStateOf(0)
        private set

    private val _showEventRoleDialog = MutableStateFlow<Boolean>(false)
    val showEventRoleDialog: MutableStateFlow<Boolean> = _showEventRoleDialog
    private val _eventRoleUser = MutableStateFlow<String>("")
    val eventRoleUser: MutableStateFlow<String> = _eventRoleUser

    private val _showAddEventNewsDialog = MutableStateFlow<Boolean>(false)
    val showAddEventNewsDialog: MutableStateFlow<Boolean> = _showAddEventNewsDialog
    private val _eventNewsId = MutableStateFlow<String>("")
    val eventNewsId: MutableStateFlow<String> = _eventNewsId

    fun showEventRoleDialog(userId: String) {
        _showEventRoleDialog.value = true
        _eventRoleUser.value = userId
    }
    fun hideEventRoleDialog() {
        _showEventRoleDialog.value = false
    }
    fun showAddEventNewsDialog(eventId:String) {
        _showAddEventNewsDialog.value = true
        _eventNewsId.value = eventId
    }
    fun hideAddEventNewsDialog(eventId:String) {
        _showAddEventNewsDialog.value = false
        _eventNewsId.value = eventId
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

    private val _showClubOpenDialog = MutableStateFlow<Boolean>(false)
    val showClubOpenDialog: MutableStateFlow<Boolean> = _showClubOpenDialog

    fun showClubOpenDialog(){
        _showClubOpenDialog.value = true
    }

    fun hideClubOpenDialog(){
        _showClubOpenDialog.value = false
    }

    private val _showAcceptUserDialog = MutableStateFlow<Boolean>(false)
    val showAcceptUserDialog: MutableStateFlow<Boolean> = _showAcceptUserDialog
    private val _requestUserName = MutableStateFlow<String>("")
    val requestUserName: MutableStateFlow<String> = _requestUserName
    private val _requestUserId = MutableStateFlow<String>("")
    val requestUserId: MutableStateFlow<String> = _requestUserId

    fun showAcceptUserDialog(userName: String, userId: String) {
        _showAcceptUserDialog.value = true
        _requestUserName.value = userName
        _requestUserId.value = userId
    }
    fun hideAcceptUserDialog() {
        _showAcceptUserDialog.value = false
    }

    private val _showRejectUserDialog = MutableStateFlow<Boolean>(false)
    val showRejectUserDialog: MutableStateFlow<Boolean> = _showRejectUserDialog

    fun showRejectUserDialog(userName: String, userId: String) {
        _showRejectUserDialog.value = true
        _requestUserName.value = userName
        _requestUserId.value = userId
    }
    fun hideRejectUserDialog() {
        _showRejectUserDialog.value = false
    }

}