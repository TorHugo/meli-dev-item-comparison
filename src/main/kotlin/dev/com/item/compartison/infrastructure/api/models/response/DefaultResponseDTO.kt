package dev.com.item.compartison.infrastructure.api.models.response

import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A generic response DTO that wraps API responses with standard information.
 *
 * @param T The type of data being wrapped.
 * @property status The HTTP status code of the response.
 * @property data The actual payload of the response.
 * @property timestamp The ISO-formatted timestamp when the response was generated.
 */
data class DefaultResponseDTO<T>(
    val status: Int,
    val data: T,
    val timestamp: String,
) {
    /**
     * Creates a response with status and data, automatically setting the current timestamp.
     *
     * @param status HTTP status code.
     * @param data Response payload.
     */
    constructor(status: Int, data: T) : this(
        status,
        data,
        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    )

    companion object {
        private const val DEFAULT_SUCCESSFULLY_RESPONSE = "Successfully in the request. No given response."

        /**
         * Creates a successful response (HTTP 200) with the given data.
         *
         * @param T Type of the response data.
         * @param data Response payload.
         * @return A DefaultResponseDTO instance with status 200.
         */
        @JvmStatic
        fun <T> success(data: T): DefaultResponseDTO<T> {
            return DefaultResponseDTO(HttpStatus.OK.value(), data)
        }

        /**
         * Creates a response for a newly created resource (HTTP 201) with the given data.
         *
         * @param T Type of the response data.
         * @param data Response payload.
         * @return A DefaultResponseDTO instance with status 201.
         */
        @JvmStatic
        fun <T> created(data: T): DefaultResponseDTO<T> {
            return DefaultResponseDTO(HttpStatus.CREATED.value(), data)
        }

        /**
         * Creates a response for a newly created resource (HTTP 201) with a default message.
         *
         * @return A DefaultResponseDTO instance with status 201.
         */
        @JvmStatic
        fun created(): DefaultResponseDTO<DefaultDataDTO> {
            val data = DefaultDataDTO(DEFAULT_SUCCESSFULLY_RESPONSE)
            return DefaultResponseDTO(HttpStatus.CREATED.value(), data)
        }

        /**
         * Creates a successful response (HTTP 200) with a default message.
         *
         * @return A DefaultResponseDTO instance with status 200.
         */
        @JvmStatic
        fun ok(): DefaultResponseDTO<DefaultDataDTO> {
            val data = DefaultDataDTO(DEFAULT_SUCCESSFULLY_RESPONSE)
            return DefaultResponseDTO(HttpStatus.OK.value(), data)
        }
    }
}
