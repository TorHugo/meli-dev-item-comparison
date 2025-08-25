package dev.com.item.compartison.application.usecase.strategy

import dev.com.item.compartison.application.helper.ComparisonDTO
import dev.com.item.compartison.domain.entity.ComparisonAggregateRoot
import dev.com.item.compartison.domain.entity.ComparisonDetailAg
import dev.com.item.compartison.domain.entity.ComparisonRankingAg
import dev.com.item.compartison.domain.entity.ComparisonRuleDomain
import dev.com.item.compartison.domain.entity.ComparisonWinnerAg
import dev.com.item.compartison.domain.entity.ProductDomain
import dev.com.item.compartison.domain.enums.ComparisonTypeEnum
import dev.com.item.compartison.domain.objects.ComparisonAverageVO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class RatingComparisonStrategy : ComparisonStrategy {
    private val logger = LoggerFactory.getLogger(RatingComparisonStrategy::class.java)

    companion object {
        private const val REASON: String = "Avaliação."
    }

    override val type: ComparisonTypeEnum = ComparisonTypeEnum.RATING
    override val description: String = ComparisonTypeEnum.RATING.description

    /**
     * Compara uma lista de produtos com base na sua avaliação.
     *
     * @param products A lista de `ProductDomain` a ser comparada.
     * @return Um `ComparisonDomain` contendo os resultados detalhados da comparação,
     *         incluindo o vencedor (produto mais barato), um ranking e estatísticas de preço.
     */
    override fun compare(
        rule: ComparisonRuleDomain,
        products: List<ProductDomain>,
    ): ComparisonAggregateRoot {
        val productsWithFinalPrice =
            products.map { product ->
                ComparisonDTO(
                    info = product,
                    finalPrice = product.finalPrice(),
                )
            }

        val isHigherBetter = rule.higherIsBetter ?: true

        val sortedProducts =
            if (isHigherBetter) {
                /** Realiza a ordenação da lista do maior para a menor avaliação. **/
                productsWithFinalPrice.sortedByDescending { it.info.rating }
            } else {
                /** Realiza a ordenação da lista da menor para a maior avaliação. **/
                productsWithFinalPrice.sortedBy { it.info.rating }
            }

        val winner = sortedProducts.first()

        val ranking =
            sortedProducts.mapIndexed { index, product ->
                ComparisonRankingAg(
                    product = product.info,
                    position = index + 1,
                )
            }

        val averageRating =
            sortedProducts.map {
                it.info.rating
            }.average()

        return ComparisonAggregateRoot(
            strategy = this.type.name,
            winner =
                ComparisonWinnerAg(
                    product = winner.info,
                    reason = REASON,
                ),
            ranking = ranking,
            rule = rule,
            details =
                ComparisonDetailAg(
                    average =
                        ComparisonAverageVO(
                            value = averageRating,
                        ),
                ),
        )
    }
}
