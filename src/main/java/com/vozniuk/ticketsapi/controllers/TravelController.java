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
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api")
public class TravelController {

    private final TravelRequestService travelRequestService;
    private final TicketService ticketService;

    private final Logger logger = LoggerFactory.getLogger(TravelController.class);

    @Autowired
    public TravelController(TravelRequestService travelRequestService, TicketService ticketService) {
        this.travelRequestService = travelRequestService;
        this.ticketService = ticketService;
    }

    @GetMapping(value = "/travels/{id}")
    public ResponseEntity<?> getTravelRequestStatusById(@PathVariable long id) {
        try {
            TravelRequest request = travelRequestService.getTravelRequestById(id);
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("status", request.getStatus()));
        } catch (EntityNotFoundException exception) {
            logger.error("Entity with id: {} not found!", id);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<?> getAllUserRequestsById(@PathVariable long id) {
        try {
            List<TravelRequest> allRequestsByUserId = travelRequestService.getAllRequestsByUserId(id);
            return ResponseEntity.status(HttpStatus.OK).body(allRequestsByUserId);
        } catch (EntityNotFoundException exception) {
            logger.error("Failed attempt to find requests. Cause: {}", exception.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping(value = "/travels", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTravelRequest(@RequestBody Ticket ticket) {
        try {
            TravelRequest travelRequest = new TravelRequest();
            ticketService.saveTicket(ticket);
            travelRequest.setTicket(ticket);
            travelRequestService.saveRequest(travelRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("request_id", travelRequest.getId()));
        } catch (Exception e) {
            logger.error("Could not execute saving to database cause of: {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

}
