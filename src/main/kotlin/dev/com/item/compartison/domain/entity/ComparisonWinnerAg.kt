package dev.com.item.compartison.domain.entity

/** Aggregate Root que possúi a informação do vencedor da comparação. **/
data class ComparisonWinnerAg(
    val product: ProductDomain?,
    val reason: String,
)
