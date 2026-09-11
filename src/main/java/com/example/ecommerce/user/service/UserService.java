package com.example.ecommerce.user.service;

import com.example.ecommerce.common.exception.DuplicateEmailException;
import com.example.ecommerce.common.exception.DuplicateUsernameException;
import com.example.ecommerce.common.exception.SamePasswordException;
import com.example.ecommerce.common.exception.UnauthorizedActionException;
import com.example.ecommerce.common.exception.UserNotFoundException;
import com.example.ecommerce.user.dto.UpdateUserRequestDto;
import com.example.ecommerce.user.dto.UserRegistrationDto;
import com.example.ecommerce.user.dto.UserResponseDto;
import com.example.ecommerce.user.entity.Role;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;






//admin gets all users
    public List<UserResponseDto> getUsers(){


List<User> users = userRepository.findAll();
       return users.stream().map(user -> buildUserResponse(user)).toList();

}


//get user by id
public UserResponseDto getUserById(Long id){
        var user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("user with id not found: " + id) );






        return  buildUserResponse(user);

}


//updating user and returning response

public UserResponseDto updateUser(Long id, UpdateUserRequestDto request){
       var  user =  userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found") );

       if(request.getUsername() != null && !request.getUsername().isBlank() ){
if(request.getUsername().equals(user.getUsername())){
    throw new DuplicateUsernameException("cannot change to the same username:" + request.getUsername() );
}
if(userRepository.findByUsername(request.getUsername()).isPresent()){
    throw new DuplicateUsernameException("username already taken");
}


user.setUsername(request.getUsername());
       }



if(request.getPassword() != null && !request.getPassword().isBlank() ){
     if (passwordEncoder.matches(request.getPassword(),user.getPassword())){

         throw new SamePasswordException("cannot change to previous password");



    }

          user.setPassword(passwordEncoder.encode(request.getPassword()));




}

       userRepository.save(user);

      return  buildUserResponse(user);





}


    public void  deleteUser(Long id){
        var user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("cannot find user with id " + id) );
                userRepository.delete(user);
    }

    private  UserResponseDto buildUserResponse (User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(user.getRole())
                .build();
    }






}
