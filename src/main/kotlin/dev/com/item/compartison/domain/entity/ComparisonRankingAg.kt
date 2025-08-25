package dev.com.item.compartison.domain.entity

data class ComparisonRankingAg(
    val product: ProductDomain,
    val position: Int,
    val details: Any? = null
)
