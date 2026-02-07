package com.springdev.Service;

import com.springdev.DTO.AuthResponseDTO;
import com.springdev.DTO.UserLoginDTO;
import com.springdev.DTO.UserRegistrationDTO;
import com.springdev.DTO.UserResponseDTO;
import com.springdev.Entity.Role;
import com.springdev.Entity.RoleName;
import com.springdev.Entity.User;
import com.springdev.JWT.JwtService;
import com.springdev.Repository.RoleRepository;
import com.springdev.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final static String USER_NOT_FOUND = "%s is already taken";
    private final static String EMAIL_IS_TAKEN = "Email: %s is taken";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(UserRegistrationDTO userRegistrationDTO){
        validateUser(userRegistrationDTO);
        Role role = roleRepository.findByName(RoleName.CUSTOMER)
                .orElseThrow(() -> new IllegalStateException("Role not found"));
        User user1 = new User(
                userRegistrationDTO.getUsername(),
                userRegistrationDTO.getEmail(),
                passwordEncoder.encode(userRegistrationDTO.getPassword())
        );
        user1.getRoles().add(role);
        String token = jwtService.generateToken(userRepository.save(user1).getUsername());
        return AuthResponseDTO.builder()
                .id(user1.getId())
                .username(user1.getUsername())
                .email(user1.getEmail())
                .roles(user1.getRoles()
                        .stream()
                        .map(role1 -> role1.getName().name())
                        .collect(Collectors.toSet()))
                .token(token)
                .build();

    }

    public AuthResponseDTO login(UserLoginDTO userLoginDTO){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDTO.getUsername(),
                            userLoginDTO.getPassword()
                    )
            );
        }catch(BadCredentialsException | UsernameNotFoundException e){
            throw new BadCredentialsException("Invalid username or password");
        }
        User user = userRepository.findByUsername(userLoginDTO.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        String token = jwtService.generateToken(user.getUsername());
        return AuthResponseDTO.builder()
                .token(token)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles()
                        .stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet()))
                .build();
    }


    void validateUser(UserRegistrationDTO userRegistrationDTO){
        if(userRepository.findByUsername(userRegistrationDTO.getUsername()).isPresent()){
            throw new IllegalStateException(String.format(USER_NOT_FOUND, userRegistrationDTO.getUsername()));
        }
        if(userRepository.findByEmail(userRegistrationDTO.getEmail()).isPresent()){
            throw new IllegalStateException(String.format(EMAIL_IS_TAKEN, userRegistrationDTO.getEmail()));
        }
    }

}
