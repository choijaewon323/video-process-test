package com.swmaestro.shorts.service;

import com.swmaestro.shorts.dto.VideoProcessRequestDto;
import com.swmaestro.shorts.dto.VideoProcessResponseDto;
import com.swmaestro.shorts.dto.VideoStateResponseDto;

public interface ShortsService {
    VideoProcessResponseDto enqueue(VideoProcessRequestDto requestDto) throws Exception;
    VideoStateResponseDto getState(long id);
}
