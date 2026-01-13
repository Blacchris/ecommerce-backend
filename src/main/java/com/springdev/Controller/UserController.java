package com.springdev.Controller;


import com.springdev.DTO.UserResponseDTO;
import com.springdev.Entity.User;
import com.springdev.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

//@RestController marks a class as a REST API controller and automatically serializes return values to JSON for HTTP responses.
@RestController
//is a Spring MVC annotation used to map HTTP requests (URLs + methods) to a controller or controller method.
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;

    @GetMapping(path = "/{id}")
    public UserResponseDTO getUserById(
            //Extracts values from the URL path and passes them as method parameters.
            @PathVariable long id){
        return userService.getUser(id);
    }

    @PostMapping
    public UserResponseDTO createUser(
            //@RequestBody = Deserializes JSON/XML from HTTP request body into a Java object automatically.
            @RequestBody User user){
        return userService.createUser(user);
    }

    @PutMapping(path = "/{id}")
    public User updateUser(@PathVariable long id, @RequestBody User user){
        return userService.updateUserById(id, user);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUserById(@PathVariable long id){
        userService.deleteUserById(id);
    }


}
