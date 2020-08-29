package com.vozniuk.ticketsapi.data.service;

import com.vozniuk.ticketsapi.data.dao.RequestDao;
import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.entity.TravelRequest;
import com.vozniuk.ticketsapi.data.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityNotFoundException;

@Transactional
@Service
public class TravelRequestService implements RequestDao {

    private final RequestRepository requestRepository;

    @Autowired
    public TravelRequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public void saveNewRequest(TravelRequest travelRequest) {
        if (isTravelRequestValid(travelRequest)) {
            travelRequest.setStatus(RequestStatus.PROCESSING);
            requestRepository.save(travelRequest);
        } else {
          throw new IllegalArgumentException("Entity TravelRequest is not valid");
        }
    }

    @Override
    public TravelRequest getTravelRequestById(long id) throws EntityNotFoundException {
        return requestRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private boolean isTravelRequestValid(TravelRequest travelRequest) {
        return travelRequest != null && travelRequest.getTicket() != null;
    }


}
