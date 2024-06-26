package com.jacob.wakatimeapp.core.common.auth

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenResponse

/**
 * An example persistence mechanism for an [AuthState] instance.
 * This stores the instance in a shared preferences file, and provides thread-safe access and
 * mutation.
 *
 * [Initial Reference](https://github.com/openid/AppAuth-Android/blob/master/app/java/net/openid/appauthdemo/AuthStateManager.java)
 */
@Singleton
class AuthTokenProvider @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val authService: AuthorizationService,
) {

    var current = runBlocking { authDataStore.getAuthState() }

    private suspend fun update(state: AuthState) {
        authDataStore.updateAuthState(state)
        current = state
    }

    suspend fun updateAfterTokenResponse(
        response: TokenResponse?,
        ex: AuthorizationException?,
    ): AuthState {
        val current = current
        current.update(response, ex)
        update(current)
        return current
    }

    fun getFreshToken() = callbackFlow {
        val current = current

        if (!current.needsTokenRefresh) {
            send(current.accessToken)
            channel.close()
            return@callbackFlow
        }

        current.performActionWithFreshTokens(authService) { _, _, _ ->
            runBlocking {
                update(current)
                send(current.accessToken)
                channel.close()
            }
        }
    }
        .filterNotNull()
        .take(1)

    fun logout() = runBlocking {
        update(AuthState())
    }
}
