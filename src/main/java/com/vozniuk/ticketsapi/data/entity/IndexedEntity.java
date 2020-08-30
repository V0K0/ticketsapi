package com.vozniuk.ticketsapi.data.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public abstract class IndexedEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "create_timestamp")
    @CreationTimestamp
    protected Timestamp createTimestamp;

    @Column(name = "update_timestamp")
    @UpdateTimestamp
    protected Timestamp updateTimestamp;

}
