package com.alltogether.lvupbackend.smalltalk.user.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchRequestDto {
    private Integer userId;
    private Character gender;
    private List<String> interests;

    private Integer userAvatarId;

    private Integer modeId;
    private Character targetGender;

    private long timestamp;

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
    public void setUserAvatarId(Integer userAvatarId) {
        this.userAvatarId = userAvatarId;
    }

    public void setGender(Character gender) {this.gender = gender;}
}
