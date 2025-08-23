package dev.com.item.compartison.domain.utils

data class PageInfoGenericUtils<T> (
    val content: List<T>,
    val number: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
