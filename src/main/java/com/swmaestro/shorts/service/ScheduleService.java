package com.swmaestro.shorts.service;

public interface ScheduleService {
    void process() throws Exception;
    void setState(long id, String to);
}
