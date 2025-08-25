package dev.com.item.compartison.domain.entity

import dev.com.item.compartison.domain.utils.DomainEntity
import java.math.BigDecimal
import java.math.RoundingMode

data class ProductDomain(
    override val identifier: Long,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val imageUrl: String,
    val category: String,
    val rating: Double,
    val specifications: Map<String, String>,
    val brand: String,
    val availability: Boolean = true,
    /** The discount percentage. e.g., 15.0 for 15% */
    val discount: Double? = null,
) : DomainEntity<Long>() {
    companion object {
        private val HUNDRED = BigDecimal(100)
        private const val PRICE_SCALE = 2
    }

    /**
     * Checks if the product is currently on sale.
     * A product is considered on sale if the discount is a positive value.
     *
     * @return `true` if the product has a valid discount, `false` otherwise.
     */
    fun isOnSale(): Boolean {
        return discount != null && discount > 0
    }

    /**
     * Calculates the final price of the product, applying any valid discount.
     *
     * @return The final price as a [BigDecimal]. If the product is not on sale,
     * returns the original price.
     */
    fun getFinalPrice(): BigDecimal {
        // If the product is not on sale, return the original price.
        if (!isOnSale()) {
            return price
        }

        // Calculate the discount factor (e.g., for a 20% discount, this will be 0.20)
        val discountFactor =
            BigDecimal.valueOf(discount!!)
                .divide(HUNDRED, PRICE_SCALE, RoundingMode.HALF_UP)

        // Calculate the discount amount
        val discountAmount = price.multiply(discountFactor)

        // Subtract the discount amount from the original price to get the final price
        return price.subtract(discountAmount).setScale(PRICE_SCALE, RoundingMode.HALF_UP)
    }
}
