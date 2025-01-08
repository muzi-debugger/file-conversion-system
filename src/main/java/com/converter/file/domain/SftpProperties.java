package com.converter.file.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sftp")
public class SftpProperties {

    private String host;
    private int port;
    private String user;
    private String password;
    private String uploadsDirectory;

    // Getters and setters
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUploadsDirectory() {
        return uploadsDirectory;
    }

    public void setUploadsDirectory(String uploadsDirectory) {
        this.uploadsDirectory = uploadsDirectory;
    }
}
