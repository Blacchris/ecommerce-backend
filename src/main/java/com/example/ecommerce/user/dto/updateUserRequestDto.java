package com.example.ecommerce.user.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class updateUserRequestDto {
    @Size(min = 3, max = 50, message = "username must be between 2 and 51")
    private String username;


    @Size(min = 8, max =72 ,message = "password must be at least 8 characters")
    private String password;
}
