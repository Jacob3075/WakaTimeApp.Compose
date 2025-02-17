package com.jacob.wakatimeapp.core.models

import timber.log.Timber

sealed class Error {
    abstract val message: String
    open val exception: Throwable? = null

    sealed class DomainError : Error() {
        data class InvalidData(override val message: String) : DomainError()
        data class DataRangeTooLarge(override val message: String) : DomainError()
    }

    sealed class NetworkErrors : Error() {
        abstract val statusCode: Int

        data class NoConnection(override val message: String) : NetworkErrors() {
            override val statusCode = -1
        }

        data class Timeout(override val message: String) : NetworkErrors() {
            override val statusCode = -1
        }

        data class GenericError(override val message: String) : NetworkErrors() {
            override val statusCode = -1
        }

        data class ClientError(override val message: String, override val statusCode: Int) :
            NetworkErrors()

        data class ServerError(override val message: String, override val statusCode: Int) :
            NetworkErrors()

        override fun errorDisplayMessage(): String {
            return "$message, status code: $statusCode exception=${exception?.message ?: "none"})"
        }

        companion object {
            @Suppress("MagicNumber")
            fun create(message: String, code: Int? = null): NetworkErrors = when (code) {
                null -> GenericError(message)
                in 400..499 -> ClientError(message, code)
                in 500..599 -> ServerError(message, code)
                else -> {
                    Timber.e("Unknown error code: $code, message: $message")
                    TODO("Handle this case")
                }
            }
        }
    }

    sealed class DatabaseError : Error() {
        data class EmptyCache(override val message: String) : DatabaseError()
        data class NotFound(override val message: String) : DatabaseError()
        data class UnknownError(override val message: String, override val exception: Throwable) :
            DatabaseError()
    }

    data class UnknownError(
        override val message: String,
        override val exception: Throwable? = null,
    ) : Error()

    open fun errorDisplayMessage(): String {
        return "$message, exception=${exception?.message ?: "none"})"
    }
}
