package dev.com.item.compartison.domain.utils

import dev.com.item.compartison.domain.enums.SortDirectionEnum

data class PaginationUtils(
    val number: Int,
    val size: Int,
    val sortBy: String,
    val direction: SortDirectionEnum
)
