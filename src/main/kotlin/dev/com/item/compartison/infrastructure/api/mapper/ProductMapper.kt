package dev.com.item.compartison.infrastructure.api.mapper

import dev.com.item.compartison.domain.entity.ProductDomain
import dev.com.item.compartison.infrastructure.api.models.response.ProductResponseDTO

fun ProductDomain.toResponseDTO(): ProductResponseDTO {
    return ProductResponseDTO(
        identifier = this.identifier,
        name = this.name,
        description = this.description,
        price = this.price,
        imageUrl = this.imageUrl,
        category = this.category,
        rating = this.rating,
        specifications = this.specifications,
        brand = this.brand,
        availability = this.availability,
        discount = this.discount,
        finalPrice = this.getFinalPrice(),
    )
}
