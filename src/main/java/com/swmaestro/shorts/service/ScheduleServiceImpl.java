package com.swmaestro.shorts.service;

import com.swmaestro.shorts.domain.FileSet;
import com.swmaestro.shorts.domain.VideoProcessor;
import com.swmaestro.shorts.dto.Task;
import com.swmaestro.shorts.dto.VideoProcessRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

import static com.swmaestro.shorts.domain.ProcessState.DONE;
import static com.swmaestro.shorts.domain.ProcessState.PROCESSING;
import static com.swmaestro.shorts.domain.TaskQueue.QUEUE;

@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final RedisTemplate<Long, String> redisTemplate;
    private final VideoProcessor videoProcessor;
    private final S3Client s3Client;

    @Value("${cloud.aws.bucket-name}")
    private String bucketName;

    public ScheduleServiceImpl(RedisTemplate<Long, String> redisTemplate, VideoProcessor videoProcessor, S3Client s3Client) {
        this.redisTemplate = redisTemplate;
        this.videoProcessor = videoProcessor;
        this.s3Client = s3Client;
    }

    @Override
    @Scheduled(fixedDelay = 1000)
    public void process() throws Exception {
        if (QUEUE.isEmpty()) {
            return;
        }
        final Task present = QUEUE.popFirst();
        final FileSet fileSet = present.getFileSet();
        final VideoProcessRequestDto request = present.getRequest();

        log.info("get original video - id : {}", present.getId());
        getInputVideo(fileSet, request);

        setState(present.getId(), PROCESSING.get());
        log.info("Batch video processing start - id : {}", present.getId());
        videoProcessor.process(present);
        setState(present.getId(), DONE.get());
        log.info("Batch video processing done - id : {}", present.getId());

        log.info("post processed video - id : {}", present.getId());
        postOutputVideo(fileSet);

        present.getFileSet().deleteAll();
    }

    @Override
    @Transactional
    public void setState(final long id, final String to) {
        ValueOperations<Long, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(id, to);
    }

    private void getInputVideo(final FileSet fileSet, final VideoProcessRequestDto request) throws Exception {
        final String keyName = request.getKeyName();

        final GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();
        final ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(objectRequest);
        final InputStream in = responseBytes.asInputStream();
        fileSet.copyToInput(in);
    }

    private void postOutputVideo(final FileSet fileSet) {
        final String outputKeyName = makeOutputKeyName(fileSet.getOutputName());

        final PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(outputKeyName)
                .build();
        s3Client.putObject(objectRequest, RequestBody.fromFile(Path.of(fileSet.getOutputPath())));
    }

    private String makeOutputKeyName(final String outputName) {
        return "process/" + outputName;
    }
}
