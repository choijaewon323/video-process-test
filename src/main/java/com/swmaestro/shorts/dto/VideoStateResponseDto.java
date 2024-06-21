package com.swmaestro.shorts.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoStateResponseDto {
    private long id;
    private String state;

    @Builder
    public VideoStateResponseDto(long id, String state) {
        this.id = id;
        this.state = state;
    }
}
