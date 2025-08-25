package dev.com.item.compartison.application.helper

import dev.com.item.compartison.domain.entity.ProductDomain
import java.math.BigDecimal

data class ComparisonDTO(
    val info: ProductDomain,
    val finalPrice: BigDecimal
)
