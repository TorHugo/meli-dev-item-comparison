package dev.com.item.compartison.domain.exception

import kotlin.jvm.Transient

/**
 * Abstract base class for domain exception handling.
 * All domain-specific exceptions should extend this class.
 *
 * @param message The base error message.
 * @param args Optional arguments for the error message.
 */
abstract class DomainExceptionHandler protected constructor(
    message: String?,
    /**
     * The arguments associated with the exception message.
     * Marked as transient to be excluded from serialization.
     */
    @Transient vararg val args: Any,
) : RuntimeException(message)
