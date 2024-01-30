package com.example.webflux;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InfoDto {
    private String date;
    private String world_name;
    private String character_gender;
    private String character_class;
    private String character_class_level;
    private int character_level;
    private long character_exp;
    private String character_exp_rate;
    private String character_guild_name;
    private String character_image;

//    {
//        "date": "2023-12-21T00:00+09:00",
//            "character_name": "string",
//            "world_name": "string",
//            "character_gender": "string",
//            "character_class": "string",
//            "character_class_level": "string",
//            "character_level": 0,
//            "character_exp": 0,
//            "character_exp_rate": "string",
//            "character_guild_name": "string",
//            "character_image": "string"
//    }
}
