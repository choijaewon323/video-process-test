package com.swmaestro.shorts.domain;

import com.swmaestro.shorts.dto.Task;
import com.swmaestro.shorts.dto.VideoProcessRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static com.swmaestro.shorts.domain.TaskQueue.QUEUE;
import static org.assertj.core.api.Assertions.assertThat;

public class TaskQueueTests {
    @DisplayName("queue 삽입 및 삭제 테스트")
    @Test
    void pushAndPopTest() throws Exception {
        // given
        final Long id = 0l;
        final MultipartFile subtitle = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8));
        final VideoProcessRequestDto request = VideoProcessRequestDto.builder()
                .start("start")
                .subtitleFile(subtitle)
                .end("end")
                .keyName("keyName")
                .build();
        final FileSet fileSet = new FileSet(0L, "mp4");

        // when
        QUEUE.pushTask(id, fileSet, request);
        final Task result = QUEUE.popFirst();

        // then
        assertThat(result.getRequest().getStart()).isEqualTo(request.getStart());
        assertThat(result.getId()).isEqualTo(id);
    }
}
