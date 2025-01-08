package com.converter.file.service.sftp;

import com.converter.file.domain.SftpProperties;
import com.jcraft.jsch.*;
import jakarta.annotation.PostConstruct;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SftpFileMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SftpFileMonitor.class);

    private final SftpProperties sftpProperties;

    public SftpFileMonitor(SftpProperties sftpProperties) {
        this.sftpProperties = sftpProperties;
    }

    @PostConstruct
    public void monitorUploadsDirectory() {
        new Thread(() -> {
            LOGGER.info("Initializing SFTP file monitoring");
            Session session = null;
            ChannelSftp channelSftp = null;

            try {
                // Create a new instance of JSch and configure the session
                JSch jsch = new JSch();
                session = jsch.getSession(sftpProperties.getUser(), sftpProperties.getHost(), sftpProperties.getPort());
                session.setPassword(sftpProperties.getPassword());

                // Disable strict host key checking
                // TODO: change to use a private key and not username and password
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                LOGGER.info("Connected to SFTP server");

                // Open an SFTP channel and connect
                channelSftp = (ChannelSftp) session.openChannel("sftp");
                channelSftp.connect();

                LOGGER.info("SFTP channel opened");
                LOGGER.info("Monitoring directory: " + sftpProperties.getUploadsDirectory());

                // Poll the SFTP directory for files
                while (true) {
                    @SuppressWarnings("unchecked")
                    Vector<ChannelSftp.LsEntry> fileList = channelSftp.ls(sftpProperties.getUploadsDirectory());

                    // Process each file in the directory
                    for (ChannelSftp.LsEntry entry : fileList) {
                        String fileName = entry.getFilename();
                        if (fileName.equals(".") || fileName.equals("..")) {
                            continue;
                        }

                        // Check if the file is a supported type (docx)
                        if (fileName.endsWith(".docx")) {
                            LOGGER.info("Found supported file: " + fileName);
                        } else {
                            LOGGER.warn("Unsupported file type: " + fileName + ". The System only supports docx file types");
                        }
                    }

                    // Poll every 20 seconds
                    Thread.sleep(20000);
                }
            } catch (Exception e) {
                LOGGER.error("Error monitoring SFTP directory: {}", e.getMessage());
            } finally {
                // Clean up and disconnect from the SFTP server
                if (channelSftp != null && channelSftp.isConnected()) {
                    channelSftp.disconnect();
                }
                if (session != null && session.isConnected()) {
                    session.disconnect();
                }
            }
        }).start();
    }
}
