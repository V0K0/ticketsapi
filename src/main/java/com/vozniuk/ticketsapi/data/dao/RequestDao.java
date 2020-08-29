package com.vozniuk.ticketsapi.data.dao;

import com.vozniuk.ticketsapi.data.entity.TravelRequest;

public interface RequestDao {
    void saveNewRequest(TravelRequest travelRequest);
    TravelRequest getTravelRequestById(long id);
}
