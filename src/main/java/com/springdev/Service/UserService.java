package com.springdev.Service;


import com.springdev.Entity.User;
import com.springdev.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    final private UserRepository userRepository;

    final static String USER_NOT_FOUND = "Username with %s not found";

    public User getUser(long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format(USER_NOT_FOUND, id)));
    }

    public User createUser(User user){
        return userRepository.save(
                new User(
                        user.getUsername(),
                        user.getEmail(),
                        user.getPassword()
                )
        );
    }

    public User updateUserById(long id, User user){
        User update = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format(USER_NOT_FOUND, id)));
        update.setUsername(user.getUsername());
        update.setEmail(user.getEmail());
        update.setPassword(user.getPassword());
        return userRepository.save(update);
    }

    public void deleteUserById(long id){
        userRepository.deleteById(id);
    }



}
