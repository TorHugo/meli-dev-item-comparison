package dev.com.item.compartison.infrastructure.api.models.response

data class ExceptionDataDTO(
    val error: String,
    val message: String?,
    val path: String,
)
