package com.vozniuk.ticketsapi.validator;

import com.vozniuk.ticketsapi.data.entity.Ticket;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TicketValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Ticket.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Ticket ticket = (Ticket) o;
        ValidationUtils.rejectIfEmpty(errors, "departureTime", "time.empty");
        if (ticket.getUserId() < 1) {
            errors.rejectValue("userId", "less than 1");
        }
        if (ticket.getRouteNumber() < 1) {
            errors.rejectValue("routeNumber", "less than 1");
        }
    }
}
