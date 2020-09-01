package com.vozniuk.ticketsapi;

import com.vozniuk.ticketsapi.controllers.RequestStatusController;
import com.vozniuk.ticketsapi.controllers.TravelController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SmokeTest {

    @Autowired
    private TravelController travelController;

    @Autowired
    private RequestStatusController requestStatusController;

    @Test
    void contextLoads() {
        assertNotNull(travelController);
        assertNotNull(requestStatusController);
    }

}
