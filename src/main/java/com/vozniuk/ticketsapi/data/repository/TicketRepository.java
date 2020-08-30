package com.vozniuk.ticketsapi.data.repository;

import com.vozniuk.ticketsapi.data.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
