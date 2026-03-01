package com.springdev.Service;

import com.springdev.CustomExceptions.AlreadySellerException;
import com.springdev.CustomExceptions.SellerRequestNotFoundException;
import com.springdev.CustomExceptions.UserNotFoundException;
import com.springdev.DTO.SellerRequestDTO;
import com.springdev.DTO.SellerRoleRequestResponseDTO;
import com.springdev.Entity.*;
import com.springdev.Repository.RoleRepository;
import com.springdev.Repository.SellerRepository;
import com.springdev.Repository.SellerRequestRepository;
import com.springdev.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SellerRequestService {

    private final SellerRequestRepository sellerRequestRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    public SellerRoleRequestResponseDTO sellerRoleRequest(
            long id,
            SellerRequestDTO sellerRequestDTO){
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean alreadySeller = user.getRoles().stream()
                        .anyMatch(r -> r.getName() == RoleName.SELLER);

        if(alreadySeller){
            throw new AlreadySellerException("You already have the SELLER role");
        }

        if(sellerRequestRepository.existsByUserIdAndStatus(user.getId(), SellerRequestStatus.PENDING)) {
            throw new IllegalStateException("You already have a pending seller role request");
        }

        SellerRequest request = SellerRequest.builder()
                .user(user)
                .shop_name(sellerRequestDTO.getShop_name())
                .message(sellerRequestDTO.getMessage())
                .status(SellerRequestStatus.PENDING)
                .business_type(sellerRequestDTO.getBusiness_type())
                .tax_id(sellerRequestDTO.getTax_id())
                .business_name(sellerRequestDTO.getBusiness_name())
                .business_address(sellerRequestDTO.getBusiness_address())
                .bank_account(sellerRequestDTO.getBank_account())
                .bank_name(sellerRequestDTO.getBank_name())
                .swift_code(sellerRequestDTO.getSwift_code())
                .createdAt(Instant.now())
                .submitted_at(LocalDateTime.now())
                .build();

        return toResponse(sellerRequestRepository.save(request));
    }

    public List<SellerRoleRequestResponseDTO> getPendingRequests(){
        return sellerRequestRepository.findByStatusOrderByCreatedAtDesc(SellerRequestStatus.PENDING)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public SellerRoleRequestResponseDTO approve(long requestId) throws RoleNotFoundException {
        SellerRequest request = sellerRequestRepository.findById(requestId)
                .orElseThrow(() -> new SellerRequestNotFoundException("Seller role request not found"));

        if(request.getStatus() != SellerRequestStatus.PENDING){
            throw new IllegalStateException("Request is not pending and cannot be approved");
        }

        request.setStatus(SellerRequestStatus.APPROVED);
        request.setReviewedAt(Instant.now());
        sellerRequestRepository.save(request);

        User user = request.getUser();
        Role sellerRole = roleRepository.findByName(RoleName.SELLER)
                .orElseThrow(() -> new RoleNotFoundException("Seller role not found"));

        user.getRoles().add(sellerRole);
        userRepository.save(user);

        Seller seller = Seller.builder()
                .user(user)
                .StoreName(request.getShop_name())
                .business_name(request.getBusiness_name())
                .business_type(request.getBusiness_type())
                .build();

        sellerRepository.save(seller);

        return toResponse(request);
    }

    public SellerRoleRequestResponseDTO reject(long requestId){
        SellerRequest request = sellerRequestRepository.findById(requestId)
                .orElseThrow(() -> new SellerRequestNotFoundException("Seller request not found"));

        if(request.getStatus() != SellerRequestStatus.PENDING){
            throw new IllegalStateException("Request is not pending and cannot be rejected");
        }

        request.setStatus(SellerRequestStatus.REJECTED);
        request.setReviewedAt(Instant.now());
        sellerRequestRepository.save(request);
        return toResponse(request);
    }

    private SellerRoleRequestResponseDTO toResponse (SellerRequest request){
        return SellerRoleRequestResponseDTO.builder()
                .id(request.getId())
                .userId(request.getUser().getId())
                .username(request.getUser().getUsername())
                .email(request.getUser().getEmail())
                .businessName(request.getBusiness_name())
                .message(request.getMessage())
                .status(request.getStatus().name())
                .createdAt(request.getCreatedAt())
                .build();


    }

}
