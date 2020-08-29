package com.vozniuk.ticketsapi.data.dao;

import com.vozniuk.ticketsapi.data.entity.Ticket;

import java.util.List;

public interface TicketDao {
    void saveNewTicket(Ticket ticket);
    Ticket getTicketById(long id);
    List<Ticket> getAllTickets();

}
