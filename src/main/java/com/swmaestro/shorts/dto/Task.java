package com.swmaestro.shorts.dto;

import com.swmaestro.shorts.domain.FileSet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Task {
    private long id;
    private VideoProcessRequestDto request;
    private FileSet fileSet;

    @Builder
    public Task(long id, VideoProcessRequestDto request, FileSet fileSet) {
        this.id = id;
        this.request = request;
        this.fileSet = fileSet;
    }
}
