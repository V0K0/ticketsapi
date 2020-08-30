package com.vozniuk.ticketsapi.data.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Table(name = "ticket")
public class Ticket extends IndexedEntity {

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
                "ticketId=" + id +
                ", departureTime=" + departureTime +
                ", routeNumber=" + routeNumber +
                "}";
    }


}
