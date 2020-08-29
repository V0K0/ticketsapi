package com.vozniuk.ticketsapi.controllers;

import com.vozniuk.ticketsapi.data.entity.Ticket;
import com.vozniuk.ticketsapi.data.entity.TravelRequest;
import com.vozniuk.ticketsapi.data.service.TicketService;
import com.vozniuk.ticketsapi.data.service.TravelRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Collections;


@RestController
@RequestMapping("/travels")
public class TravelController {

    private final TravelRequestService travelRequestService;
    private final TicketService ticketService;

    private final Logger logger = LoggerFactory.getLogger(TravelController.class);

    @Autowired
    public TravelController(TravelRequestService travelRequestService, TicketService ticketService) {
        this.travelRequestService = travelRequestService;
        this.ticketService = ticketService;
    }

    @GetMapping(value = "/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getTravelRequestStatus(@PathVariable long id) {
        try {
            TravelRequest request = travelRequestService.getTravelRequestById(id);
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("status", request.getStatus()));
        } catch (EntityNotFoundException exception) {
            logger.error("Entity with id: {} not found!", id);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    public Object createNewTravelRequest(@RequestParam(name = "time") String time, @RequestParam(name = "route") int routeNumber) {
        if (time != null && routeNumber > 0) {
            Timestamp departure = convertToTimestamp(time);

            Ticket ticket = new Ticket();
            TravelRequest request = new TravelRequest();
            ticket.setDepartureTime(departure);
            ticket.setRouteNumber(routeNumber);

            try {
                ticketService.saveNewTicket(ticket);
                request.setTicket(ticket);
                travelRequestService.saveNewRequest(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("request_id", request.getTravelRequestId()));
            } catch (IllegalArgumentException exception) {
                logger.error("Could not execute saving to database cause of: {}", exception.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    private Timestamp convertToTimestamp(String time) {
        try {
            return Timestamp.valueOf(time);
        } catch (IllegalArgumentException e) {
            logger.error("Failed attempt to parse date");
        }
        return null;
    }

}
