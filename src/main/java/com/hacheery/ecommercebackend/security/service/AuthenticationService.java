package com.hacheery.ecommercebackend.security.service;

import com.hacheery.ecommercebackend.dto.UserDto;
import com.hacheery.ecommercebackend.exception.BadCredentialsException;
import com.hacheery.ecommercebackend.exception.DuplicateException;
import com.hacheery.ecommercebackend.exception.SQLException;
import com.hacheery.ecommercebackend.mapper.UserMapper;
import com.hacheery.ecommercebackend.security.entity.Role;
import com.hacheery.ecommercebackend.security.entity.Token;
import com.hacheery.ecommercebackend.security.entity.TokenType;
import com.hacheery.ecommercebackend.security.entity.User;
import com.hacheery.ecommercebackend.security.model.AuthenticationRequest;
import com.hacheery.ecommercebackend.security.model.AuthenticationResponse;
import com.hacheery.ecommercebackend.security.model.RegisterRequest;
import com.hacheery.ecommercebackend.security.repository.TokenRepository;
import com.hacheery.ecommercebackend.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        boolean isEmailExist = repository.existsByEmail(request.getEmail());
        if (isEmailExist) {
            throw new DuplicateException("Email already exists");
        }
        try {
            var user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            var savedUser = repository.save(user);
            var jwtToken = jwtService.generateToken(savedUser);
            saveUserToken(savedUser, jwtToken);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (DataIntegrityViolationException e) {
            throw new SQLException(e.getLocalizedMessage(), e);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            UserDto userDto = UserMapper.mapToUserDto(user);
            return AuthenticationResponse.builder()
                    .user(userDto)
                    .token(jwtToken)
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .userId(user.getId())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
