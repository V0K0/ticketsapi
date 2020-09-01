package com.vozniuk.ticketsapi.controllers;

import com.vozniuk.ticketsapi.data.service.RequestStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestStatusController {

    private final RequestStatusService requestStatusService;

    public RequestStatusController(RequestStatusService requestStatusService) {
        this.requestStatusService = requestStatusService;
    }

    @GetMapping(value = "api/statuses/random")
    public ResponseEntity<?> getRandomRequestStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(requestStatusService.getRandomRequestStatus());
    }
}
