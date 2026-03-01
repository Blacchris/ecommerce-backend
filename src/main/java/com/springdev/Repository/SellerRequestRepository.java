package com.springdev.Repository;

import com.springdev.Entity.SellerRequest;
import com.springdev.Entity.SellerRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface SellerRequestRepository extends JpaRepository<SellerRequest, Long> {


    List<SellerRequest> findByStatusOrderByCreatedAtDesc(SellerRequestStatus status);

    boolean existsByUserIdAndStatus(long id, SellerRequestStatus status);
}
