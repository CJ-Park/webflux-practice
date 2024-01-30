package com.example.webflux;

import jakarta.annotation.Generated;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class User {
    @Id
    private Long id;
    private String ocid;
    private String nickname;
    private int lev;
    private int ranking;
    private String guild;
    private String className;
}
