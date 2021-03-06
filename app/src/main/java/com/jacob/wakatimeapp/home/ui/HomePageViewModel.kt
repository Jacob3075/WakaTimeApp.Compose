package com.jacob.wakatimeapp.home.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.wakatimeapp.core.database.OfflineDataStore
import com.jacob.wakatimeapp.core.models.ErrorTypes
import com.jacob.wakatimeapp.core.models.Result
import com.jacob.wakatimeapp.core.common.AuthStateManager
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationService
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomePageViewModel @Inject constructor(
    application: Application,
    offlineDataStore: com.jacob.wakatimeapp.core.database.OfflineDataStore,
    private val ioDispatcher: CoroutineContext,
    private val getLast7DaysStatsUC: GetLast7DaysStatsUC,
    private val authStateManager: com.jacob.wakatimeapp.core.common.AuthStateManager,
) : AndroidViewModel(application) {
    private val authService = AuthorizationService(getApplication())

    private val _homePageState =
        MutableStateFlow<HomePageViewState>(HomePageViewState.Loading)
    val homePageState = _homePageState.asStateFlow()

    private val _errors = MutableSharedFlow<HomePageViewState.Error>()
    val errors = _errors.asSharedFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            val result = getLast7DaysStatsUC.invoke()
            val userDetails = offlineDataStore.getUserDetails().filterNotNull().first()

            when (result) {
                is com.jacob.wakatimeapp.core.models.Result.Success -> {
                    _homePageState.value = HomePageViewState.Loaded(
                        result.value,
                        userDetails,
                    )
                }
                is com.jacob.wakatimeapp.core.models.Result.Failure -> {
                    val error = getErrorMessage(result)
                    _homePageState.value = error
                    _errors.emit(error)
                }
                is com.jacob.wakatimeapp.core.models.Result.Empty -> Timber.e("EMPTY")
            }
        }
    }

    private fun getErrorMessage(last7DaysStatsResult: com.jacob.wakatimeapp.core.models.Result.Failure): HomePageViewState.Error =
        when (val errorType = last7DaysStatsResult.errorHolder) {
            is com.jacob.wakatimeapp.core.models.ErrorTypes.NetworkError -> HomePageViewState.Error(errorType.throwable.message ?: "")
        }
}
