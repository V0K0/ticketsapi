package com.vozniuk.ticketsapi.data.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "ticketId")
@Table(name = "ticket")
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private long ticketId;

    @Column(name = "departure_time", nullable = false)
    private Timestamp departureTime;

    @Column(name = "route_number", nullable = false)
    private int routeNumber;

    @JsonManagedReference
    @OneToOne(mappedBy = "ticket", fetch = FetchType.LAZY)
    private TravelRequest travelRequest;

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", departureTime=" + departureTime +
                ", routeNumber=" + routeNumber +
                ", travel_request_id=" + travelRequest.getTravelRequestId() +
                '}';
    }
}
