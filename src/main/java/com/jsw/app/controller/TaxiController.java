package com.jsw.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jsw.app.entity.Request;
import com.jsw.app.exception.AddressInvalidException;
import com.jsw.app.service.RequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
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
    public ResponseEntity callTaxi (HttpServletRequest servletRequest, @RequestParam("address") String address) {
        String header = servletRequest.getHeader("Authorization");

        Request request;
        try {
            request = requestService.makeRequet(address, header);
        } catch (AddressInvalidException ae) {
            // 주소가 없거나 주소의 허용된 문자열 길이(1~100자)를 벗어난 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ae.getMessage());
        } catch (AuthenticationCredentialsNotFoundException ace) {
            // 인증 정보가 유효하지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ace.getMessage());
        } catch (Exception e) {
            // 승객이 아닌 유저가 배차 요청한 경우
            if (e.getMessage().startsWith("승객만")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            // 승객의 기존 배차 요청이 수락되지 않은 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        return ResponseEntity.ok(request);
    }

    @PostMapping("/taxi-requests/{taxiRequestId}/accept")
    public ResponseEntity acceptCall (HttpServletRequest servletRequest, @PathVariable("taxiRequestId") Long taxiRequestId) {
        String header = servletRequest.getHeader("Authorization");

        Request request;
        try {
            request = requestService.acceptRequest(taxiRequestId, header);
        } catch (AuthenticationCredentialsNotFoundException ace) {
            // 인증 정보가 유효하지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ace.getMessage());
        } catch (NullPointerException ne) {
            // 요청한 택시 배차 요청이 존재하지 않는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ne.getMessage());
        } catch (IllegalArgumentException ie) {
            // 이미 다른 기사에 의해 배차 요청이 수락된 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ie.getMessage());
        } catch (Exception e) {
            // 기사가 아닌 유저가 배차 요청을 수락하려고 하는 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

        return ResponseEntity.ok(request);
    }

}
