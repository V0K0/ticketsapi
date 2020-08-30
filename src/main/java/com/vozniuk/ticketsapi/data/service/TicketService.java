package com.vozniuk.ticketsapi.data.service;

import com.vozniuk.ticketsapi.data.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    void saveTicket(Ticket ticket);

    Ticket getTicketById(long id);

    List<Ticket> getAllTickets();
}
