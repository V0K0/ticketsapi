package com.vozniuk.ticketsapi.data.service.impl;

import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.service.RequestStatusService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class RequestStatusServiceImpl implements RequestStatusService {

    /**
     * Simply returns random {@link RequestStatus}.
     * Chance of fail status is lower than successful.
     */
    @Override
    public RequestStatus getRandomRequestStatus() {
        int processResult = ThreadLocalRandom.current().nextInt(-2, 10);
        return processResult < 0 ? RequestStatus.ERROR : RequestStatus.COMPLETED;
    }
}
