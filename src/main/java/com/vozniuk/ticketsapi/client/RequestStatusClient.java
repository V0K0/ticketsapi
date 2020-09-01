package com.vozniuk.ticketsapi.client;

import com.vozniuk.ticketsapi.controllers.RequestStatusController;
import com.vozniuk.ticketsapi.data.entity.RequestStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
@PropertySource("classpath:client.properties")
public class RequestStatusClient {

    @Value("${request.status.update.client.url}")
    private String requestStatusUrl;

    /**
     * Calls {@link RequestStatusController} through RestTemplate.
     * Controller returns {@link ResponseEntity} with {@link RequestStatus}
     */
    public RequestStatus getRandomRequestStatus() throws HttpStatusCodeException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RequestStatus> responseEntity
                = restTemplate.getForEntity(requestStatusUrl, RequestStatus.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new HttpClientErrorException(responseEntity.getStatusCode());
        }
    }
}
