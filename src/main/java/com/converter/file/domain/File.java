package com.converter.file.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A File.
 */
@Entity
@Table(name = "file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @NotNull
    @Column(name = "file_type", nullable = false)
    private String fileType;

    @NotNull
    @Column(name = "last_modified", nullable = false)
    private LocalDate lastModified;

    @Column(name = "converted")
    private Boolean converted;

    @Column(name = "s_3_url")
    private String s3Url;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public File id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public File fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return this.fileType;
    }

    public File fileType(String fileType) {
        this.setFileType(fileType);
        return this;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public File lastModified(LocalDate lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public Boolean getConverted() {
        return this.converted;
    }

    public File converted(Boolean converted) {
        this.setConverted(converted);
        return this;
    }

    public void setConverted(Boolean converted) {
        this.converted = converted;
    }

    public String gets3Url() {
        return this.s3Url;
    }

    public File s3Url(String s3Url) {
        this.sets3Url(s3Url);
        return this;
    }

    public void sets3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public String getCategory() {
        return this.category;
    }

    public File category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }
        return getId() != null && getId().equals(((File) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "File{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", converted='" + getConverted() + "'" +
            ", s3Url='" + gets3Url() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
