package com.vozniuk.ticketsapi.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Table(name = "ticket", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "departure_time", "route_number"})})
public class Ticket extends IndexedEntity {

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "departure_time", nullable = false)
    private Timestamp departureTime;

    @Column(name = "route_number", nullable = false)
    private int routeNumber;
    @JsonIgnore
    @OneToOne(mappedBy = "ticket", fetch = FetchType.LAZY)
    private TravelRequest travelRequest;

    public Ticket(long userId, Timestamp departureTime, int routeNumber) {
        this.routeNumber = routeNumber;
        this.departureTime = departureTime;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + id +
                ", departureTime=" + departureTime +
                ", routeNumber=" + routeNumber +
                "}";
    }

}
