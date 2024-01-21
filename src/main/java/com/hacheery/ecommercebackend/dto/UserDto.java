package com.hacheery.ecommercebackend.dto;

import com.hacheery.ecommercebackend.security.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Date dateOfBirth;
    private String address;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
}