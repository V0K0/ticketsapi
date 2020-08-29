package com.vozniuk.ticketsapi.data.service;

import com.vozniuk.ticketsapi.data.dao.TicketDao;
import com.vozniuk.ticketsapi.data.entity.Ticket;
import com.vozniuk.ticketsapi.data.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class TicketService implements TicketDao {

    private TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void saveNewTicket(Ticket ticket) {
        if (isTickedValid(ticket)) {
            ticketRepository.save(ticket);
        } else {
            throw new IllegalArgumentException("Entity Ticket is not valid");
        }
    }

    private boolean isTickedValid(Ticket ticket) {
        return ticket != null && ticket.getDepartureTime() != null && ticket.getRouteNumber() > 0;
    }

    @Override
    public Ticket getTicketById(long id) throws EntityNotFoundException{
        return ticketRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
