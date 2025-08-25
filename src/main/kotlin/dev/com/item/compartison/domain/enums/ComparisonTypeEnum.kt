package dev.com.item.compartison.domain.enums

enum class ComparisonTypeEnum(val description: String) {
    PRICE(description = "Estratégia responsável por realizar a comparação dos produtos por preço."),
    RATING(description = "Estratégia responsável por realizar a comparação dos produtos por avaliação."),
    SPECIFICATION(description = "Estratégia responsável por realizar a comparação dos produtos por alguma especificação técnica."),
}