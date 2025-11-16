package com.hacheery.ecommerce.security.service.impl;

import com.hacheery.ecommerce.security.entity.Role;
import com.hacheery.ecommerce.security.entity.User;
import com.hacheery.ecommerce.security.mapper.UserMapper;
import com.hacheery.ecommerce.security.payload.AuthenticationRequest;
import com.hacheery.ecommerce.security.payload.AuthenticationResponse;
import com.hacheery.ecommerce.security.payload.RegisterRequest;
import com.hacheery.ecommerce.security.repository.UserRepository;
import com.hacheery.ecommerce.security.service.AuthenticationService;
import com.hacheery.ecommerce.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utils.OtpUtil;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setRole(Role.ADMIN);

        // Sinh OTP
        String otp = OtpUtil.generateOtp();
        newUser.setVerificationCode(otp);
        newUser.setVerificationExpiry(Instant.now().plusSeconds(300)); // hết hạn sau 5 phút

        User createdUser = userRepository.save(newUser);

        // Gửi mail
        mailService.sendMailAsync(createdUser.getEmail(), "Email Verification Code", "Your verification code is: " + otp);

        // Tạo JWT token nhưng không lưu vào DB
        String jwtToken = jwtService.generateToken(createdUser);

        return AuthenticationResponse.builder()
                .userDto(UserMapper.mapToUserDto(createdUser))
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        // Tạo JWT token nhưng không lưu vào DB
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .userDto(UserMapper.mapToUserDto(user))
                .token(jwtToken)
                .build();
    }
}
