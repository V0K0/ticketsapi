package com.vozniuk.ticketsapi.data.repository;

import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.entity.TravelRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<TravelRequest, Long> {

    @Query("select tr from TravelRequest tr left join fetch tr.ticket")
    Optional<List<TravelRequest>> findAllFetchTicket();

    Optional<List<TravelRequest>> findAllByStatus(RequestStatus status);

    @Query("select tr from TravelRequest tr left join fetch tr.ticket where tr.status='PROCESSING' or tr.status='IN_PROGRESS'")
    Optional<List<TravelRequest>> findAllNotCompletedRequests();

    @Query("select tr from TravelRequest tr left join fetch tr.ticket where tr.ticket.userId = :id")
    Optional<List<TravelRequest>> findAllByUserId(long id);
}
