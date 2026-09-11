package com.example.ecommerce.user.controller;

import com.example.ecommerce.user.dto.UpdateUserRequestDto;

import com.example.ecommerce.user.dto.UserResponseDto;
import com.example.ecommerce.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;




    @GetMapping("/admin")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDto dto){
        return ResponseEntity.ok(userService.updateUser(id,dto));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleterUserById(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
