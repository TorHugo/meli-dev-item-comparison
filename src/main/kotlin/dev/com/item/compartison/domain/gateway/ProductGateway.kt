package dev.com.item.compartison.domain.gateway

import dev.com.item.compartison.domain.entity.ProductDomain
import dev.com.item.compartison.domain.utils.PageInfoGenericUtils
import dev.com.item.compartison.domain.utils.PaginationUtils

interface ProductGateway {
    fun findAllByPage(pageable: PaginationUtils): PageInfoGenericUtils<ProductDomain>

    fun findByProductId(productId: Long): ProductDomain?
}
