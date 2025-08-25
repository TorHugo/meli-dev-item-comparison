package dev.com.item.compartison.application.usecase

import dev.com.item.compartison.domain.entity.ProductDomain
import dev.com.item.compartison.domain.exception.template.GatewayException
import dev.com.item.compartison.domain.exception.template.GenericException
import dev.com.item.compartison.domain.gateway.ProductGateway
import dev.com.item.compartison.domain.utils.PageInfoGenericUtils
import dev.com.item.compartison.domain.utils.PaginationUtils
import dev.com.item.compartison.infrastructure.adapter.LoadingProductAdapter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FindAllProductPageableUseCase(
    private val productGateway: ProductGateway,
) {
    private val logger = LoggerFactory.getLogger(LoadingProductAdapter::class.java)

    fun execute(pageable: PaginationUtils): PageInfoGenericUtils<ProductDomain> {
        try {
            logger.info("c=FindAllProductPageableUseCase m=execute() s=Start")
            val products = productGateway.findAllByPage(pageable = pageable)

            if (products.content.isEmpty()) {
                return PageInfoGenericUtils(
                    content = listOf(),
                    number = pageable.number,
                    size = pageable.size,
                    totalElements = 0,
                    totalPages = 0,
                )
            }

            logger.info("c=FindAllProductPageableUseCase m=execute() s=Done")
            return products
        } catch (exception: GatewayException) {
            logger.error("c=FindAllProductPageableUseCase m=execute() s=Error - GatewayException - message=${exception.message}")
            throw GatewayException(exception.message)
        } catch (exception: Exception) {
            logger.error("c=FindAllProductPageableUseCase m=execute() s=Error - Exception - message=${exception.message}")
            throw GenericException("generic.error")
        }
    }
}
