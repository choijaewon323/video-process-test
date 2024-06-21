package com.swmaestro.shorts.service;

import com.github.f4b6a3.tsid.TsidCreator;
import com.swmaestro.shorts.domain.FileSet;
import com.swmaestro.shorts.dto.*;
import com.swmaestro.shorts.exception.InvalidVideoFormatException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.concurrent.TimeUnit;

import static com.swmaestro.shorts.domain.Path.TEMP_PATH;
import static com.swmaestro.shorts.domain.ProcessState.BEFORE_PROCESSING;
import static com.swmaestro.shorts.domain.ProcessState.NOT_EXISTS;
import static com.swmaestro.shorts.domain.TaskQueue.QUEUE;

@Transactional
@Service
public class ShortsServiceImpl implements ShortsService {
    private final static int SUCCESS = 200;

    private final RedisTemplate<Long, String> redisTemplate;

    public ShortsServiceImpl(RedisTemplate<Long, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public VideoProcessResponseDto enqueue(final VideoProcessRequestDto requestDto) throws Exception {
        final long id = generateId();
        final FileSet fileSet = new FileSet(id, getExtension(requestDto.getKeyName()));
        fileSet.copyToSubtitle(requestDto.getSubtitleFile().getInputStream());

        QUEUE.pushTask(id, fileSet, requestDto);
        final ValueOperations<Long, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(id, BEFORE_PROCESSING.get());
        redisTemplate.expire(id, 7, TimeUnit.DAYS);

        return VideoProcessResponseDto.builder()
                .id(id)
                .statusCode(SUCCESS)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public VideoStateResponseDto getState(final long id) {
        if (!redisTemplate.hasKey(id)) {
            return VideoStateResponseDto.builder()
                    .id(id)
                    .state(NOT_EXISTS.get())
                    .build();
        }

        final ValueOperations<Long, String> valueOperations = redisTemplate.opsForValue();
        final String state = valueOperations.get(id);
        return VideoStateResponseDto.builder()
                .id(id)
                .state(state)
                .build();
    }

    private long generateId() {
        return TsidCreator.getTsid().toLong();
    }

    private String getExtension(final String keyName) {
        final int lastIndex = keyName.lastIndexOf('.');

        if (lastIndex == -1) {
            throw new InvalidVideoFormatException("올바른 비디오 형식이 아닙니다.");
        }

        return keyName.substring(lastIndex);
    }
}
