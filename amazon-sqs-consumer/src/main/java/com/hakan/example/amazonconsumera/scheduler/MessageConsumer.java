package com.hakan.example.amazonconsumera.scheduler;


import com.hakan.example.amazonconsumera.client.AmazonSNSClientHandler;
import com.hakan.example.amazonconsumera.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageConsumer {

    @Autowired
    private ProcessService processService;

    @Autowired
    private AmazonSNSClientHandler amazonSNSClientHandler;

    @Scheduled(fixedDelay = 2000)
    public void consumeSQSMessages() {
       processService.consumeMessages();
    }

    @Scheduled(fixedDelay = 7000)
    public void clearCompeletedProcess() {
        log.info("ClearCompletedTask is working");
        processService.cleanCompletedProcess();

    }

}
