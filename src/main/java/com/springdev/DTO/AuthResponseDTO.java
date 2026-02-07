package com.springdev.DTO;

import com.springdev.Entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AuthResponseDTO {

    private String token;
    private long id;
    private String username;
    private String email;
    private Set<String> roles;
}
