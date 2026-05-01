package io.github.ilyadreamix.inpostinternshiptask.domain.exceptions

internal open class AppException(cause: Throwable? = null, message: String? = null) : Exception(message, cause) {
  constructor(message: String) : this(cause = null, message)
}
