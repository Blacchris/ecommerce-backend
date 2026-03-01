package com.springdev.Controller;


import com.springdev.DTO.SellerRequestDTO;
import com.springdev.DTO.SellerRoleRequestResponseDTO;
import com.springdev.DTO.UserRequestDTO;
import com.springdev.DTO.UserResponseDTO;
import com.springdev.Entity.CustomUserDetails;
import com.springdev.Service.SellerRequestService;
import com.springdev.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

//@RestController marks a class as a REST API controller and automatically serializes return values to JSON for HTTP responses.
@RestController //Handles HTTP requests and responses for user-related operations.
//is a Spring MVC annotation used to map HTTP requests (URLs + methods) to a controller or controller method.
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

     private final UserService userService;
    private final SellerRequestService sellerRequestService;

    @GetMapping
    public ResponseEntity<UserResponseDTO> getUserById(
            @AuthenticationPrincipal CustomUserDetails principal
            //Extracts values from the URL path and passes them as method parameters.
    ){
        long user_id = principal.getUser().getId();
        return ResponseEntity.ok(userService.getUser(user_id));
    }

    @PostMapping(path = "/seller-request/{Id}")
    public ResponseEntity<SellerRoleRequestResponseDTO> sellerRoleRequest(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable long Id,
            @RequestBody SellerRequestDTO sellerRequestDTO
    ){
        long authenticatedUserId = principal != null ? principal.getUser().getId() : -1;
        log.info("sellerRoleRequest called by userId={} for targetId={}", authenticatedUserId, Id);
        if(authenticatedUserId != Id){
            // If there is a mismatch, don't reject — use the authenticated user's id and log a warning
            log.warn("Path id {} does not match authenticated user id {} - ignoring path id and using authenticated id.", Id, authenticatedUserId);
        }
        return ResponseEntity.ok(sellerRequestService.sellerRoleRequest(authenticatedUserId, sellerRequestDTO));
    }

    @PutMapping(path = "/update")
    public ResponseEntity<UserResponseDTO> updateUser(
            @AuthenticationPrincipal CustomUserDetails principal,
            //@RequestBody = Deserializes JSON/XML from HTTP request body into a Java object automatically.
            @RequestBody UserRequestDTO userRequestDTO){
        log.info("Update Endpoint was called");
        long user_id = principal.getUser().getId();
        return ResponseEntity.ok(userService.updateUser(user_id, userRequestDTO));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal CustomUserDetails principal
    ){
        long user_id = principal.getUser().getId();
        userService.deleteUser(user_id);
        return ResponseEntity.noContent().build();
    }


}
