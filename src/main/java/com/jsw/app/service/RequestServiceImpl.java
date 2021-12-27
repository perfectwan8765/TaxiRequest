package com.jsw.app.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.jsw.app.entity.Member;
import com.jsw.app.entity.Request;
import com.jsw.app.enums.RequestStatus;
import com.jsw.app.enums.UserRole;
import com.jsw.app.exception.AddressInvalidException;
import com.jsw.app.exception.CustomException;
import com.jsw.app.repository.MemberRepository;
import com.jsw.app.repository.RequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private MemberRepository memberRepository;

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
            throw new CustomException(HttpStatus.UNAUTHORIZED,  "로그인이 필요합니다.");
        }

        if (memberWrapper.isEmpty()) {
            log.error("Invalid Login Information");
            throw new CustomException(HttpStatus.UNAUTHORIZED,  "로그인이 필요합니다.");
        }

        Member member = memberWrapper.get();

        // 기사가 배차를 요청한 경우
        if (member.getUserType() != UserRole.PASSENGER) {
            throw new CustomException(HttpStatus.FORBIDDEN,  "승객만 배차 요청할 수 있습니다.");
        }

        // 아직 대기중인 배차가 있는 경우
        if (requestRepository.findNoAccetpedRequest(member.getId()) > 0) {
            throw new CustomException(HttpStatus.CONFLICT,  "아직 대기중인 배차 요청이 있습니다.");
        }

        // 주소 - 없는 경우
        if (address == null || address.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST,  "주소가 없습니다.");
        }
        // 주소 - 문자열(100)를 벗어난 경우
        if (address.length() > 100) {
            throw new CustomException(HttpStatus.BAD_REQUEST,  "주소는 100자 이하로 입력해주세요.");
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
            throw new CustomException(HttpStatus.UNAUTHORIZED,  "로그인이 필요합니다.");
        }

        if (memberWrapper.isEmpty()) {
            log.error("Invalid Login Information");
            throw new CustomException(HttpStatus.UNAUTHORIZED,  "로그인이 필요합니다.");
        }

        Optional<Request> requestWrapper = requestRepository.findById(taxiRequestId);

        // 배차 미 존재
        if (requestWrapper.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND,  "존재하지 않는 배차 요청입니다.");
        }
        Request request = requestWrapper.get();

        // 이미 다른 기사에 의해 바차 요청이 수락된 경우
        if (request.getStatus().equals(RequestStatus.ACCEPT) && request.getDriverId() == null) {
            throw new CustomException(HttpStatus.CONFLICT,  "수락할 수 없는 배차 요청입니다. 다른 배차 요청을 선택하세요.");
        }

        Member member = memberWrapper.get();

        if (member.getUserType() != UserRole.DRIVER) {
            throw new CustomException(HttpStatus.FORBIDDEN,  "기사만 배차 요청을 수락할 수 있습니다.");
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