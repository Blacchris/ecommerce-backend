package com.example.ecommerce.user.dto;

import com.example.ecommerce.user.entity.Role;
import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;



}
