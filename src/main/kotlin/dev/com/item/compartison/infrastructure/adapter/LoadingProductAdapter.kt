package dev.com.item.compartison.infrastructure.adapter

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.com.item.compartison.domain.entity.ProductDomain
import dev.com.item.compartison.domain.exception.template.FileParseException
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class LoadingProductAdapter {
    private val logger = LoggerFactory.getLogger(LoadingProductAdapter::class.java)
    private val objectMapper = ObjectMapper()

    fun load(): List<ProductDomain> {
        try {
            logger.info("c=LoadingProductAdapter m=load() s=start")
            val resource = ClassPathResource("data/products.json")
            val products: List<ProductDomain> = objectMapper.readValue(resource.inputStream)
            logger.info("c=LoadingProductAdapter m=load() s=done")
            return products
        } catch (e: IOException) {
            // It's better to catch specific exceptions than a generic 'Exception'.
            logger.error("c=LoadingProductAdapter m=load() s=error message='Error reading file'", e)
            throw FileParseException("file.parse.error")
        } catch (e: JsonProcessingException) {
            logger.error("c=LoadingProductAdapter m=load() s=error message='Error parsing JSON'", e)
            throw FileParseException("file.parse.error")
        }
    }
}