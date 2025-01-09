package com.converter.file.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.converter.file.domain.File;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqsService {

    private static final Logger log = LoggerFactory.getLogger(SqsService.class);

    private final AmazonSQS sqsClient;
    private final String queueUrl;

    public SqsService(
        @Value("${aws.sqs.endpoint}") String endpoint,
        @Value("${aws.sqs.region}") String region,
        @Value("${aws.sqs.credentials.access-key}") String accessKey,
        @Value("${aws.sqs.credentials.secret-key}") String secretKey,
        @Value("${aws.sqs.queue.file-queue}") String queueName
    ) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        this.sqsClient = AmazonSQSClientBuilder.standard()
            .withEndpointConfiguration(new AmazonSQSClientBuilder.EndpointConfiguration(endpoint, region))
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .build();

        this.queueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();
    }

    public void SendToSqsQueue(File fileEntity, byte[] pdfContent) {
        try {
            String base64Content = Base64.getEncoder().encodeToString(pdfContent);

            String message = String.format(
                "{ \"id\": %d, \"fileName\": \"%s\", \"fileType\": \"%s\", \"lastModified\": \"%s\", \"converted\": %b, \"s3Url\": \"%s\", \"category\": \"%s\", \"base64EncodedContent\": \"%s\" }",
                fileEntity.getId(),
                fileEntity.getFileName(),
                fileEntity.getFileType(),
                fileEntity.getLastModified(),
                fileEntity.getConverted(),
                fileEntity.gets3Url() == null ? "" : fileEntity.gets3Url(),
                fileEntity.getCategory(),
                base64Content
            );
            SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueUrl).withMessageBody(message);
            sqsClient.sendMessage(sendMessageRequest);
        } catch (Exception e) {
            log.error("Error sending message to sqs: {}", e.getMessage());
        }
    }
}
