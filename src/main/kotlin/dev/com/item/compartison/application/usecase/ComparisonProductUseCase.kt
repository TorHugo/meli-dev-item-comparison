package dev.com.item.compartison.application.usecase

import dev.com.item.compartison.application.usecase.context.ComparisonContext
import dev.com.item.compartison.domain.entity.ComparisonAggregateRoot
import dev.com.item.compartison.domain.entity.ComparisonRuleDomain
import dev.com.item.compartison.domain.enums.ComparisonTypeEnum
import dev.com.item.compartison.domain.exception.template.DomainException
import dev.com.item.compartison.domain.exception.template.GenericException
import dev.com.item.compartison.domain.exception.template.IllegalArgumentInternalException
import dev.com.item.compartison.domain.gateway.ProductGateway
import dev.com.item.compartison.domain.helper.SpecificationParamHelper
import dev.com.item.compartison.infrastructure.adapter.LoadingProductAdapter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ComparisonProductUseCase(
    private val strategyContext: ComparisonContext,
    private val productGateway: ProductGateway,
) {
    private val logger = LoggerFactory.getLogger(LoadingProductAdapter::class.java)

    /**
     * Permite realizar a comparação de produtos por alguma especificidade solicitada.
     *
     * Primeiro é realizado uma pré-filtragem dos dados adicionando a responsabilidade disso ao `database`.
     * Posteriormente, é realizado a estrategia de `sorting` com base no tipo solicitado e na regra solicitada.
     *
     * @return Um `ComparisonAggregateRoot` contendo os resultados detalhados da comparação,
     *         incluindo o vencedor (produto mais barato), um ranking e estatísticas de preço.
     * **/
    fun execute(
        type: ComparisonTypeEnum,
        rule: ComparisonRuleDomain,
        specification: SpecificationParamHelper,
    ): ComparisonAggregateRoot {
        try {
            logger.info("c=ComparisonProductsUseCase m=execute() s=Start")
            val products =
                productGateway.findAllBySpecifications(
                    specification = specification,
                )

            if (products.isEmpty())
                {
                    throw DomainException("product.list.empty")
                }

            val result =
                strategyContext.execute(
                    type = type,
                    rule = rule,
                    input = products,
                )

            logger.info("c=ComparisonProductsUseCase m=execute() s=Done")
            return result
        } catch (exception: NoSuchElementException) {
            logger.error(
                "c=ComparisonProductUseCase m=compare s=Error - Attempted to access first/last on an empty sorted list. This should have been caught by the initial empty check. message=${exception.message}",
            )
            throw GenericException(message = "generic.error")
        } catch (exception: DomainException) {
            logger.error("c=ComparisonProductUseCase m=compare s=Error - DomainException - message=${exception.message}")
            throw DomainException(message = exception.message)
        } catch (exception: GenericException) {
            logger.error("c=ComparisonProductUseCase m=compare s=Error - GenericException - message=${exception.message}")
            throw GenericException(message = exception.message)
        } catch (exception: IllegalArgumentInternalException) {
            logger.error("c=ComparisonProductUseCase m=compare s=Error - IllegalArgumentInternalException - message=${exception.message}")
            throw IllegalArgumentInternalException(message = exception.message)
        } catch (exception: Exception) {
            logger.error("c=ComparisonProductUseCase m=compare s=Error - Exception - message=${exception.message}")
            throw GenericException(message = "generic.error")
        }
    }
}
