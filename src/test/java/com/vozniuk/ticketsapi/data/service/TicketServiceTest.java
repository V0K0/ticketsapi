package com.vozniuk.ticketsapi.data.service;

import com.vozniuk.ticketsapi.data.entity.Ticket;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    private static List<Ticket> tickets;

    private static long userId = 1L;

    @BeforeAll
    private static void testSetUpTicketsCollection() {
        tickets = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tickets.add(getTicket());
        }
    }

    @Test
    @Order(1)
    void testCorrectNumberOfTickets(){
        tickets.forEach(ticket -> ticketService.saveTicket(ticket));
        int countOfSavedTickets = ticketService.getAllTickets().size();
        assertEquals(tickets.size(), countOfSavedTickets);
    }

    @Test
    void testServicePersistsTicket() {
        Ticket ticket = getTicket();
        ticketService.saveTicket(ticket);
        assertNotEquals(0, ticket.getId());
    }

    @Test
    void testAddTicketWithoutTime(){
        Ticket timelessTicket = new Ticket();
        timelessTicket.setRouteNumber(1);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> ticketService.saveTicket(timelessTicket));
        assertEquals("Entity Ticket is not valid", ex.getMessage());
    }

    private static Ticket getTicket() {
        Ticket ticket = new Ticket();
        ticket.setRouteNumber(1);
        ticket.setDepartureTime(Timestamp.valueOf(LocalDateTime.now()));
        ticket.setUserId(userId++);
        return ticket;
    }

    @Test
    void testThrowExceptionWhenTicketNotFound() {
        assertThrows(EntityNotFoundException.class, () -> ticketService.getTicketById(999));
    }

    @Test
    void testAddTicketWithoutRoute() {
        Ticket routeLessTicket = new Ticket();
        routeLessTicket.setDepartureTime(Timestamp.valueOf(LocalDateTime.now()));
        Exception ex = assertThrows(IllegalArgumentException.class, () -> ticketService.saveTicket(routeLessTicket));
        assertEquals("Entity Ticket is not valid", ex.getMessage());
    }

}