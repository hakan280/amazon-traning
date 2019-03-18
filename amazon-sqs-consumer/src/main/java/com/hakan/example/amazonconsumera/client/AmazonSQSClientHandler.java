package com.hakan.example.amazonconsumera.client;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakan.example.amazonconsumera.process.ProcessHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AmazonSQSClientHandler {

    @Value("${amazon.sqs.url}")
    private String amazonSQSQueueUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private ProcessHolder processHolder;


    public List<Message> consumeMessages() {
        return amazonSQS.receiveMessage(getReceiveMessageRequest()).getMessages();
    }

    public void deleteMessage(Message message) {
        amazonSQS.deleteMessage(amazonSQSQueueUrl, message.getReceiptHandle());
    }



    private ReceiveMessageRequest getReceiveMessageRequest() {

        ReceiveMessageRequest request = new ReceiveMessageRequest();

        log.info("Requested message size is -->" + processHolder.getMaxReceiveMessageCount());
        request.setMaxNumberOfMessages(processHolder.getMaxReceiveMessageCount());
        request.withWaitTimeSeconds(1);
        request.withQueueUrl(amazonSQSQueueUrl);
        return request;
    }
}
