package com.springdev.Initializer;

import com.springdev.Entity.Role;
import com.springdev.Entity.RoleName;
import com.springdev.Entity.User;
import com.springdev.Repository.RoleRepository;
import com.springdev.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DataInitializer implements CommandLineRunner {

    final private static String ROLE_NOT_FOUND = "Role not Found";

    final private RoleRepository roleRepository;
    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;


    @Override
    public void run(String... args) throws Exception {

        if(!roleRepository.existsByName(RoleName.ADMIN)){
            roleRepository.save(new Role(RoleName.ADMIN));
        }

        if(!roleRepository.existsByName(RoleName.SELLER)){
            roleRepository.save(new Role(RoleName.SELLER));
        }

        if(!roleRepository.existsByName(RoleName.CUSTOMER)){
            roleRepository.save(new Role(RoleName.CUSTOMER));
        }

        if(userRepository.findByEmail(email).isEmpty()){
           User admin = User.builder()
                   .username("admin")
                   .email(email)
                   .password(passwordEncoder.encode(password))
                   .roles(new HashSet<>())
                   .build();

           Role role = roleRepository.findByName(RoleName.ADMIN)
                   .orElseThrow(() -> new RoleNotFoundException(ROLE_NOT_FOUND));

           admin.getRoles().add(role);
           userRepository.save(admin);
           log.info("Default Admin Created!");
        }
    }
}
