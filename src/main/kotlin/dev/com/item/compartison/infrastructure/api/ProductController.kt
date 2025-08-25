package dev.com.item.compartison.infrastructure.api

import dev.com.item.compartison.application.usecase.FindAllProductPageableUseCase
import dev.com.item.compartison.application.usecase.FindProductByIdentifierUseCase
import dev.com.item.compartison.domain.enums.SortDirectionEnum
import dev.com.item.compartison.domain.utils.PageInfoGenericUtils
import dev.com.item.compartison.domain.utils.PaginationUtils
import dev.com.item.compartison.infrastructure.api.mapper.toResponseDTO
import dev.com.item.compartison.infrastructure.api.models.response.DefaultResponseDTO
import dev.com.item.compartison.infrastructure.api.models.response.ProductResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/products")
class ProductController(
    private val findProductByIdentifierUseCase: FindProductByIdentifierUseCase,
    private val findAllProductPageableUseCase: FindAllProductPageableUseCase
) {

    @GetMapping("/find-all")
    @ResponseStatus(HttpStatus.OK)
    fun findAllProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "1") size: Int,
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

        val response = products.content.map { it.toResponseDTO() }

        return DefaultResponseDTO.success(
            data = PageInfoGenericUtils(
                content = response,
                number =  products.number,
                size =  products.size,
                totalElements =  products.totalElements,
                totalPages =  products.totalPages,
            )
        )
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun findByIdentifier(
        @PathVariable productId: Long
    ): DefaultResponseDTO<ProductResponseDTO> {
        val product = findProductByIdentifierUseCase.execute(
            identifier = productId
        )

        return DefaultResponseDTO.success(
            data = product.toResponseDTO()
        )
    }
}