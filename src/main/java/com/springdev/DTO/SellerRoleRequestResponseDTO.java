package com.springdev.DTO;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SellerRoleRequestResponseDTO {
    private Long id;
    private Long userId;
    private String username;
    private String email;
    private String businessName;
    private String message;
    private String status;
    private Instant createdAt;
}
