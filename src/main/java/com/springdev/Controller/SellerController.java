package com.springdev.Controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/seller")
@PreAuthorize("hasRole('SELLER')")
public class SellerController {


}
