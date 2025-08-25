package dev.com.item.compartison.application.usecase

import dev.com.item.compartison.domain.entity.ProductDomain
import dev.com.item.compartison.domain.exception.template.GatewayException
import dev.com.item.compartison.domain.exception.template.GenericException
import dev.com.item.compartison.domain.exception.template.NotFoundException
import dev.com.item.compartison.domain.gateway.ProductGateway
import dev.com.item.compartison.infrastructure.adapter.LoadingProductAdapter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FindProductByIdentifierUseCase(
    private val productGateway: ProductGateway,
) {
    private val logger = LoggerFactory.getLogger(LoadingProductAdapter::class.java)

    fun execute(identifier: Long): ProductDomain {
        try {
            logger.info("c=FindProductByIdentifierUseCase m=execute() s=Start identifier=$identifier")
            val product = productGateway.findByProductId(productId = identifier)

            if (product == null) {
                throw NotFoundException("product.not.found")
            }

            logger.info("c=FindProductByIdentifierUseCase m=execute() s=Done identifier=$identifier")
            return product
        } catch (exception: NotFoundException) {
            logger.error(
                "c=FindProductByIdentifierUseCase m=execute() s=Error - NotFoundException - identifier=$identifier message=${exception.message}",
            )
            throw NotFoundException(message = "domain.error")
        } catch (exception: GatewayException) {
            logger.error(
                "c=FindProductByIdentifierUseCase m=execute() s=Error - GatewayException - identifier=$identifier message=${exception.message}",
            )
            throw GatewayException(message = exception.message)
        } catch (exception: Exception) {
            logger.error(
                "c=FindProductByIdentifierUseCase m=execute() s=Error - Exception - identifier=$identifier message=${exception.message}",
            )
            throw GenericException(message = "generic.error")
        }
    }
}
