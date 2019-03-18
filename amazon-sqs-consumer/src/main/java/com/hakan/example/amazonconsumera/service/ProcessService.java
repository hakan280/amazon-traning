package com.hakan.example.amazonconsumera.service;

import com.amazonaws.services.sqs.model.Message;
import com.hakan.example.amazonconsumera.client.AmazonSQSClientHandler;
import com.hakan.example.amazonconsumera.handler.ProcessHandler;
import com.hakan.example.amazonconsumera.repository.ProcessRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProcessService {

    @Autowired
    private ProcessRepository repo;

    @Autowired
    private ProcessHandler processHandler;

    @Autowired
    private AmazonSQSClientHandler amazonSQSClient;

    public void consumeMessages() {

        if(processHandler.isProcessHolderFull()) {
            log.info("Active process list is full, waiting for completed or cancelled");
            return;
        }

        List<Message> messages = amazonSQSClient.consumeMessages();
        log.info("#Recieved Messages -> " + messages.size());

        messages.forEach(message -> processHandler.runProcessAsync(message));

    }

    public void cleanCompletedProcess() {
        processHandler.cleanCompletedProcess();
    }
}
