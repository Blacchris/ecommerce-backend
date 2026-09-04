package com.example.ecommerce.user.service;

import com.example.ecommerce.common.exception.*;
import com.example.ecommerce.user.dto.updateUserRequestDto;
import com.example.ecommerce.user.dto.userRegistrationDto;
import com.example.ecommerce.user.dto.userResponseDto;
import com.example.ecommerce.user.entity.Role;
import com.example.ecommerce.user.entity.user;
import com.example.ecommerce.user.repository.userRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class userService {
    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //create new user and make him or her a customer automatically

    public userResponseDto createUserDto (userRegistrationDto request){


        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new DuplicateEmailException("email already taken " + request.getEmail());
        }
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new DuplicateUsernameException("username already taken " + request.getUsername());
        }


        user dtouser = new user();


        dtouser.setEmail(request.getEmail());


        dtouser.setPassword(passwordEncoder.encode(request.getPassword()));


        dtouser.setUsername(request.getUsername());
        dtouser.setRole(new HashSet<>(Set.of(Role.customer)));
        userRepository.save(dtouser);

userResponseDto newuser = new userResponseDto();
newuser.setEmail(dtouser.getEmail());
newuser.setUsername(dtouser.getUsername());
newuser.setId(dtouser.getId());
newuser.setRoles(dtouser.getRole());

         return newuser;
    }



//admin gets all users
    public List<userResponseDto> getUsers(Long id){

        user checkuser = userRepository.findById(id).orElseThrow(() ->new UserNotFoundException("user not found with id " + id)) ;
        if(!checkuser.getRole().contains(Role.admin) ){
            throw new UnauthorizedActionException("user is not an admin : " + checkuser.getRole());
        }
List<user> users = userRepository.findAll();
       return users.stream().map(user -> new userResponseDto(user.getId(), user.getUsername() ,user.getEmail(),user.getRole())).toList();

}


//get user by id
public userResponseDto getUserById(Long id){
        Optional<user> findUser = userRepository.findById(id);

        if(findUser.isEmpty()){
            throw new UserNotFoundException("no user with id found:" + id);
        }
        user user = findUser.get();

        userResponseDto dto = new userResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRole());

        return dto;

}


//updating user and returning response

public userResponseDto updateUser(Long id, updateUserRequestDto dto){
       user user =  userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found") );
if(dto.getUsername().equals(user.getUsername())){
    throw new DuplicateUsernameException("cannot change to the same username:" + dto.getUsername() );
}
    user.setUsername(dto.getUsername());

if(dto.getPassword() != null && !dto.getPassword().isBlank()){
if( passwordEncoder.matches(dto.getPassword(),user.getPassword())){
    throw new SamePasswordException("cannot change to previous password");
}

    user.setPassword(passwordEncoder.encode(dto.getPassword()));
}

       userRepository.save(user);

      return new userResponseDto(user.getId(),user.getUsername(),user.getEmail(),user.getRole()
      );


}


    public void  deleteUser(Long id){
        user user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("cannot find user with id " + id) );
                userRepository.delete(user);
    }






}
