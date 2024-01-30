package com.example.webflux;

import lombok.Data;

import java.util.List;

@Data
public class InfoResponseDto {
    List<RankInfo> ranking;
}
