package com.springdev.DTO;

import com.springdev.Entity.Role;
import com.springdev.Entity.RoleName;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AssignRoleRequest {

    private long id;
    private Set<RoleName> roleName;
}
