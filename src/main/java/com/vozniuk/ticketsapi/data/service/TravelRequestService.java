package com.vozniuk.ticketsapi.data.service;

import com.vozniuk.ticketsapi.data.entity.TravelRequest;

public interface TravelRequestService {
    void saveRequest(TravelRequest travelRequest);

    TravelRequest getTravelRequestById(long id);
}
