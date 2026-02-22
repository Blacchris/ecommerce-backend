package com.springdev.Service;


import com.springdev.DTO.UserRequestDTO;
import com.springdev.DTO.UserResponseDTO;
import com.springdev.Entity.Role;
import com.springdev.Entity.RoleName;
import com.springdev.Entity.User;
import com.springdev.Repository.RoleRepository;
import com.springdev.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    final private UserRepository userRepository;
    final private RoleRepository roleRepository;

    final static String USER_NOT_FOUND = "Username with %s not found";

    public UserResponseDTO getUser(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format(USER_NOT_FOUND, id)));
        return fetchUser(user);
    }

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::fetchUser)
                .toList();
    }

//    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){
//        User user1 = new User(
//                userRequestDTO.getUsername(),
//                userRequestDTO.getEmail(),
//                userRequestDTO.getPassword()
//        );
//        Role role = roleRepository.findByName(RoleName.ADMIN)
//                        .orElseThrow(() -> new RuntimeException("Role not found"));
//        user1.getRoles().add(role);
//        return fetchUser(user1);
//    }

    public UserResponseDTO updateUser(long id, UserRequestDTO userRequestDTO){
        User update = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format(USER_NOT_FOUND, id)));
        update.setUsername(userRequestDTO.getUsername());
        update.setEmail(userRequestDTO.getEmail());
        update.setPassword(userRequestDTO.getPassword());
        return fetchUser(userRepository.save(update));
    }

    public void deleteUser(long id){
        userRepository.deleteById(id);
    }

    UserResponseDTO fetchUser(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                new HashSet<>(
                        user.getRoles()
                                .stream()
                                .map(role -> role.getName().name())
                                .collect(Collectors.toSet())
                )
        );
    }


}
