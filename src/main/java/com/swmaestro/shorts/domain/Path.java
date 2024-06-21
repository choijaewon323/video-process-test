package com.swmaestro.shorts.domain;

/*
2024-06-21: 곧 삭제될 예정
 */
@Deprecated
public enum Path {
    ORIGIN_PATH("C:\\Users\\casdf\\Desktop\\video-test-path"),
    TEMP_PATH("C:\\Users\\casdf\\Desktop\\video-test-path\\tmp")
    ;

    private final String path;

    Path(String path) {
        this.path = path;
    }

    public String get() {
        return path;
    }
}
