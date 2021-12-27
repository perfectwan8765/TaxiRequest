package com.jsw.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jsw.app.entity.Request;
import com.jsw.app.service.RequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
* Taxi Requeests API
* 택시 배차 요청 관련 API
*/
@RestController
public class TaxiController {

    @Autowired
    private RequestService requestService;

    @GetMapping("/taxi-requests")
    public ResponseEntity<List<Request>> getCallList () {
        return ResponseEntity.ok(requestService.getRequestList());
    }

    @PostMapping("/taxi-requests")
    public ResponseEntity<Request> callTaxi (HttpServletRequest servletRequest, @RequestParam("address") String address) {
        String header = servletRequest.getHeader("Authorization");
        return ResponseEntity.ok(requestService.makeRequet(address, header));
    }

    @PostMapping("/taxi-requests/{taxiRequestId}/accept")
    public ResponseEntity<Request> acceptCall (HttpServletRequest servletRequest, @PathVariable("taxiRequestId") Long taxiRequestId) {
        String header = servletRequest.getHeader("Authorization");
        return ResponseEntity.ok(requestService.acceptRequest(taxiRequestId, header));
    }

}