package com.example.project.core.error

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val exception: AppError) : Resource<T>()
}

sealed class AppError : Exception() {
    object NetworkError : AppError()
    object DatabaseError : AppError()
    object AiServiceUnavailable : AppError()
    object RateLimitError : AppError()
    data class UnknownError(override val message: String?) : AppError()
}
