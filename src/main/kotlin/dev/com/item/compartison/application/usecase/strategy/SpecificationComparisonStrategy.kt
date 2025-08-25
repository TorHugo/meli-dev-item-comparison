package dev.com.item.compartison.application.usecase.strategy

import dev.com.item.compartison.application.helper.SpecificationComparisonDTO
import dev.com.item.compartison.domain.entity.ComparisonAggregateRoot
import dev.com.item.compartison.domain.entity.ComparisonRankingAg
import dev.com.item.compartison.domain.entity.ComparisonRuleDomain
import dev.com.item.compartison.domain.entity.ComparisonWinnerAg
import dev.com.item.compartison.domain.entity.ProductDomain
import dev.com.item.compartison.domain.enums.ComparisonTypeEnum
import dev.com.item.compartison.domain.exception.template.DomainException
import dev.com.item.compartison.domain.objects.SpecificationRankingVO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SpecificationComparisonStrategy : ComparisonStrategy {
    private val logger = LoggerFactory.getLogger(SpecificationComparisonStrategy::class.java)

    companion object {
        private const val REASON: String = "Especificação técnica."
    }

    override val type: ComparisonTypeEnum = ComparisonTypeEnum.SPECIFICATION
    override val description: String = ComparisonTypeEnum.SPECIFICATION.description

    /**
     * Compara uma lista de produtos com base na especificação técnica.
     *
     * @param products A lista de `ProductDomain` a ser comparada.
     * @return Um `ComparisonDomain` contendo os resultados detalhados da comparação,
     *         incluindo o vencedor (produto mais barato), um ranking e estatísticas de preço.
     */
    override fun compare(
        rule: ComparisonRuleDomain,
        products: List<ProductDomain>,
    ): ComparisonAggregateRoot {
        if ((rule.specificationKey == null || rule.specificationKey.isBlank()) || rule.higherIsBetter == null) {
            throw DomainException(message = "strategy.specification.the.keys.must.be.not.null")
        }

        val productsWithSpecifications =
            products.mapNotNull { product ->
                product.specifications[rule.specificationKey]?.let { specValue ->
                    extractNumericValue(specValue)?.let { numericValue ->
                        SpecificationComparisonDTO(
                            info = product,
                            finalPrice = product.finalPrice(),
                            value = specValue,
                            numericValue = numericValue,
                        )
                    }
                }
            }

        if (productsWithSpecifications.isEmpty()) {
            logger.warn("c=PriceComparisonStrategy m=compare s=Warning - specification list is empty")
            throw DomainException(message = "specification.list.empty")
        }

        val sortedProducts =
            if (rule.higherIsBetter) {
                productsWithSpecifications.sortedByDescending { it.numericValue }
            } else {
                productsWithSpecifications.sortedBy { it.numericValue }
            }

        val ranking =
            sortedProducts.mapIndexed { index, product ->
                ComparisonRankingAg(
                    product = product.info,
                    position = index + 1,
                    details =
                        SpecificationRankingVO(
                            value = product.value,
                            score = product.numericValue,
                        ),
                )
            }

        val winner = sortedProducts.firstOrNull()

        return ComparisonAggregateRoot(
            strategy = this.type.name,
            winner =
                ComparisonWinnerAg(
                    product = winner?.info,
                    reason = REASON,
                ),
            ranking = ranking,
            details = null,
            rule = rule,
        )
    }

    /** Tenta extrair valores numéricos para a comparação. Exemplo: "128GB" -> 128 ou "6.1 polegadas" -> 6.1 **/
    private fun extractNumericValue(value: String): Double? {
        return try {
            val numbers = Regex("""(\d+\.?\d*)""").find(value)
            numbers?.value?.toDouble()
        } catch (exception: NumberFormatException) {
            logger.error("c=PriceComparisonStrategy m=extractNumericValue s=Error - NumberFormatException - message=${exception.message}")
            null
        }
    }
}
