package com.hacheery.ecommercebackend.security.controller;

import com.hacheery.ecommercebackend.security.model.AuthenticationRequest;
import com.hacheery.ecommercebackend.security.model.AuthenticationResponse;
import com.hacheery.ecommercebackend.security.model.RegisterRequest;
import com.hacheery.ecommercebackend.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        logger.info(String.format("Register new user: username = %s, password = %s, email = %s",
                registerRequest.getName(), registerRequest.getPassword(), registerRequest.getEmail()));
        return ResponseEntity.ok(service.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        logger.info(String.format("email = %s, password = %s",
                request.getEmail(), request.getPassword()));
        return ResponseEntity.ok(service.authenticate(request));
    }


}
