package com.springdev.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerRequestDTO {

    private long user_id;
    private String shop_name;
    private String message;
    private String business_name;
    private String business_type;
    private String tax_id;
    private String business_address;
    private String bank_account;
    private String bank_name;
    private String swift_code;
}
