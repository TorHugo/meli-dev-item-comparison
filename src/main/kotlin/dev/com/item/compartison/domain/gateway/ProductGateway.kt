package dev.com.item.compartison.domain.gateway

import dev.com.item.compartison.domain.entity.ProductDomain
import dev.com.item.compartison.domain.helper.SpecificationParamHelper
import dev.com.item.compartison.domain.utils.PageInfoGenericUtils
import dev.com.item.compartison.domain.utils.PaginationUtils

interface ProductGateway {
    fun findAllByPage(pageable: PaginationUtils): PageInfoGenericUtils<ProductDomain>

    fun findAllBySpecifications(specification: SpecificationParamHelper): List<ProductDomain>

    fun findByProductId(productId: Long): ProductDomain?
}
