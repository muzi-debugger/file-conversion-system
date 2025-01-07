package com.converter.file.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static File getFileSample1() {
        return new File().id(1L).fileName("fileName1").fileType("fileType1").s3Url("s3Url1").category("category1");
    }

    public static File getFileSample2() {
        return new File().id(2L).fileName("fileName2").fileType("fileType2").s3Url("s3Url2").category("category2");
    }

    public static File getFileRandomSampleGenerator() {
        return new File()
            .id(longCount.incrementAndGet())
            .fileName(UUID.randomUUID().toString())
            .fileType(UUID.randomUUID().toString())
            .s3Url(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString());
    }
}
