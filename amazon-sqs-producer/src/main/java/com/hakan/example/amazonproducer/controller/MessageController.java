package com.hakan.example.amazonproducer.controller;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakan.example.amazonproducer.model.ProcessMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/test")
public class MessageController {

    @Value("${amazon.sqs.queue.url}")
    private String amazonSQSQueueUrl;

    @Qualifier("sqs")
    @Autowired
    private AmazonSQS amazonSQSClient;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/message")
    public void sendSQLMessage(@RequestBody String message) {
        amazonSQSClient.sendMessage(getSendMessageRequestWithBody(message));
    }


    @GetMapping("/message")
    public String getSQLMessages() {

        List<Message> messageList = amazonSQSClient.receiveMessage(getReceiveMessageRequest()).getMessages();
        messageList.forEach(message -> {
                    try {
                        ProcessMessage processMessage = objectMapper.readValue(message.getBody(), ProcessMessage.class);
                        System.out.println(processMessage);
                        amazonSQSClient.deleteMessage(amazonSQSQueueUrl, message.getReceiptHandle());
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
        );

        return messageList.toString();
    }


    private SendMessageRequest getSendMessageRequestWithBody(String body) {

        ProcessMessage processMessage = new ProcessMessage(UUID.randomUUID().toString(), body);

        String jsonBody = null;
        try {
            jsonBody = objectMapper.writeValueAsString(processMessage);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        return new SendMessageRequest(amazonSQSQueueUrl, jsonBody);
    }


    private ReceiveMessageRequest getReceiveMessageRequest() {
        ReceiveMessageRequest request = new ReceiveMessageRequest();
        request.setMaxNumberOfMessages(3);
        request.withWaitTimeSeconds(3);
        request.withQueueUrl(amazonSQSQueueUrl);


        return request;
    }


}
