package com.vozniuk.ticketsapi.data.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Table(name = "travel_request")
public class TravelRequest extends IndexedEntity {

    public TravelRequest(RequestStatus status, Ticket ticket) {
        this.status = status;
        this.ticket = ticket;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", unique = true)
    private Ticket ticket;

    @Override
    public String toString() {
        return "TravelRequest{" +
                "travelRequestId=" + id +
                ", status=" + status +
                '}';
    }

}
