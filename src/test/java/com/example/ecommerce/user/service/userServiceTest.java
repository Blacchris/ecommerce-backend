package com.example.ecommerce.user.service;

import com.example.ecommerce.common.exception.DuplicateEmailException;
import com.example.ecommerce.common.exception.DuplicateUsernameException;
import com.example.ecommerce.common.exception.UnauthorizedActionException;
import com.example.ecommerce.common.exception.UserNotFoundException;
import com.example.ecommerce.user.dto.updateUserRequestDto;
import com.example.ecommerce.user.dto.userRegistrationDto;
import com.example.ecommerce.user.dto.userResponseDto;
import com.example.ecommerce.user.entity.Role;
import com.example.ecommerce.user.entity.user;
import com.example.ecommerce.user.repository.userRepository;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class userServiceTest {

    @Mock
  private  userRepository userRepository;
    @InjectMocks
    private userService userService;

    @Mock
    private PasswordEncoder passwordEncoder;



    @Test
    void createUserDto() {
        userRegistrationDto request = new userRegistrationDto();
        request.setPassword("hash1234");
        request.setEmail("steve@gmail.com");
        request.setUsername("steve");

        user user = new user();
        user.setEmail("steve@gmail.com");
        user.setUsername("steve");
        user.setPassword(request.getPassword());

        when(userRepository.save(any(user.class))).thenReturn(null);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        userResponseDto response = userService.createUserDto(request);



        assertEquals("steve",response.getUsername());
        assertEquals("steve@gmail.com",response.getEmail());
        assertTrue(user.getRole().contains(Role.customer));

        verify(userRepository).save(any(user.class));




    }
  @Test
  void getUsers() {
     user admin = new user();
     admin.setId(1L);

     admin.setEmail("admin@gmail.com");
      admin.setUsername("admin");
     admin.setRole(new HashSet<>(Set.of(Role.admin)));

     when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

     when(userRepository.findAll()).thenReturn(List.of(admin));

     List<userResponseDto> response = userService.getUsers(1L);

     assertEquals(1,response.size());
     assertEquals("admin",response.get(0).getUsername());

     verify(userRepository).findById(1L);
     verify(userRepository).findAll();



   }

    @Test
    void getUserById() {
        user user = new user();
        user.setId(1L);user.setRole(new HashSet<>(Set.of(Role.customer)));
        user.setEmail("stephen@gmail.com");
        user.setUsername("steve");
        user.setPassword("stevenhello");
      //  user.setRole(new HashSet<>(Set.of(Role.customer)));



        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userResponseDto request = userService.getUserById(1L);


        assertEquals("steve",request.getUsername());
        assertEquals("stephen@gmail.com",request.getEmail());
        assertTrue(request.getRoles().contains(Role.customer));
        verify(userRepository).findById(1L);


    }

    @Test
    void updateUser() {
      user user = new user();
      user.setId(1L);
      user.setUsername("steve");
      user.setEmail("steve@gmail.com");
      user.setPassword("OldPassword");


      when(userRepository.findById(1L)).thenReturn(Optional.of(user));


     updateUserRequestDto request = new updateUserRequestDto();
     request.setUsername("senyo");
     request.setPassword("newPassword");

     when(passwordEncoder.encode(request.getPassword())).thenReturn("newPassword");

     userResponseDto response = userService.updateUser(1L,request);

     assertEquals("senyo",response.getUsername());

     verify(passwordEncoder).encode("newPassword");
     verify(userRepository).save(user);


    }

    @Test
    void deleteUSer() {
        user user = new user();
        user.setUsername("steve");
        user.setEmail("steve@gmail.com");
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

          userService.deleteUser(1L);

          verify(userRepository).findById(1L);
          verify(userRepository).delete(user);


    }
    @Test
    void createUserWithExistingEmail(){
        user user = new user();
        user.setEmail("steve@gmail.com");
        user.setId(1L);
        user.setUsername("steve");

        userRegistrationDto register = new userRegistrationDto();
        register.setEmail(user.getEmail());
        register.setUsername(user.getUsername());

        when(userRepository.findByEmail(register.getEmail())).thenReturn(Optional.of(user));





        assertThrows(DuplicateEmailException.class, () -> userService.createUserDto(register));



    }

    @Test
    void createWithExistingUsername (){
        user user = new user();
        user.setUsername("steve");
        user.setId(1L);
        user.setEmail("steve@gmail.com");

        userRegistrationDto request = new userRegistrationDto();
        request.setUsername(user.getUsername());
        request.setEmail(user.getEmail());

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        assertThrows(DuplicateUsernameException.class,() -> userService.createUserDto(request));
    }

    @Test
    void getUserByIdNotFound (){

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                ()-> userService.getUserById(1L));
    }


    @Test
    void getUsersWhenIdNotAdmin(){
        user customer = new user();
        customer.setRole(new HashSet<>(Set.of(Role.customer)));
        customer.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(UnauthorizedActionException.class,
                () -> userService.getUsers(customer.getId()));
    }


    @Test
    void updateUserNotFound (){
        updateUserRequestDto request = new updateUserRequestDto();
        request.setPassword("hashpassword");
        request.setUsername("steve");
        Long id =1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                ()-> userService.updateUser(id,request));
    }

    @Test
    void deleteUserNotFound(){
         Long id=1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                ()-> userService.deleteUser(id));

    }

    @Test
    void getUsersUserNotFound(){
        Long id =1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                ()-> userService.getUsers(id));
    }

    @Test
    void updateUserPassword(){
        user user = new user();
        user.setPassword("oldPassword");
        user.setUsername("steve");
        user.setEmail("steve@gmail.com");
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));


        updateUserRequestDto request = new updateUserRequestDto();

        request.setPassword("newPassword");
        request.setUsername("stephen");

        when(passwordEncoder.matches(request.getPassword(),user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("EncodedPassword");
        userResponseDto response = userService.updateUser(user.getId(),request);


        ArgumentCaptor<user> userCaptor = ArgumentCaptor.forClass(user.class);
        verify(userRepository).save(userCaptor.capture());

        user capturedUser = userCaptor.getValue();

        assertEquals("EncodedPassword",capturedUser.getPassword());


    }



}