package com.example.ecommerce.user.service;

import com.example.ecommerce.auth.AuthenticationService;
import com.example.ecommerce.auth.RegisterRequest;
import com.example.ecommerce.common.exception.DuplicateEmailException;
import com.example.ecommerce.common.exception.DuplicateUsernameException;
import com.example.ecommerce.common.exception.UnauthorizedActionException;
import com.example.ecommerce.common.exception.UserNotFoundException;
import com.example.ecommerce.user.dto.UpdateUserRequestDto;
import com.example.ecommerce.user.dto.UserResponseDto;
import com.example.ecommerce.user.entity.Role;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class userServiceTest {

    @Mock
  private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @InjectMocks
   private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;



    @Test
    void createUserDto() {
        var request = new RegisterRequest();
        request.setPassword("hash1234");
        request.setEmail("steve@gmail.com");
        request.setUsername("steve");

        User user = new User();
        user.setEmail("steve@gmail.com");
        user.setUsername("steve");
        user.setPassword(request.getPassword());

        when(userRepository.save(any(User.class))).thenReturn(null);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        var response = authenticationService.register(request);





        verify(userRepository).save(any(User.class));




    }
  @Test
  void getUsers() {
     User admin = new User();
     admin.setId(1L);

     admin.setEmail("admin@gmail.com");
      admin.setUsername("admin");
     admin.setRole(new HashSet<>(Set.of(Role.admin)));

     when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

     when(userRepository.findAll()).thenReturn(List.of(admin));

     List<UserResponseDto> response = userService.getUsers();


     assertEquals("admin",response.get(0).getUsername());

     verify(userRepository).findById(1L);
     verify(userRepository).findAll();



   }

    @Test
    void getUserById() {
        User user = new User();
        user.setId(1L);user.setRole(new HashSet<>(Set.of(Role.customer)));
        user.setEmail("stephen@gmail.com");
        user.setUsername("steve");
        user.setPassword("stevenhello");
      //  user.setRole(new HashSet<>(Set.of(Role.customer)));



        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDto request = userService.getUserById(1L);


        assertEquals("steve",request.getUsername());
        assertEquals("stephen@gmail.com",request.getEmail());
        assertTrue(request.getRoles().contains(Role.customer));
        verify(userRepository).findById(1L);


    }

    @Test
    void updateUser() {
      User user = new User();
      user.setId(1L);
      user.setUsername("steve");
      user.setEmail("steve@gmail.com");
      user.setPassword("OldPassword");


      when(userRepository.findById(1L)).thenReturn(Optional.of(user));


     var request = new UpdateUserRequestDto();
     request.setUsername("senyo");
     request.setPassword("newPassword");

     when(passwordEncoder.encode(request.getPassword())).thenReturn("newPassword");

     UserResponseDto response = userService.updateUser(1L,request);

     assertEquals("senyo",response.getUsername());

     verify(passwordEncoder).encode("newPassword");
     verify(userRepository).save(user);


    }

    @Test
    void deleteUSer() {
        User user = new User();
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
        User user = new User();
        user.setEmail("steve@gmail.com");
        user.setId(1L);
        user.setUsername("steve");

        var register = new RegisterRequest();
        register.setEmail(user.getEmail());
        register.setUsername(user.getUsername());

        when(userRepository.findByEmail(register.getEmail())).thenReturn(Optional.of(user));





        assertThrows(DuplicateEmailException.class, () -> authenticationService.register(register));



    }

    @Test
    void createWithExistingUsername (){
        User user = new User();
        user.setUsername("steve");
        user.setId(1L);
        user.setEmail("steve@gmail.com");

       var request = new RegisterRequest();
        request.setUsername(user.getUsername());
        request.setEmail(user.getEmail());

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        assertThrows(DuplicateUsernameException.class,() -> authenticationService.register(request));
    }

    @Test
    void getUserByIdNotFound (){

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                ()-> userService.getUserById(1L));
    }


    @Test
    void getUsersWhenIdNotAdmin(){
        User customer = new User();
        customer.setRole(new HashSet<>(Set.of(Role.customer)));
        customer.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(UnauthorizedActionException.class,
                () -> userService.getUsers());
    }


    @Test
    void updateUserNotFound (){
        UpdateUserRequestDto request = new UpdateUserRequestDto();
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
                ()-> userService.getUsers());
    }

    @Test
    void updateUserPassword(){
        User user = new User();
        user.setPassword("oldPassword");
        user.setUsername("steve");
        user.setEmail("steve@gmail.com");
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));


        UpdateUserRequestDto request = new UpdateUserRequestDto();

        request.setPassword("newPassword");
        request.setUsername("stephen");

        when(passwordEncoder.matches(request.getPassword(),user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("EncodedPassword");
        UserResponseDto response = userService.updateUser(user.getId(),request);


        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        assertEquals("EncodedPassword",capturedUser.getPassword());


    }



}