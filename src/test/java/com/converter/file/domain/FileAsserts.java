package com.converter.file.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class FileAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFileAllPropertiesEquals(File expected, File actual) {
        assertFileAutoGeneratedPropertiesEquals(expected, actual);
        assertFileAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFileAllUpdatablePropertiesEquals(File expected, File actual) {
        assertFileUpdatableFieldsEquals(expected, actual);
        assertFileUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFileAutoGeneratedPropertiesEquals(File expected, File actual) {
        assertThat(expected)
            .as("Verify File auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFileUpdatableFieldsEquals(File expected, File actual) {
        assertThat(expected)
            .as("Verify File relevant properties")
            .satisfies(e -> assertThat(e.getFileName()).as("check fileName").isEqualTo(actual.getFileName()))
            .satisfies(e -> assertThat(e.getFileType()).as("check fileType").isEqualTo(actual.getFileType()))
            .satisfies(e -> assertThat(e.getLastModified()).as("check lastModified").isEqualTo(actual.getLastModified()))
            .satisfies(e -> assertThat(e.getConverted()).as("check converted").isEqualTo(actual.getConverted()))
            .satisfies(e -> assertThat(e.gets3Url()).as("check s3Url").isEqualTo(actual.gets3Url()))
            .satisfies(e -> assertThat(e.getCategory()).as("check category").isEqualTo(actual.getCategory()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFileUpdatableRelationshipsEquals(File expected, File actual) {
        // empty method
    }
}
