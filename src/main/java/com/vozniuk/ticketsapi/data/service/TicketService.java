package com.vozniuk.ticketsapi.data.service;

import com.vozniuk.ticketsapi.data.entity.Ticket;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface TicketService {

    void saveTicket(Ticket ticket) throws IllegalArgumentException;

    Ticket getTicketById(long id) throws EntityNotFoundException;

    List<Ticket> getAllTickets();
}
