package com.swmaestro.shorts.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class VideoProcessResponseDto {
    private int statusCode;
    private long id;

    @Builder
    public VideoProcessResponseDto(int statusCode, long id) {
        this.statusCode = statusCode;
        this.id = id;
    }
}
