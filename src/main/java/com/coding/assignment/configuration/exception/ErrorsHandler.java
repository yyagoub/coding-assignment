package com.coding.assignment.configuration.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ErrorsHandler {

    @Autowired
    private MessageSource messageSource;

    public List<ValidationError> publish(String object, String localeCode, List<ValidationError> errors) {
        object = getObject(object);
        errors = getErrors(errors);
        String message = getMessage(localeCode);
        errors.add(new ValidationError(object,message));
        return errors;
    }

    public List<ValidationError> publish(String object, String localeCode) {
        List<ValidationError> errors = new ArrayList<>();
        object = getObject(object);
        String message = getMessage(localeCode);
        errors.add(new ValidationError(object,message));
        return errors;
    }

    private String getObject(String object) {
        if (StringUtils.isEmpty(object)) object = "object" ;
        return object;
    }

    private String getMessage(String localeCode) {
        if (StringUtils.isEmpty(localeCode))
            return "error";
        String message = messageSource.getMessage(localeCode, null, LocaleContextHolder.getLocale());
        if (StringUtils.isEmpty(message))
            return "error";
        else
            return message;
    }

    private List<ValidationError> getErrors(List<ValidationError> errors){
        if (errors == null) errors = new ArrayList<>();
        return errors;
    }
}