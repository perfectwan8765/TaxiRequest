package com.jsw.app.repository;

import java.util.List;
import java.util.Optional;

import com.jsw.app.entity.Request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestRepository extends JpaRepository<Request, Long> {

    public List<Request> findAllByOrderByCreatedAtDesc();

    @Query(value = "SELECT count(r) FROM request r WHERE passenger_id = :passengerId AND driver_id IS NULL",
        nativeQuery = true)
    Long findNoAccetpedRequest(@Param("passengerId") Long passengerId);
    
}