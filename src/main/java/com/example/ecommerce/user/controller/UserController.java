package com.example.ecommerce.user.controller;

import com.example.ecommerce.user.dto.updateUserRequestDto;
import com.example.ecommerce.user.dto.userRegistrationDto;
import com.example.ecommerce.user.dto.userResponseDto;
import com.example.ecommerce.user.entity.user;
import com.example.ecommerce.user.service.userService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final userService userService;

    @PostMapping("/register")
    public ResponseEntity<userResponseDto> register(@Valid @RequestBody userRegistrationDto request){
        return ResponseEntity.ok(userService.createUserDto(request));
    }

    @GetMapping
    public ResponseEntity<List<userResponseDto>> getAllUsers(@RequestParam Long adminId){
        return ResponseEntity.ok(userService.getUsers(adminId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<userResponseDto> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<userResponseDto> updateUserById(@PathVariable Long id, @Valid @RequestBody  updateUserRequestDto dto){
        return ResponseEntity.ok(userService.updateUser(id,dto));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleterUserById(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
