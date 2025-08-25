package dev.com.item.compartison.infrastructure.config

import dev.com.item.compartison.infrastructure.utils.PRODUCT_TAG
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Meli - Item Comparison",
        version = "1.0",
        description = "Documentação dos endpoints da API."
    ),
    tags = [
        Tag(name = PRODUCT_TAG, description = "Endpoints relacionados a produtos.")
    ]
)
class OpenAPI {
}