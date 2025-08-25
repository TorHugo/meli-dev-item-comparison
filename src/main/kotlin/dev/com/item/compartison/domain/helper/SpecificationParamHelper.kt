package dev.com.item.compartison.domain.helper

/**
 * Categorias para pré filtragem dos produtos.
 * **/
data class SpecificationParamHelper(
    val category: String,
    val brand: String? = null,
    val name: String? = null,
    val discount: Double? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    /** Outras categorias para filtro. **/
)
