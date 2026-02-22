package com.springdev.DTO;

import com.springdev.Entity.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleResponse {

    private long id;
    private String username;
    private String email;
    private Set<String> role;
    private String message;
}
