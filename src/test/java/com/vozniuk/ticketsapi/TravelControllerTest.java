package com.vozniuk.ticketsapi;

import com.vozniuk.ticketsapi.controllers.TravelController;
import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.entity.Ticket;
import com.vozniuk.ticketsapi.data.entity.TravelRequest;
import com.vozniuk.ticketsapi.data.service.impl.TicketServiceImpl;
import com.vozniuk.ticketsapi.data.service.impl.TravelRequestServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


class TravelControllerTest {


    private static TravelRequest request;

    @BeforeAll
    private static void initTravelRequestAndTicket() {
        Ticket ticket = new Ticket();
        ticket.setDepartureTime(Timestamp.valueOf(LocalDateTime.now()));
        ticket.setRouteNumber(1);
        ticket.setId(1L);

        request = new TravelRequest();
        request.setStatus(RequestStatus.PROCESSING);
        request.setTicket(ticket);
        request.setId(1L);
    }

    @Test
    void testGetById() {
        TravelRequestServiceImpl travelRequestService = mock(TravelRequestServiceImpl.class);
        TicketServiceImpl ticketService = mock(TicketServiceImpl.class);
        when(travelRequestService.getTravelRequestById(anyLong())).thenReturn(request);

        TravelController controller = new TravelController(travelRequestService, ticketService);
        ResponseEntity<Map<String, RequestStatus>> expected = ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("status", RequestStatus.PROCESSING));

        assertEquals(expected, controller.getTravelRequestStatus(1));
        verify(travelRequestService, times(1)).getTravelRequestById(1);
    }

    @Test
    void testNotExistingIdResponse() {
        TravelRequestServiceImpl travelRequestService = mock(TravelRequestServiceImpl.class);
        TicketServiceImpl ticketService = mock(TicketServiceImpl.class);
        when(travelRequestService.getTravelRequestById(anyLong())).thenThrow(EntityNotFoundException.class);

        TravelController travelController = new TravelController(travelRequestService, ticketService);
        ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        assertEquals(expected, travelController.getTravelRequestStatus(666));
    }

    @Test
    void testCreateNewRequestStatus_201() {
        TicketServiceImpl ticketService = mock(TicketServiceImpl.class);
        TravelRequestServiceImpl travelRequestService = mock(TravelRequestServiceImpl.class);

        TravelController controller = new TravelController(travelRequestService, ticketService);
        Object response = controller.createNewTravelRequest("2020-08-29 20:00:00", 100);
        HttpStatus actualStatusCode = ((ResponseEntity) response).getStatusCode();
        assertEquals(HttpStatus.CREATED, actualStatusCode);
    }

    @Test
    void testControllerStatusOnInvalidParams_400() {
        TicketServiceImpl ticketService = mock(TicketServiceImpl.class);
        TravelRequestServiceImpl travelRequestService = mock(TravelRequestServiceImpl.class);
        doThrow(IllegalArgumentException.class).when(ticketService).saveTicket(any());

        TravelController controller = new TravelController(travelRequestService, ticketService);

        Object badDateResponse = controller.createNewTravelRequest("any string", 20);

        verify(ticketService, times(1)).saveTicket(any());
        HttpStatus actualStatusCode = ((ResponseEntity) badDateResponse).getStatusCode();
        assertEquals(HttpStatus.BAD_REQUEST, actualStatusCode);
    }




}
