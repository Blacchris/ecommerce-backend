package com.springdev.Controller.admin;

import com.springdev.DTO.*;
import com.springdev.Service.SellerRequestService;
import com.springdev.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/admin/user")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {


    final private UserService userService;
    final private SellerRequestService sellerRequestService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping(path = "/seller-requests")
    public ResponseEntity<List<SellerRoleRequestResponseDTO>> getPendingSellerRequests() {
        return ResponseEntity.status(HttpStatus.OK).body(sellerRequestService.getPendingRequests());
    }

    @PostMapping(path = "/seller-request/approve/{Id}")
    public ResponseEntity<SellerRoleRequestResponseDTO> ApproveSellerRequest(
             @PathVariable long Id
    ) throws RoleNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sellerRequestService.approve(Id));
    }

    @PostMapping(path = "/seller-request/reject/{Id}")
    public ResponseEntity<SellerRoleRequestResponseDTO> RejectSellerRequest(
            @PathVariable long Id
    ) {
        return ResponseEntity.ok(sellerRequestService.reject(Id));
    }
}
