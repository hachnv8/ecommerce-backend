package com.hacheery.ecommerce.security.service;

import com.hacheery.ecommerce.security.entity.User;

public interface JwtService {
    String extractUsername(String token);
    String generateToken(User user);
}
