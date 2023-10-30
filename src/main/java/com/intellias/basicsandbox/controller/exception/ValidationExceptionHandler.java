package com.intellias.basicsandbox.controller.exception;

import com.intellias.basicsandbox.controller.dto.ErrorDTO;
import org.jboss.logging.Logger;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger log = Logger.getLogger(ValidationExceptionHandler.class);

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        log.debugf("Handled argument validation exception. [%s]", exception.getMessage());

        List<String> fieldErrors = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        String langHeader = headers.getHeaderString(HttpHeaders.ACCEPT_LANGUAGE);
        ErrorDTO errorDTO = processFieldErrors(fieldErrors, langHeader);

        return Response.status(BAD_REQUEST).entity(errorDTO).build();
    }

    private ErrorDTO processFieldErrors(List<String> fieldErrors, String language) {
        StringBuilder validationErrorMessageBuilder = new StringBuilder();
        Locale locale = extractLocaleFromHeader(language);
        ResourceBundle localizedResource = ResourceBundle.getBundle("resourcebundle.messages", locale);
        for (String fieldError: fieldErrors) {
            if (fieldError != null) {
                validationErrorMessageBuilder.append("[")
                        .append(fieldError)
                        .append("] ")
                        .append(applyCustomTranslation(localizedResource, fieldError))
                        .append(". ");
            }
        }
        String validationMessage = validationErrorMessageBuilder.toString();
        log.infof("Argument validation response [{}]", validationMessage);
        return new ErrorDTO(BAD_REQUEST, validationMessage);
    }

    private String applyCustomTranslation(ResourceBundle localizedResource, String message) {
        if (message != null && message.startsWith("{") && message.endsWith("}") ) {
            String translationKey = message.substring(1, message.length() - 1);
            if (localizedResource.containsKey(translationKey)){
                return localizedResource.getString(translationKey);
            }
        }
        return message;
    }

    private Locale extractLocaleFromHeader(String acceptLanguageHeader) {
        if (acceptLanguageHeader != null && !acceptLanguageHeader.isEmpty()) {
            String[] languages = acceptLanguageHeader.split(",");
            for (String language : languages) {
                String[] parts = language.trim().split(";q=");
                if (parts.length > 0) {
                    Locale locale = Locale.forLanguageTag(parts[0]);
                    if (locale != null) {
                        return locale;
                    }
                }
            }
        }
        // Return the default locale if no valid language is found in the header
        return Locale.getDefault();
    }
}
