package com.swmaestro.shorts.domain;

public enum ProcessState {
    PROCESSING("processing"),
    NOT_EXISTS("not exists"),
    BEFORE_PROCESSING("before processing"),
    DONE("done");

    private final String state;

    ProcessState(String state) {
        this.state = state;
    }

    public String get() {
        return state;
    }
}
