package com.hacheery.ecommerce.security.controller;

import com.hacheery.ecommerce.dto.VerifyOtpRequest;
import com.hacheery.ecommerce.security.entity.User;
import com.hacheery.ecommerce.security.payload.AuthenticationRequest;
import com.hacheery.ecommerce.security.payload.AuthenticationResponse;
import com.hacheery.ecommerce.security.payload.RegisterRequest;
import com.hacheery.ecommerce.security.repository.UserRepository;
import com.hacheery.ecommerce.security.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        AuthenticationResponse response = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getVerificationCode() == null
                || !user.getVerificationCode().equals(req.getOtp())) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        if (user.getVerificationExpiry().isBefore(Instant.now())) {
            return ResponseEntity.badRequest().body("OTP expired");
        }

        // OTP correct
        user.setEnabled(true);
        user.setVerificationCode(null); // clear
        userRepository.save(user);

        return ResponseEntity.ok("Account verified successfully!");
    }

}
