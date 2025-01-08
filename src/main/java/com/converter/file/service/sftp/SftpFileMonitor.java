package com.converter.file.service.sftp;

import com.converter.file.domain.SftpProperties;
import com.converter.file.service.DocxToPdfService;
import com.jcraft.jsch.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SftpFileMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SftpFileMonitor.class);

    private final SftpProperties sftpProperties;
    private final DocxToPdfService docxToPdfService;

    public SftpFileMonitor(SftpProperties sftpProperties, DocxToPdfService docxToPdfService) {
        this.sftpProperties = sftpProperties;
        this.docxToPdfService = docxToPdfService;
    }

    // Scheduled method to monitor the uploads directory
    @Scheduled(fixedDelay = 20000) // Run every 20 seconds
    public void monitorUploadsDirectory() {
        LOGGER.info("Polling SFTP directory for new files...");
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            // Set up the SFTP session
            JSch jsch = new JSch();
            session = jsch.getSession(sftpProperties.getUser(), sftpProperties.getHost(), sftpProperties.getPort());
            session.setPassword(sftpProperties.getPassword());

            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            LOGGER.info("Connected to SFTP server");

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            @SuppressWarnings("unchecked")
            Vector<ChannelSftp.LsEntry> fileList = channelSftp.ls(sftpProperties.getUploadsDirectory());

            for (ChannelSftp.LsEntry entry : fileList) {
                String fileName = entry.getFilename();

                // Ignore system entries
                if (fileName.equals(".") || fileName.equals("..")) {
                    continue;
                }

                // Check if the file is a supported type
                if (fileName.endsWith(".docx")) {
                    LOGGER.info("Found supported file: " + fileName);

                    // Download the file locally
                    File localFile = downloadFile(channelSftp, fileName);

                    // Trigger the transformation
                    docxToPdfService.transformToPdf(localFile);

                    // Delete the processed file from SFTP server
                    channelSftp.rm(sftpProperties.getUploadsDirectory() + "/" + fileName);
                    LOGGER.info("File processed and removed from SFTP server: " + fileName);
                } else {
                    LOGGER.warn("Unsupported file type: " + fileName);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error while monitoring SFTP directory: {}", e.getMessage());
        } finally {
            // Cleanup resources
            if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    private File downloadFile(ChannelSftp channelSftp, String fileName) throws SftpException {
        String remoteFilePath = sftpProperties.getUploadsDirectory() + "/" + fileName;
        File localFile = new File("src/main/resources/downloads/" + fileName); // Adjust path as needed

        try (OutputStream outputStream = new FileOutputStream(localFile)) {
            channelSftp.get(remoteFilePath, outputStream);
            LOGGER.info("File downloaded locally: " + localFile.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.error("Error downloading file: {}", e.getMessage());
        }
        return localFile;
    }
}
