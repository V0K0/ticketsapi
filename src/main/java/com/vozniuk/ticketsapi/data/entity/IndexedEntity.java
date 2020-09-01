package com.vozniuk.ticketsapi.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
public abstract class IndexedEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "created_at")
    @CreationTimestamp
    protected Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    protected Timestamp updatedAt;

}
