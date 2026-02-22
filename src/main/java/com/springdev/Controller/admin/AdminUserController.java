package com.springdev.Controller.admin;

import com.springdev.DTO.AssignRoleRequest;
import com.springdev.DTO.UserResponseDTO;
import com.springdev.DTO.UserRoleResponse;
import com.springdev.Service.AssignRoleService;
import com.springdev.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/api/admin/user")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {


    final private AssignRoleService assignRoleService;
    final private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserRoleResponse> assignRole(
            @RequestBody AssignRoleRequest request){
        return ResponseEntity.ok(assignRoleService.assignRole(request));
    }
}
