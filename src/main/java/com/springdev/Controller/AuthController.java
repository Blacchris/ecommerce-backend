package com.springdev.Controller;

import com.springdev.DTO.AuthResponseDTO;
import com.springdev.DTO.UserLoginDTO;
import com.springdev.DTO.UserRegistrationDTO;
import com.springdev.DTO.UserResponseDTO;
import com.springdev.Entity.User;
import com.springdev.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/signup")
    public AuthResponseDTO registerUser(
           @Valid @RequestBody UserRegistrationDTO userRegistrationDTO){
        return authService.register(userRegistrationDTO);
    }

    @PostMapping(path = "/login")
    public AuthResponseDTO login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        return authService.login(userLoginDTO);
    }

}
