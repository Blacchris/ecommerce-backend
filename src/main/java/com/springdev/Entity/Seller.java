package com.springdev.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seller")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String StoreName;
    private String business_name;
    private String business_type;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;



    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> productList = new ArrayList<>();

    public Seller(User user, String storeName, String business_name, String business_type) {
        this.user = user;
        StoreName = storeName;
        this.business_name = business_name;
        this.business_type = business_type;
    }
}
