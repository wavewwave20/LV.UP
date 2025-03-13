package com.alltogether.lvupbackend.user.dto;

import com.alltogether.lvupbackend.user.domain.Avatar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvatarResponseDto {
    private Integer avatarId;
    private String name;
    private String avatarImagePath;
    private String description;
    private Boolean enabled;
    private Character gender;

    public static AvatarResponseDto from(Avatar avatar) {
        return AvatarResponseDto.builder()
                .avatarId(avatar.getAvatarId())
                .name(avatar.getName())
                .avatarImagePath(avatar.getAvatarImagePath())
                .description(avatar.getDescription())
                .enabled(avatar.getEnabled())
                .gender(avatar.getGender())
                .build();
    }
}