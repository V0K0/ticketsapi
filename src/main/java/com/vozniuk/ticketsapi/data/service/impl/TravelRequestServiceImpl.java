package com.vozniuk.ticketsapi.data.service.impl;

import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.entity.TravelRequest;
import com.vozniuk.ticketsapi.data.repository.RequestRepository;
import com.vozniuk.ticketsapi.data.service.TravelRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Transactional
@Service
public class TravelRequestServiceImpl implements TravelRequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public TravelRequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public void saveRequest(TravelRequest travelRequest) {
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
