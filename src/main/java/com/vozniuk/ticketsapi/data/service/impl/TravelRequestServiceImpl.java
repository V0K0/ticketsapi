package com.vozniuk.ticketsapi.data.service.impl;

import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import com.vozniuk.ticketsapi.data.entity.TravelRequest;
import com.vozniuk.ticketsapi.data.repository.RequestRepository;
import com.vozniuk.ticketsapi.data.service.TravelRequestService;
import com.vozniuk.ticketsapi.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class TravelRequestServiceImpl implements TravelRequestService {

    private final RequestRepository requestRepository;

    private final RequestValidator requestValidator;

    @Autowired
    public TravelRequestServiceImpl(RequestRepository requestRepository, RequestValidator requestValidator) {
        this.requestRepository = requestRepository;
        this.requestValidator = requestValidator;
    }

    @Override
    public void saveRequest(TravelRequest travelRequest) {
        if (!validateRequest(travelRequest).hasErrors()) {
            travelRequest.setStatus(RequestStatus.PROCESSING);
            requestRepository.save(travelRequest);
        } else {
            throw new IllegalArgumentException("Entity TravelRequest is not valid");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TravelRequest getTravelRequestById(long id) throws EntityNotFoundException {
        return requestRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelRequest> getAllRequestsByUserId(long id) {
        Optional<List<TravelRequest>> allByUserId = requestRepository.findAllByUserId(id);
        if (allByUserId.isPresent()) {
            List<TravelRequest> travelRequests = allByUserId.get();
            Timestamp currentDate = Timestamp.valueOf(LocalDateTime.now());

            Predicate<TravelRequest> predicate = (request -> request.getTicket().getDepartureTime().compareTo(currentDate) > 0);

            return travelRequests.stream().filter(predicate).
                    sorted(Comparator.comparing(r -> r.getTicket().getDepartureTime())).
                    collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("No results for user with id: " + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<TravelRequest>> findProcessingTravelRequests() {
        return requestRepository.findAllNotCompletedRequests();
    }

    @Override
    public void changeRequestStatus(long id, RequestStatus requestStatus) {
        TravelRequest travelRequest = requestRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        travelRequest.setStatus(requestStatus);
    }


    private BindingResult validateRequest(TravelRequest travelRequest) {
        DataBinder dataBinder = new DataBinder(travelRequest);
        dataBinder.setValidator(requestValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }

}
