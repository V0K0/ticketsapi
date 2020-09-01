package com.vozniuk.ticketsapi.data.service;

import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.entity.TravelRequest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


public interface TravelRequestService {

    void saveRequest(TravelRequest travelRequest) throws IllegalArgumentException;

    TravelRequest getTravelRequestById(long id) throws EntityNotFoundException;

    List<TravelRequest> getAllRequestsByUserId(long id) throws EntityNotFoundException;

    Optional<List<TravelRequest>> findProcessingTravelRequests();

    void changeRequestStatus(long id, RequestStatus requestStatus);
}
