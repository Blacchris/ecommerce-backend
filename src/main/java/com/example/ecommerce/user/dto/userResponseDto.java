package com.example.ecommerce.user.dto;

import com.example.ecommerce.user.entity.Role;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class userResponseDto {
    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;



}
