package com.vozniuk.ticketsapi.data.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "travelRequestId")
@Table(name = "travel_request")
public class TravelRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private long travelRequestId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", unique = true)
    private Ticket ticket;

    @Override
    public String toString() {
        return "TravelRequest{" +
                "travelRequestId=" + travelRequestId +
                ", status=" + status +
                ", ticket=" + ticket.getTicketId() +
                '}';
    }
}
