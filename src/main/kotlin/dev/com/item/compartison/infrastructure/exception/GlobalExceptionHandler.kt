package dev.com.item.compartison.infrastructure.exception

import dev.com.item.compartison.domain.enums.ErrorTypeEnum
import dev.com.item.compartison.domain.exception.template.DomainException
import dev.com.item.compartison.domain.exception.template.FileParseException
import dev.com.item.compartison.domain.exception.template.GatewayException
import dev.com.item.compartison.domain.exception.template.GenericException
import dev.com.item.compartison.domain.exception.template.NotFoundException
import dev.com.item.compartison.infrastructure.api.models.response.DefaultResponseDTO
import dev.com.item.compartison.infrastructure.api.models.response.ExceptionDataDTO
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    private val messageSource: MessageSource
) {

    @ExceptionHandler(GenericException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericException(
        exception: GenericException,
        request: HttpServletRequest
    ): DefaultResponseDTO<ExceptionDataDTO>{
        val message = exception.message?.let {
            messageSource.getMessage(
                it,
                exception.args,
                LocaleContextHolder.getLocale()
            )
        }

        val data = ExceptionDataDTO(
            error = ErrorTypeEnum.GENERIC_ERROR.name,
            message = message,
            path = request.requestURI
        )

        return DefaultResponseDTO(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            data = data
        )
    }

    @ExceptionHandler(DomainException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleDomainException(
        exception: DomainException,
        request: HttpServletRequest
    ): DefaultResponseDTO<ExceptionDataDTO>{
        val message = exception.message?.let {
            messageSource.getMessage(
                it,
                exception.args,
                LocaleContextHolder.getLocale()
            )
        }

        val data = ExceptionDataDTO(
            error = ErrorTypeEnum.DOMAIN_ERROR.name,
            message = message,
            path = request.requestURI
        )

        return DefaultResponseDTO(
            status = HttpStatus.CONFLICT.value(),
            data = data
        )
    }

    @ExceptionHandler(FileParseException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleFileParseException(
        exception: FileParseException,
        request: HttpServletRequest
    ): DefaultResponseDTO<ExceptionDataDTO>{
        val message = exception.message?.let {
            messageSource.getMessage(
                it,
                exception.args,
                LocaleContextHolder.getLocale()
            )
        }

        val data = ExceptionDataDTO(
            error = ErrorTypeEnum.FILE_PARSE_ERROR.name,
            message = message,
            path = request.requestURI
        )

        return DefaultResponseDTO(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            data = data
        )
    }

    @ExceptionHandler(GatewayException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGatewayException(
        exception: GatewayException,
        request: HttpServletRequest
    ): DefaultResponseDTO<ExceptionDataDTO>{
        val message = exception.message?.let {
            messageSource.getMessage(
                it,
                exception.args,
                LocaleContextHolder.getLocale()
            )
        }

        val data = ExceptionDataDTO(
            error = ErrorTypeEnum.GATEWAY_ERROR.name,
            message = message,
            path = request.requestURI
        )

        return DefaultResponseDTO(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            data = data
        )
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(
        exception: NotFoundException,
        request: HttpServletRequest
    ): DefaultResponseDTO<ExceptionDataDTO>{
        val message = exception.message?.let {
            messageSource.getMessage(
                it,
                exception.args,
                LocaleContextHolder.getLocale()
            )
        }

        val data = ExceptionDataDTO(
            error = ErrorTypeEnum.NOT_FOUND_ERROR.name,
            message = message,
            path = request.requestURI
        )

        return DefaultResponseDTO(
            status = HttpStatus.NOT_FOUND.value(),
            data = data
        )
    }
}
