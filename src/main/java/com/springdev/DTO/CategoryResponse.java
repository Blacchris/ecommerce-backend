package com.springdev.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CategoryResponse {

    private long id;
    private String name;
}
