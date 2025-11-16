package com.hacheery.ecommerce.security.service;

import com.hacheery.ecommerce.security.payload.AuthenticationRequest;
import com.hacheery.ecommerce.security.payload.AuthenticationResponse;
import com.hacheery.ecommerce.security.payload.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse login(AuthenticationRequest request);
}
