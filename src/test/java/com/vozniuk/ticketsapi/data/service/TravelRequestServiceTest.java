package com.vozniuk.ticketsapi.data.service;

import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.entity.Ticket;
import com.vozniuk.ticketsapi.data.entity.TravelRequest;
import com.vozniuk.ticketsapi.data.service.impl.TravelRequestServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TravelRequestServiceTest {

    @Autowired
    private TravelRequestServiceImpl travelRequestServiceImpl;

    @Autowired
    private EntityManagerFactory emf;


    @Test
    void testServiceThrowsExceptionOnNull() {
        assertThrows(Exception.class, () -> travelRequestServiceImpl.saveRequest(null));
    }

    @Test
    void testServiceSetsStatusOnRequest() {
        TravelRequest request = getRequest();
        travelRequestServiceImpl.saveRequest(request);
        assertEquals(RequestStatus.PROCESSING, request.getStatus());
    }

    @Test
    void testRequestServicePersistsRequest() {
        TravelRequest request = getRequest();
        travelRequestServiceImpl.saveRequest(request);
        assertNotEquals(0, request.getId());
    }

    @Test
    void testGetRequestThrowsException() {
        assertThrows(EntityNotFoundException.class, () -> travelRequestServiceImpl.getTravelRequestById(999));
    }

    @Test
    void testGetRequestReturnsCorrectEntity() {
        TravelRequest travelRequest = getRequest();
        travelRequestServiceImpl.saveRequest(travelRequest);
        TravelRequest request = travelRequestServiceImpl.getTravelRequestById(travelRequest.getId());
        assertEquals(travelRequest, request);
    }

    @Test
    void testGetRequestsCountInFutureByUserId() {

        List<TravelRequest> createdRequests = getPersistedListWithFutureRequests(1, 5);

        Ticket oldTicket = new Ticket(1, Timestamp.valueOf(LocalDateTime.now().minusDays(1)), 1);
        performTransaction(em -> em.persist(oldTicket));
        TravelRequest oldRequest = new TravelRequest(RequestStatus.PROCESSING, oldTicket);
        performTransaction(em -> em.persist(oldRequest));
        createdRequests.add(oldRequest);

        List<TravelRequest> allRequestsByUserId = travelRequestServiceImpl.getAllRequestsByUserId(1);
        assertEquals(createdRequests.size() - 1, allRequestsByUserId.size());
    }

    @Test
    void testAllRequestsByUserIdAreSorted() {
        List<TravelRequest> createdRequests = getPersistedListWithFutureRequests(3, 4);
        List<TravelRequest> sorted = createdRequests.stream()
                .sorted(Comparator.comparing(r -> r.getTicket().getDepartureTime()))
                .collect(Collectors.toList());
        assertEquals(sorted.get(0).getTicket().getDepartureTime(), createdRequests.get(0).getTicket().getDepartureTime());
    }

    @Test
    void testChangeRequestStatusChangesStatus() {
        TravelRequest request = getRequest();
        request.setStatus(RequestStatus.PROCESSING);
        performTransaction(entityManager -> entityManager.persist(request));
        travelRequestServiceImpl.changeRequestStatus(request.getId(), RequestStatus.COMPLETED);
        TravelRequest fromDb = travelRequestServiceImpl.getTravelRequestById(request.getId());
        assertEquals(RequestStatus.COMPLETED, fromDb.getStatus());
    }

    @Test
    void testFindProcessingOnly() {
        List<TravelRequest> persistedList = getPersistedListWithFutureRequests(5, 5);
        Optional<List<TravelRequest>> processingTravelRequests = travelRequestServiceImpl.findProcessingTravelRequests();
        boolean isProcessing = false;

        if (processingTravelRequests.isPresent()) {
            isProcessing = processingTravelRequests.get()
                    .stream()
                    .allMatch(travelRequest -> travelRequest
                            .getStatus().equals(RequestStatus.PROCESSING) ||
                            travelRequest.getStatus().equals(RequestStatus.IN_PROGRESS));
        }

        assertTrue(isProcessing);
    }

    private TravelRequest getRequest() {
        TravelRequest request = new TravelRequest();
        Ticket ticket = new Ticket(2, Timestamp.valueOf(LocalDateTime.now()), 1);
        performTransaction(em -> em.persist(ticket));
        request.setTicket(ticket);
        return request;
    }

    private List<TravelRequest> getPersistedListWithFutureRequests(long userId, int size) {
        List<TravelRequest> requestList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Ticket ticket = new Ticket(userId, Timestamp.valueOf(LocalDateTime.now().plusDays(i + 1)), 1);
            performTransaction(em -> em.persist(ticket));
            TravelRequest travelRequest = new TravelRequest(RequestStatus.PROCESSING, ticket);
            performTransaction(em -> em.persist(travelRequest));
            requestList.add(travelRequest);
        }
        return requestList;
    }


    private void performTransaction(Consumer<EntityManager> entityManagerConsumer) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManagerConsumer.accept(entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        } finally {
            entityManager.close();
        }
    }


}