package com.vozniuk.ticketsapi.data.service;

import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.entity.Ticket;
import com.vozniuk.ticketsapi.data.entity.TravelRequest;
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
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TravelRequestServiceTest {

    @Autowired
    private TravelRequestService travelRequestService;

    @Autowired
    private EntityManagerFactory emf;


    @Test
    void testServiceThrowsExceptionRequestWithoutTicket() {
        TravelRequest request = new TravelRequest();
        Exception ex = assertThrows(IllegalArgumentException.class, () -> travelRequestService.saveRequest(request));
        assertEquals("Entity TravelRequest is not valid", ex.getMessage());
    }

    @Test
    void testServiceThrowsExceptionOnNull() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> travelRequestService.saveRequest(null));
        assertEquals("Entity TravelRequest is not valid", ex.getMessage());
    }

    @Test
    void testServiceSetsStatusOnRequest() {
        TravelRequest request = getRequest();
        travelRequestService.saveRequest(request);
        assertEquals(RequestStatus.PROCESSING, request.getStatus());
    }

    @Test
    void testRequestServicePersistsRequest() {
        TravelRequest request = getRequest();
        travelRequestService.saveRequest(request);
        assertNotEquals(0, request.getId());
    }

    @Test
    void testGetRequestThrowsException(){
        assertThrows(EntityNotFoundException.class, () -> travelRequestService.getTravelRequestById(999));
    }

    @Test
    void testGetRequestReturnsCorrectEntity() {
        TravelRequest travelRequest = getRequest();
        travelRequestService.saveRequest(travelRequest);
        TravelRequest request = travelRequestService.getTravelRequestById(travelRequest.getId());
        assertEquals(travelRequest, request);
    }

    private TravelRequest getRequest() {
        TravelRequest request = new TravelRequest();
        Ticket ticket = getTicket();
        performTransaction(em -> em.persist(ticket));
        request.setTicket(ticket);
        return request;
    }

    private Ticket getTicket() {
        Ticket ticket = new Ticket();
        ticket.setRouteNumber(1);
        ticket.setDepartureTime(Timestamp.valueOf(LocalDateTime.now()));
        return ticket;
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