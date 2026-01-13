package com.springdev.DTO;

import com.springdev.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private long id;
    private String username;
    private String email;
    private Set<String> roles = new HashSet<>();


}
