package com.vozniuk.ticketsapi.data.service.impl;

import com.vozniuk.ticketsapi.data.entity.Ticket;
import com.vozniuk.ticketsapi.data.repository.TicketRepository;
import com.vozniuk.ticketsapi.data.service.TicketService;
import com.vozniuk.ticketsapi.validator.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private TicketValidator ticketValidator;

    @Autowired
    public void setTicketValidator(TicketValidator ticketValidator) {
        this.ticketValidator = ticketValidator;
    }

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void saveTicket(Ticket ticket) {
        if (!validateTicket(ticket).hasErrors()) {
            ticketRepository.save(ticket);
        } else {
            throw new IllegalArgumentException("Entity Ticket is not valid");
        }
    }

    private BindingResult validateTicket(Ticket ticket) {
        DataBinder dataBinder = new DataBinder(ticket);
        dataBinder.addValidators(ticketValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket getTicketById(long id) {
        return ticketRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
