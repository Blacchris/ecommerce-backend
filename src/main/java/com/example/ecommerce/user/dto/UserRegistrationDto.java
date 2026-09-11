package com.example.ecommerce.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegistrationDto {

    @NotBlank(message = "username is required")
    private String username;

    @Email(message = "invalid email format")
    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message =  "password is required")
    @Size(min=8, message = "password cannot be less than 8 characters")
    private String password;




}
