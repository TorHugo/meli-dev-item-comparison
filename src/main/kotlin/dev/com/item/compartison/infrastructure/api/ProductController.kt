package dev.com.item.compartison.infrastructure.api

import dev.com.item.compartison.application.usecase.ComparisonProductUseCase
import dev.com.item.compartison.application.usecase.FindAllProductPageableUseCase
import dev.com.item.compartison.application.usecase.FindProductByIdentifierUseCase
import dev.com.item.compartison.domain.entity.ComparisonAggregateRoot
import dev.com.item.compartison.domain.entity.ComparisonRuleDomain
import dev.com.item.compartison.domain.enums.ComparisonTypeEnum
import dev.com.item.compartison.domain.enums.SortDirectionEnum
import dev.com.item.compartison.domain.helper.SpecificationParamHelper
import dev.com.item.compartison.domain.utils.PageInfoGenericUtils
import dev.com.item.compartison.domain.utils.PaginationUtils
import dev.com.item.compartison.infrastructure.api.mapper.toResponseDTO
import dev.com.item.compartison.infrastructure.api.models.response.DefaultResponseDTO
import dev.com.item.compartison.infrastructure.api.models.response.ProductResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/products")
class ProductController(
    private val findProductByIdentifierUseCase: FindProductByIdentifierUseCase,
    private val findAllProductPageableUseCase: FindAllProductPageableUseCase,
    private val comparisonProductUseCase: ComparisonProductUseCase,
) {
    @GetMapping("/find-all")
    @ResponseStatus(HttpStatus.OK)
    fun findAllProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "1") size: Int,
        @RequestParam(defaultValue = "identifier") sortBy: String,
        @RequestParam(defaultValue = "ASC") direction: SortDirectionEnum,
    ): DefaultResponseDTO<PageInfoGenericUtils<ProductResponseDTO>> {
        val pageable =
            PaginationUtils(
                number = page,
                size = size,
                sortBy = sortBy,
                direction = direction,
            )
        val products = findAllProductPageableUseCase.execute(pageable = pageable)

        val response = products.content.map { it.toResponseDTO() }

        return DefaultResponseDTO.success(
            data =
                PageInfoGenericUtils(
                    content = response,
                    number = products.number,
                    size = products.size,
                    totalElements = products.totalElements,
                    totalPages = products.totalPages,
                ),
        )
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun findByIdentifier(
        @PathVariable productId: Long,
    ): DefaultResponseDTO<ProductResponseDTO> {
        val product =
            findProductByIdentifierUseCase.execute(
                identifier = productId,
            )

        return DefaultResponseDTO.success(
            data = product.toResponseDTO(),
        )
    }

    @GetMapping("/comparison")
    @ResponseStatus(HttpStatus.OK)
    fun comparisonProducts(
        @RequestHeader(name = "type") type: ComparisonTypeEnum,
        @RequestHeader(name = "specificationKey", required = false) specificationKey: String? = null,
        @RequestHeader(name = "higherIsBetter", required = false) higherIsBetter: Boolean? = null,
        @RequestParam(name = "category") category: String,
        @RequestParam(name = "brand") brand: String? = null,
        @RequestParam(name = "name") name: String? = null,
        @RequestParam(name = "discount") discount: Double? = null,
        @RequestParam(name = "minPrice") minPrice: Double? = null,
        @RequestParam(name = "maxPrice") maxPrice: Double? = null,
    ): DefaultResponseDTO<ComparisonAggregateRoot> {
        val rule =
            ComparisonRuleDomain(
                specificationKey = specificationKey,
                higherIsBetter = higherIsBetter,
            )

        val specification =
            SpecificationParamHelper(
                category = category,
                name = name,
                brand = brand,
                discount = discount,
                minPrice = minPrice,
                maxPrice = maxPrice,
            )

        val result =
            comparisonProductUseCase.execute(
                type = type,
                rule = rule,
                specification = specification,
            )

        return DefaultResponseDTO.success(
            data = result,
        )
    }
}
