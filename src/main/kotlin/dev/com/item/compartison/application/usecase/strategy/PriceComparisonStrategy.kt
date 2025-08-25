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
import java.math.BigDecimal

@Component
class PriceComparisonStrategy : ComparisonStrategy {
    private val logger = LoggerFactory.getLogger(PriceComparisonStrategy::class.java)

    companion object {
        private const val REASON: String = "Preço."
    }

    override val type: ComparisonTypeEnum = ComparisonTypeEnum.PRICE
    override val description: String = ComparisonTypeEnum.PRICE.description

    /**
     * Compara uma lista de produtos com base no seu preço final (considerando descontos),
     * identificando o mais barato, o mais caro e gerando um ranking de preços.
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

        val lowerIsBetter = rule.higherIsBetter?.not() ?: true

        val sortedProducts =
            if (lowerIsBetter) {
                /** Realiza a ordenação da lista do menor para o maior preço. **/
                productsWithFinalPrice.sortedBy { it.finalPrice }
            } else {
                /** Realiza a ordenação da lista do maior para o menor preço. **/
                productsWithFinalPrice.sortedByDescending { it.finalPrice }
            }

        val winner = sortedProducts.first()

        val ranking =
            sortedProducts.mapIndexed { index, product ->
                ComparisonRankingAg(
                    product = product.info,
                    position = index + 1,
                )
            }

        val averagePrice =
            sortedProducts.map { it.finalPrice }.let { prices ->
                if (prices.isEmpty()) {
                    BigDecimal.ZERO
                } else {
                    prices.reduce { acc, price -> acc + price } / prices.size.toBigDecimal()
                }
            }

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
                            value = averagePrice,
                        ),
                ),
        )
    }
}
