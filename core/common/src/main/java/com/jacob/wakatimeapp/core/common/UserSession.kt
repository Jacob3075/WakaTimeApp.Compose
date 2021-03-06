package com.jacob.wakatimeapp.core.common

import com.jacob.wakatimeapp.core.models.UserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserSession {
    private var _loggedInUser = MutableStateFlow<UserDetails?>(null)
    val loggedInUser: StateFlow<UserDetails?> = _loggedInUser

    fun logInUser(userDetails: UserDetails) {
        _loggedInUser.value = userDetails
    }
}
