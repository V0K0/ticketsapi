package com.vozniuk.ticketsapi.processor;

import com.vozniuk.ticketsapi.client.RequestStatusClient;
import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.entity.TravelRequest;
import com.vozniuk.ticketsapi.data.service.TravelRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class PaymentProcessorRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PaymentProcessorRunnable.class);

    private final List<TravelRequest> travelRequests;

    private final RequestStatusClient requestStatusClient;

    private final TravelRequestService travelRequestService;

    PaymentProcessorRunnable(List<TravelRequest> travelRequests, RequestStatusClient requestStatusClient, TravelRequestService travelRequestService) {
        this.travelRequests = travelRequests;
        this.requestStatusClient = requestStatusClient;
        this.travelRequestService = travelRequestService;
    }

    /**
     * Method invokes {@link RequestStatusClient} getRandomStatus() for each {@link TravelRequest} in list.
     * Before calling client it changes request status to 'IN_PROGRESS' if something wrong happen than request could be
     * processed later in future. If we got an answer than we update request with new status and go to next or finish operation.
     */
    @Override
    public void run() {
        logger.info("Executing payment with bunch of requests");
        travelRequests.forEach(travelRequest -> {
            logger.info("Executing payment for request: {}", travelRequest.getId());
            travelRequestService.changeRequestStatus(travelRequest.getId(), RequestStatus.IN_PROGRESS);
            RequestStatus receivedRequestStatus = requestStatusClient.getRandomRequestStatus();
            travelRequestService.changeRequestStatus(travelRequest.getId(), receivedRequestStatus);
            logger.info("Completed operation for request: {}, result: {}", travelRequest.getId(), receivedRequestStatus);
        });
        logger.info("All payments completed");
    }
}
