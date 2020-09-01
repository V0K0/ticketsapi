package com.vozniuk.ticketsapi.controllers;

import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.service.RequestStatusService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestStatusControllerTest {

    @Test
    void getRandomRequestStatus() {
        RequestStatusService requestStatusService = mock(RequestStatusService.class);
        RequestStatusController requestStatusController = new RequestStatusController(requestStatusService);

        when(requestStatusService.getRandomRequestStatus()).thenReturn(RequestStatus.COMPLETED);

        ResponseEntity<?> randomRequestStatus = requestStatusController.getRandomRequestStatus();
        ResponseEntity<?> expected = ResponseEntity.status(HttpStatus.OK).body(RequestStatus.COMPLETED);

        assertEquals(expected.getStatusCode(), randomRequestStatus.getStatusCode());
        assertEquals(expected.getBody(), randomRequestStatus.getBody());
    }
}