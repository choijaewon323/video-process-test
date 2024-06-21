package com.swmaestro.shorts.controller;

import com.swmaestro.shorts.dto.VideoProcessRequestDto;
import com.swmaestro.shorts.dto.VideoProcessResponseDto;
import com.swmaestro.shorts.dto.VideoStateResponseDto;
import com.swmaestro.shorts.service.ShortsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ShortsApiController {
    private final ShortsService shortsService;

    public ShortsApiController(ShortsService shortsService) {
        this.shortsService = shortsService;
    }

    @PostMapping("/video/v1/shorts")
    public ResponseEntity<VideoProcessResponseDto> newTask(@ModelAttribute VideoProcessRequestDto requestDto) throws Exception {
        log.info("URL : /video/v1/shorts, METHOD : POST");
        final VideoProcessResponseDto result = shortsService.enqueue(requestDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/video/v1/shorts/{id}")
    public ResponseEntity<VideoStateResponseDto> getProcessState(@PathVariable Long id) {
        log.info("URL : /video/v1/shorts/{}, METHOD : GET", id);
        final VideoStateResponseDto result = shortsService.getState(id);
        return ResponseEntity.ok(result);
    }
}
