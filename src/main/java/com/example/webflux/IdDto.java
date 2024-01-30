package com.example.webflux;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IdDto {
    private String ocid;

    public IdDto(String id) {
        this.ocid = id;
    }
}
