package com.swmaestro.shorts.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CommandInfo {
    private String inputPath;
    private String subtitlePath;
    private String outputPath;
    private String start;
    private String end;

    @Builder
    public CommandInfo(String inputPath, String subtitlePath, String outputPath, String start, String end) {
        this.inputPath = inputPath;
        this.subtitlePath = subtitlePath;
        this.outputPath = outputPath;
        this.start = start;
        this.end = end;
    }
}
