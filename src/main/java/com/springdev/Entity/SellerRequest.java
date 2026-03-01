package com.springdev.Entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "sellerRequest")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private SellerRequestStatus status;

    private String shop_name;
    private String message;
    private String business_name;
    private String business_type;
    private String tax_id;
    private String business_address;
    private String bank_account;
    private String bank_name;
    private String swift_code;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @CreationTimestamp
    private LocalDateTime submitted_at;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public SellerRequest(User user, String shop_name,String message,String business_name ,String business_type, String tax_id, String business_address, String bank_account, String bank_name, String swift_code) {
        this.user = user;
        this.business_name = business_name;
        this.shop_name = shop_name;
        this.message = message;
        this.business_type = business_type;
        this.tax_id = tax_id;
        this.business_address = business_address;
        this.bank_account = bank_account;
        this.bank_name = bank_name;
        this.swift_code = swift_code;
    }
}
