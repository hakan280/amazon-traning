package com.hakan.example.amazonconsumera.handler;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakan.example.amazonconsumera.client.AmazonSQSClientHandler;
import com.hakan.example.amazonconsumera.enums.ProcessStatus;
import com.hakan.example.amazonconsumera.model.ProcessMessage;
import com.hakan.example.amazonconsumera.model.ProcessWrapper;
import com.hakan.example.amazonconsumera.process.ProcessHolder;
import com.hakan.example.amazonconsumera.process.ProcessRunner;
import com.hakan.example.amazonconsumera.repository.ProcessRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class ProcessHandler {

    @Autowired
    private ProcessHolder processHolder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private AmazonSQSClientHandler sqsClientHandler;


    public void cleanCompletedProcess() {
        processHolder.clearCompletedProcess();
    }

    //https://www.callicoder.com/java-8-completablefuture-tutorial/
    public void runProcessAsync(Message message) {

        saveProcessMessageAndAddProcessHolder(message)
                .thenCompose(this::deleteMessageFromSQSQueue)
                .thenCompose(this::runProccessMessage)
                .thenAccept(this::updateProcessStatus)
                .handle((res, ex) -> {
                    if (ex != null) {
                        log.error(ex.getMessage(), ex);
                    }
                    return res;
                });
    }


    private Optional<ProcessMessage> getMessageBodyAsJavaObject(Message message) {
        try {
            return Optional.of(objectMapper.readValue(message.getBody(), ProcessMessage.class));
        } catch (IOException e) {
            log.error("Message cannot be converted java object", e);
            return Optional.empty();
        }
    }

    private CompletableFuture<ProcessWrapper> saveProcessMessageAndAddProcessHolder(Message message) {

        return CompletableFuture.supplyAsync(() -> getMessageBodyAsJavaObject(message).map(processMessage -> {
            checkProcessIsAlreadyInserted(processMessage);
            ProcessMessage savedProcess = processRepository.save(processMessage);
            processHolder.add(savedProcess);
            return new ProcessWrapper(savedProcess, message);
        }).orElseThrow(() -> new RuntimeException("Something went wrong while parsing message body as ProcessMessage object")));
    }


    private void checkProcessIsAlreadyInserted(ProcessMessage processMessage) {
        processRepository.findById(processMessage.getId())
                .ifPresent(m -> {
                    throw new RuntimeException("The process message has been already inserted");
                });
    }

    @Retryable(
            value = {Exception.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 5000)
    )
    private CompletableFuture<ProcessWrapper> deleteMessageFromSQSQueue(final ProcessWrapper processWrapper) {

        return CompletableFuture.supplyAsync(() -> {
            log.info("deleteMessageFromSQSQueue start");
            sqsClientHandler.deleteMessage(processWrapper.getMessage());
            log.info("deleteMessageFromSQSQueue finish");
            return processWrapper;
        });
    }


    private CompletableFuture<ProcessWrapper> runProccessMessage(ProcessWrapper processWrapper) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("runProccesMessage start");
            new ProcessRunner(processWrapper.getProcessMessage()).run();
            log.info("runProccesMessage finish");
            return processWrapper;
        });
    }

    @Retryable(
            value = {SQLException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 5000)
    )
    private void updateProcessStatus(ProcessWrapper processWrapper) {
        CompletableFuture.runAsync(() -> {
            log.info("updateProcessStatus start");
            ProcessMessage processMessage = processWrapper.getProcessMessage();
            processMessage.setProcessStatus(ProcessStatus.COMPLETED);
            processRepository.save(processWrapper.getProcessMessage());
            log.info("updateProcessStatus finish");

        });
    }

    public boolean isProcessHolderFull() {
        return processHolder.isContainerFull();
    }
}
