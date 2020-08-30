package com.vozniuk.ticketsapi.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Table(name = "travel_request")
public class TravelRequest extends IndexedEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @JsonBackReference
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
