package com.swmaestro.shorts.domain;

import com.swmaestro.shorts.dto.CommandInfo;
import com.swmaestro.shorts.dto.Task;
import com.swmaestro.shorts.dto.VideoProcessRequestDto;
import com.swmaestro.shorts.exception.VideoProcessFailException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class VideoProcessor {
    private final static int SUCCESS = 0;

    private final CommandFactory commandFactory;

    public VideoProcessor(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public void process(final Task task) throws Exception {
        final FileSet fileSet = task.getFileSet();
        final VideoProcessRequestDto request = task.getRequest();

        final List<String> command = commandFactory.makeCommand(CommandInfo.builder()
                .inputPath(fileSet.getInputPath())
                .subtitlePath(fileSet.getSubtitlePath())
                .outputPath(fileSet.getOutputPath())
                .start(request.getStart())
                .end(request.getEnd())
                .build());

        final ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        final Process process = processBuilder.start();
        readFromProcess(process);

        final int exitCode = process.waitFor();
        if (exitCode != SUCCESS) {
            throw new VideoProcessFailException("동영상 처리에 실패했습니다. 에러 코드 : " + exitCode);
        }
    }

    private void readFromProcess(final Process process) throws Exception {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while (reader.readLine() != null) {}
    }
}
