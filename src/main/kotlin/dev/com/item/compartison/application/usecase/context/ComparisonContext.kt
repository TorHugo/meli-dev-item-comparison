package dev.com.item.compartison.application.usecase.context

import dev.com.item.compartison.application.usecase.strategy.ComparisonStrategy
import dev.com.item.compartison.domain.entity.ComparisonAggregateRoot
import dev.com.item.compartison.domain.entity.ComparisonRuleDomain
import dev.com.item.compartison.domain.entity.ProductDomain
import dev.com.item.compartison.domain.enums.ComparisonTypeEnum
import dev.com.item.compartison.domain.exception.template.IllegalArgumentInternalException
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class ComparisonContext(
    private val strategyImplementations: List<ComparisonStrategy>,
) {
    private val strategies = mutableMapOf<ComparisonTypeEnum, ComparisonStrategy>()

    @PostConstruct
    fun initStrategies() {
        strategyImplementations.forEach { strategy ->
            strategies[strategy.type] = strategy
        }
    }

    /**
     * Executa a estratégia de comparação com base no tipo fornecido.
     *
     * @param type O tipo de comparação a ser executada.
     * @param input Os dados necessários para a comparação.
     * @return O resultado da comparação.
     * @throws IllegalArgumentInternalException se nenhuma estratégia for encontrada para o tipo.
     */
    fun execute(
        type: ComparisonTypeEnum,
        rule: ComparisonRuleDomain,
        input: List<ProductDomain>,
    ): ComparisonAggregateRoot {
        val strategy =
            strategies[type]
                ?: throw IllegalArgumentInternalException("strategy.not.found")

        return strategy.compare(
            rule = rule,
            products = input,
        )
    }
}
