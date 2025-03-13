package com.alltogether.lvupbackend.user.dto;



import com.alltogether.lvupbackend.user.domain.Interest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestResponseDto {
    private Integer interest_id;
    private String name;

    public static InterestResponseDto from(Interest interest) {
        return InterestResponseDto.builder()
                .interest_id(interest.getInterestId())
                .name(interest.getName())
                .build();
    }
}
