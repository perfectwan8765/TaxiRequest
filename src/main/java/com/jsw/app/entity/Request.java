package com.jsw.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.jsw.app.converter.StatusConverter;
import com.jsw.app.enums.RequestStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="REQUEST")
@SequenceGenerator (
    name = "REQUEST_ID_GENERATOR",
    sequenceName = "REQUEST_SEQ",
    initialValue = 1,
    allocationSize = 1 // default가 50임. 사용할때 조심!!!
)
public class Request {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUEST_ID_GENERATOR" )
    private Long id;

    @Column(name="ADDRESS", nullable=false, length=100)
    private String address;

    @Column(name="DRIVER_ID")
    private Long driverId;

    @Column(name="PASSENGER_ID", nullable=false)
    private Long passengerId;
    
    @Column(name="STATUS")
    private RequestStatus status; // null or accepted

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ACCEPTED_AT")
    private Date acceptedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_AT")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UPDATED_AT")
    private Date updatedAt;
    
}
