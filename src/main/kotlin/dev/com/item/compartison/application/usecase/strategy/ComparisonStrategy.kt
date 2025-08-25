package dev.com.item.compartison.application.usecase.strategy

import dev.com.item.compartison.domain.entity.ComparisonAggregateRoot
import dev.com.item.compartison.domain.entity.ComparisonRuleDomain
import dev.com.item.compartison.domain.entity.ProductDomain
import dev.com.item.compartison.domain.enums.ComparisonTypeEnum

/**
 * Interface default para estratégias de comparação.
 * **/
interface ComparisonStrategy {
    val type: ComparisonTypeEnum
    val description: String

    /**
     * Função responsável por realizar a comparação entre produtos.
     * Com base na estratégia escolhida.
     * **/
    fun compare(rule: ComparisonRuleDomain, products: List<ProductDomain>): ComparisonAggregateRoot
}