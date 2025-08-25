package dev.com.item.compartison.infrastructure.api.models.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class ProductResponseDTO(
    val identifier: Long,
    val name: String,
    val description: String,
    val price: BigDecimal,
    @JsonProperty("imagem_url")
    val imageUrl: String,
    val category: String,
    val rating: Double,
    val specifications: Map<String, String>,
    val brand: String,
    val availability: Boolean = true,
    val discount: Double? = null,
    val finalPrice: BigDecimal,
)
