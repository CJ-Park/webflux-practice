package com.example.webflux;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankInfo {
    private String date;
    private int ranking;
    private String character_name;
    private String world_name;
    private String class_name;
    private String sub_class_name;
    private int character_level;
    private long character_exp;
    private int character_popularity;
    private String character_guildname;

    public User toEntity() {
        return new User.UserBuilder()
                .nickname(this.getCharacter_name())
                .lev(this.getCharacter_level())
                .ranking(this.getRanking())
                .guild(this.getCharacter_guildname())
                .className(this.getClass_name())
                .build();
    }
}
