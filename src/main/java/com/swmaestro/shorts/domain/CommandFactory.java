package com.swmaestro.shorts.domain;

import com.swmaestro.shorts.dto.CommandInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.swmaestro.shorts.domain.Path.ORIGIN_PATH;

@Component
public class CommandFactory {
    private String path = "";

    /*
    2024-06-21: 수정 필요 - .bat 파일은 윈도우 아니면 안 먹힘
     */
    public List<String> makeCommand(final CommandInfo info) {
        final List<String> command = new ArrayList<>();
        command.add(ORIGIN_PATH.get() + "\\video-process.bat");
        command.add(info.getInputPath());
        command.add(info.getSubtitlePath());
        command.add(info.getOutputPath());
        command.add(info.getStart());
        command.add(info.getEnd());
        return command;
    }
}
