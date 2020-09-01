package com.vozniuk.ticketsapi.processor;

import com.vozniuk.ticketsapi.client.RequestStatusClient;
import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.entity.TravelRequest;
import com.vozniuk.ticketsapi.data.service.TravelRequestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@PropertySource("classpath:processor.properties")
public class PaymentProcessor {

    private final TravelRequestService travelRequestService;
    private final RequestStatusClient requestStatusClient;

    @Value("${processor.thread.count}")
    private int threadCount;
    @Value("${processor.thread.request.count.max}")
    private int threadProcessingRequestMaxCount;

    public PaymentProcessor(TravelRequestService travelRequestService, RequestStatusClient requestStatusClient) {
        this.travelRequestService = travelRequestService;
        this.requestStatusClient = requestStatusClient;
    }

    /**
     * Executes pseudo payment once in a minute
     * Method gets list of {@link TravelRequest} with {@link RequestStatus} 'PROCESSING' or 'IN_PROGRESS'.
     * If list size is greater than capacity (count of threads * count of requests they can process per minute)
     * then it get`s sublist and divide it with all threads.
     * In each thread it calls for random status in {@link RequestStatusClient} that perform a query to RequestStatusController
     * with RestTemplate. Starts with application startup if scheduling is enabled.
     */
    @Scheduled(fixedDelay = 60000)
    public void executePayment() {
        Optional<List<TravelRequest>> processingTravelRequests = travelRequestService.findProcessingTravelRequests();
        processingTravelRequests.ifPresent(this::divideListWithThreads);
    }

    private void divideListWithThreads(List<TravelRequest> allProcessingRequests) {

        final int capacity = threadProcessingRequestMaxCount * threadCount;

        if (allProcessingRequests.size() >= capacity) {
            extractSubListListAndDivideWithThreads(allProcessingRequests, capacity);
        } else {
            divideAllAvailableRequestsWithThreads(allProcessingRequests);
        }

    }

    private void extractSubListListAndDivideWithThreads(List<TravelRequest> allProcessingRequests, int capacity) {
        List<TravelRequest> maximumCapacityList = allProcessingRequests.subList(0, capacity);
        for (int i = 0; i < maximumCapacityList.size(); i += threadProcessingRequestMaxCount) {
            List<TravelRequest> threadSubList = maximumCapacityList.subList(i, i + threadProcessingRequestMaxCount);
            startNewThreadAndProcessList(threadSubList);
        }
    }

    private void divideAllAvailableRequestsWithThreads(List<TravelRequest> allProcessingRequests) {
        for (int i = allProcessingRequests.size(); i > 0; i -= threadProcessingRequestMaxCount) {
            List<TravelRequest> threadSubList;
            if (i - threadProcessingRequestMaxCount < 0) {
                threadSubList = allProcessingRequests.subList(0, i);
            } else {
                threadSubList = allProcessingRequests.subList(i - threadProcessingRequestMaxCount, i);
            }
            startNewThreadAndProcessList(threadSubList);
        }
    }

    private void startNewThreadAndProcessList(List<TravelRequest> forThreadList) {
        Thread thread = new Thread(new PaymentProcessorRunnable(forThreadList, requestStatusClient, travelRequestService));
        thread.start();
    }

}
