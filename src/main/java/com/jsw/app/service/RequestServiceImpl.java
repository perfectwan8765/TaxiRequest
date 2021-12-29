package com.jsw.app.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.jsw.app.entity.Member;
import com.jsw.app.entity.Request;
import com.jsw.app.enums.RequestStatus;
import com.jsw.app.enums.UserRole;
import com.jsw.app.exception.CustomException;
import com.jsw.app.repository.MemberRepository;
import com.jsw.app.repository.RequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public List<Request> getRequestList ()  {
        return requestRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
    * Taxi Call 요청 (승객)
    * @param address Taxi를 call할 주소
    * @param header HttpRequest header 중 인증 Header(Authorization)
    * @return Request 요청한 Request 객체
    * @ author 정상완
    */
    @Override
    public Request makeRequet (String address, String header) throws CustomException {
        Optional<Member> memberWrapper;
        try {
            memberWrapper = getMemberFromToken(header);
        } catch (SignatureVerificationException se) {
            // Token 형식이 잘못된 경우
            log.error("Invalid token format: {}", se.getMessage());
            throw new CustomException(HttpStatus.UNAUTHORIZED, messageSource.getMessage("err.login.needLogin", null, Locale.getDefault()));
        }

        if (memberWrapper.isEmpty()) {
            log.error("Invalid Login Information");
            throw new CustomException(HttpStatus.UNAUTHORIZED, messageSource.getMessage("err.login.needLogin", null, Locale.getDefault()));
        }

        Member member = memberWrapper.get();

        // 기사가 배차를 요청한 경우
        if (member.getUserType() != UserRole.PASSENGER) {
            throw new CustomException(HttpStatus.FORBIDDEN, messageSource.getMessage("err.request.onlyRequestPassanger", null, Locale.getDefault()));
        }

        // 아직 대기중인 배차가 있는 경우
        if (requestRepository.findNoAccetpedRequest(member.getId()) > 0) {
            throw new CustomException(HttpStatus.CONFLICT, messageSource.getMessage("err.request.existsWaitingRequest", null, Locale.getDefault()));
        }

        // 주소 - 없는 경우
        if (address == null || address.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, messageSource.getMessage("err.request.noAddress", null, Locale.getDefault()));
        }
        // 주소 - 문자열(100)를 벗어난 경우
        if (address.length() > 100) {
            throw new CustomException(HttpStatus.BAD_REQUEST, messageSource.getMessage("err.request.under100", null, Locale.getDefault()));
        }

        Date now = new Date(System.currentTimeMillis());

        Request request = new Request();
        request.setPassengerId(member.getId());
        request.setAddress(address);
        request.setStatus(RequestStatus.REQUEST);
        request.setCreatedAt(now);
        request.setUpdatedAt(now);

        return requestRepository.save(request);
    }

    /**
    * Taxi Call 요청 수락 (기사)
    * @param taxiRequestId 수락해줄 Taxi Call ID
    * @param header HttpRequest header 중 인증 Header(Authorization)
    * @return Request 요청 수락한 Request 객체
    * @ author 정상완
    */
    @Override
    public Request acceptRequest (Long taxiRequestId, String header) throws CustomException {
        Optional<Member> memberWrapper;

        try {
            memberWrapper = getMemberFromToken(header);
        } catch (SignatureVerificationException se) {
            // Token 형식이 잘못된 경우
            log.error("Invalid token format: {}", se.getMessage());
            throw new CustomException(HttpStatus.UNAUTHORIZED, messageSource.getMessage("err.login.needLogin", null, Locale.getDefault()));
        }

        if (memberWrapper.isEmpty()) {
            log.error("Invalid Login Information");
            throw new CustomException(HttpStatus.UNAUTHORIZED, messageSource.getMessage("err.login.needLogin", null, Locale.getDefault()));
        }

        Optional<Request> requestWrapper = requestRepository.findById(taxiRequestId);

        // 배차 미 존재
        if (requestWrapper.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, messageSource.getMessage("err.request.noRequest", null, Locale.getDefault()));
        }
        Request request = requestWrapper.get();

        // 이미 다른 기사에 의해 바차 요청이 수락된 경우
        if (request.getStatus().equals(RequestStatus.ACCEPT) && request.getDriverId() == null) {
            throw new CustomException(HttpStatus.CONFLICT, messageSource.getMessage("err.request.unacceptableRequest", null, Locale.getDefault()));
        }

        Member member = memberWrapper.get();

        if (member.getUserType() != UserRole.DRIVER) {
            throw new CustomException(HttpStatus.FORBIDDEN, messageSource.getMessage("err.request.onlyAcceptDriver", null, Locale.getDefault()));
        }

        Date now = new Date(System.currentTimeMillis());
        request.setDriverId(member.getId());
        request.setStatus(RequestStatus.ACCEPT);
        request.setUpdatedAt(now);
        request.setAcceptedAt(now);

        log.info("Accept Request: {}", request);
        return requestRepository.save(request);
    }

    /**
    * Token에서 Member 정보 가져오기
    * @param header HttpRequest header 중 인증 Header(Authorization)
    * @return Optional<Member> Member 객체
    * @ author 정상완
    */
    private Optional<Member> getMemberFromToken (String header) throws SignatureVerificationException {
        String email = JWT.require(Algorithm.HMAC512("taxiapi".getBytes()))
                    .build()
                    .verify(header.replace("token ", ""))
                    .getSubject();

        return memberRepository.findByEmail(email);
    }
    
}