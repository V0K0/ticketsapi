package com.vozniuk.ticketsapi.data.service.impl;

import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.entity.TravelRequest;
import com.vozniuk.ticketsapi.data.repository.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class PaymentServiceImpl {

    private final RequestRepository requestRepository;
    private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    public PaymentServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    /**
     * Executes pseudo payment once in a minute
     * Method gets all {@link TravelRequest} with with {@link RequestStatus} 'PROCESSING'
     * And chooses one to process.
     * Starts with application startup
     */
    @Scheduled(fixedDelay = 60000)
    public void executePayment() {
        Optional<List<TravelRequest>> optionalList = requestRepository.findAllByStatus(RequestStatus.PROCESSING);
        if (optionalList.isPresent()) {
            logger.info("Executing payment...");
            List<TravelRequest> travelRequests = optionalList.get();
            TravelRequest requestToProcess = travelRequests.stream().findAny().get();
            processRequest(requestToProcess);
        } else {
            logger.trace("All available requests are processed");
        }
    }

    /**
     * In this method {@link TravelRequest} randomly get processed
     * Chance of fail processing is lower than being succeed
     * Also the chance that {@link TravelRequest} won`t be processed is same as fail
     *
     * @param request is request with {@link RequestStatus} 'PROCESSING'
     */
    private void processRequest(TravelRequest request) {
        int processResult = ThreadLocalRandom.current().nextInt(-1, 10);
        switch (processResult) {
            case -1:
                request.setStatus(RequestStatus.ERROR);
                logger.error("Error occurred while operation process with request id: {}", request.getId());
                break;
            case 0:
                logger.info("Operation can`t be completed now. It`ll be processed later. Request: {}", request.getId());
                break;
            default:
                request.setStatus(RequestStatus.COMPLETED);
                logger.info("Payment successfully completed");
        }
    }


}
