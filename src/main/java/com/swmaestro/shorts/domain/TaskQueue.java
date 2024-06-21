package com.swmaestro.shorts.domain;

import com.swmaestro.shorts.dto.Task;
import com.swmaestro.shorts.dto.VideoProcessRequestDto;
import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*
    TaskQueue : 스케줄링 작업을 위한 임시 큐 -> Kafka or RabbitMQ로 바꿀수도 있음
 */
public enum TaskQueue {
    QUEUE
    ;
    private final BlockingQueue<Task> queue;
    TaskQueue() {
        queue = new ArrayBlockingQueue<>(5000);
    }

    public void pushTask(final long id, final FileSet fileSet, final VideoProcessRequestDto request) throws Exception {
        queue.add(Task.builder()
                .id(id)
                .fileSet(fileSet)
                .request(request)
                .build()
        );
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Task popFirst() {
        final Task first = queue.poll();
        return first;
    }
}
