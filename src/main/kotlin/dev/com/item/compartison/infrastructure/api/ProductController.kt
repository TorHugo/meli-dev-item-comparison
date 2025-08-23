package dev.com.item.compartison.infrastructure.api

import dev.com.item.compartison.application.usecase.FindAllProductPageableUseCase
import dev.com.item.compartison.application.usecase.FindProductByIdentifierUseCase
import dev.com.item.compartison.domain.enums.SortDirectionEnum
import dev.com.item.compartison.domain.gateway.ProductGateway
import dev.com.item.compartison.domain.utils.PageInfoGenericUtils
import dev.com.item.compartison.domain.utils.PaginationUtils
import dev.com.item.compartison.infrastructure.api.models.response.DefaultResponseDTO
import dev.com.item.compartison.infrastructure.api.models.response.ProductResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
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
    private val productGateway: ProductGateway
) {

    @GetMapping("/find-all")
    @ResponseStatus(HttpStatus.OK)
    fun findAllProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "0") size: Int,
        @RequestParam(defaultValue = "identifier") sortBy: String,
        @RequestParam(defaultValue = "ASC") direction: SortDirectionEnum,
    ): DefaultResponseDTO<PageInfoGenericUtils<ProductResponseDTO>> {
        val pageable = PaginationUtils(
            number = page,
            size = size,
            sortBy = sortBy,
            direction = direction
        )
        val products = findAllProductPageableUseCase.execute(pageable = pageable)
        return DefaultResponseDTO.success(
            data = products
        )
    }
}