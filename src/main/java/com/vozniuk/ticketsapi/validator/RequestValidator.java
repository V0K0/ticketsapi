package com.vozniuk.ticketsapi.validator;

import com.vozniuk.ticketsapi.data.entity.TravelRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return TravelRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "ticket", "ticket.empty");
    }
}
