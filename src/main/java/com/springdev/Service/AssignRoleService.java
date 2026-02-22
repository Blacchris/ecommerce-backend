package com.springdev.Service;

import com.springdev.CustomExceptions.UserNotFoundException;
import com.springdev.DTO.AssignRoleRequest;
import com.springdev.DTO.UserRoleResponse;
import com.springdev.Entity.Role;
import com.springdev.Entity.RoleName;
import com.springdev.Entity.User;
import com.springdev.Repository.RoleRepository;
import com.springdev.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleResponse assignRole(AssignRoleRequest request){
        User user1 = userRepository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Set<Role> newRoles = resolveRoles(request.getRoleName());
        user1.getRoles().addAll(newRoles);

        User user = userRepository.save(user1);

        return UserRoleResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRoles().stream()
                        .map(r -> r.getName().name())
                        .collect(Collectors.toSet()))
                .message(String.format("%s has been promoted", user.getUsername()))
                .build();
    }

    private Set<Role> resolveRoles(Set<RoleName> roleName){
        return roleName.stream()
                .map(name -> {
                    try {
                        return roleRepository.findByName(name)
                                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
                    } catch (RoleNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());

    }

}
