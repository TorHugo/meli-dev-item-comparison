package dev.com.item.compartison.infrastructure.gateway

import dev.com.item.compartison.domain.entity.ProductDomain
import dev.com.item.compartison.domain.enums.SortDirectionEnum
import dev.com.item.compartison.domain.exception.template.GatewayException
import dev.com.item.compartison.domain.gateway.ProductGateway
import dev.com.item.compartison.domain.helper.SpecificationParamHelper
import dev.com.item.compartison.domain.utils.PageInfoGenericUtils
import dev.com.item.compartison.domain.utils.PaginationUtils
import dev.com.item.compartison.infrastructure.adapter.LoadingProductAdapter
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ProductGatewayImpl(
    private val loadingProductAdapter: LoadingProductAdapter,
) : ProductGateway {
    companion object {
        private var products: List<ProductDomain> = emptyList()
        private const val GATEWAY_ERROR: String = "gateway.error"
    }

    @PostConstruct
    fun load() {
        products = loadingProductAdapter.load()
    }

    private val logger = LoggerFactory.getLogger(ProductGatewayImpl::class.java)

    override fun findAllByPage(pageable: PaginationUtils): PageInfoGenericUtils<ProductDomain> {
        try {
            return paginateAndSortList(products, pageable)
        } catch (exception: Exception) {
            logger.error("c=ProductGatewayImpl m=findAllByPage() s=Exception message=${exception.message}", exception)
            throw GatewayException(GATEWAY_ERROR)
        }
    }

    /**
     * Obs.: Essa responsabilidade ficaria exclusivamente no database.
     * No caso, idealmente não seria necessário "reconstruir" a lista de `baseFilteredProducts` várias vezes.
     *
     * **/
    override fun findAllBySpecifications(specification: SpecificationParamHelper): List<ProductDomain> {
        try {
            var baseFilteredProducts = products.filter {
                it.category.contains(specification.category, ignoreCase = true)
            }

            specification.brand?.let { brand ->
                baseFilteredProducts = baseFilteredProducts.filter { it.brand.equals(brand, ignoreCase = true) }
            }

            specification.name?.let { name ->
                baseFilteredProducts = baseFilteredProducts.filter { it.name.equals(name, ignoreCase = true) }
            }

            specification.discount?.let { discount ->
                baseFilteredProducts = baseFilteredProducts.filter { it.discount!! >= discount }
            }

            specification.minPrice?.let { minPrice ->
                baseFilteredProducts = baseFilteredProducts.filter { it.price >= BigDecimal.valueOf(minPrice) }
            }

            specification.maxPrice?.let { maxPrice ->
                baseFilteredProducts = baseFilteredProducts.filter { it.price <= BigDecimal.valueOf(maxPrice) }
            }

            return baseFilteredProducts
        } catch (exception: Exception) {
            logger.error("c=ProductGatewayImpl m=findAllByPage() s=Exception message=${exception.message}", exception)
            throw GatewayException(GATEWAY_ERROR)
        }
    }

    override fun findByProductId(productId: Long): ProductDomain? {
        try {
            return products.find { it.identifier == productId }
        } catch (exception: Exception) {
            // Adicionar a exceção ao log ajuda na depuração.
            logger.error("c=ProductGatewayImpl m=findByProductId() s=Exception message=${exception.message}", exception)
            throw GatewayException(GATEWAY_ERROR)
        }
    }

    /**
     * Simulates JPA pagination and sorting on an in-memory list.
     * This method first sorts the list based on the provided criteria and then extracts
     * the requested page.
     *
     * @param sourceList The complete list of items to be paginated.
     * @param pageable The pagination and sorting parameters.
     * @return A PageInfoGenericUtils object containing the paginated content and metadata.
     */
    private fun paginateAndSortList(
        sourceList: List<ProductDomain>,
        pageable: PaginationUtils,
    ): PageInfoGenericUtils<ProductDomain> {
        val sortedList = sortProducts(sourceList, pageable.sortBy, pageable.direction)
        val fromIndex = pageable.number * pageable.size

        // If the start index is beyond the list size, return an empty page.
        if (fromIndex >= sortedList.size) {
            return PageInfoGenericUtils(
                content = emptyList(),
                number = pageable.number,
                size = pageable.size,
                totalElements = sortedList.size.toLong(),
                totalPages = calculateTotalPages(sortedList.size, pageable.size),
            )
        }

        // Calculate the end index, ensuring it doesn't exceed the list size.
        val toIndex = (fromIndex + pageable.size).coerceAtMost(sortedList.size)

        val pageContent = sortedList.subList(fromIndex, toIndex)
        return PageInfoGenericUtils(
            content = pageContent,
            number = pageable.number,
            size = pageable.size,
            totalElements = sortedList.size.toLong(),
            totalPages = calculateTotalPages(sortedList.size, pageable.size),
        )
    }

    /**
     * Sorts a list of products based on a property name and direction.
     */
    private fun sortProducts(
        productsToSort: List<ProductDomain>,
        sortBy: String,
        direction: SortDirectionEnum,
    ): List<ProductDomain> {
        val comparator =
            when (sortBy.lowercase()) {
                "name" -> compareBy(ProductDomain::name)
                "price" -> compareBy(ProductDomain::price)
                "rating" -> compareBy(ProductDomain::rating)
                "brand" -> compareBy(ProductDomain::brand)
                else -> return productsToSort
            }

        return if (direction == SortDirectionEnum.DESC) {
            productsToSort.sortedWith(comparator.reversed())
        } else {
            productsToSort.sortedWith(comparator)
        }
    }

    /**
     * Calculates the total number of pages required to display all elements.
     * This is an efficient way to calculate the ceiling of an integer division.
     */
    private fun calculateTotalPages(
        totalElements: Int,
        pageSize: Int,
    ): Int {
        if (pageSize == 0) return 0
        return (totalElements + pageSize - 1) / pageSize
    }
}
