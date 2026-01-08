package com.springdev.Controller;


import com.springdev.Entity.User;
import com.springdev.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;

    @GetMapping(path = "/{id}")
    public User getUserById(@PathVariable long id){
        return userService.getUser(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user){
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
