package com.springdev.Initializer;

import com.springdev.Entity.Role;
import com.springdev.Entity.RoleName;
import com.springdev.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    final private RoleRepository roleRepository;

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
    }
}
