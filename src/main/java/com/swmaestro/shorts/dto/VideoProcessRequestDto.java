package com.swmaestro.shorts.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class VideoProcessRequestDto {
    private String keyName;
    private MultipartFile subtitleFile;
    private String start;
    private String end;

    @Builder
    public VideoProcessRequestDto(String keyName, MultipartFile subtitleFile, String start, String end) {
        this.keyName = keyName;
        this.subtitleFile = subtitleFile;
        this.start = start;
        this.end = end;
    }
}
