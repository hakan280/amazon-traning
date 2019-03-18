package com.hakan.example.amazonproducer.runner;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AmazonSQSCreateQueueRunner implements ApplicationRunner {

    @Autowired
    private AmazonSQS amazonSQSClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //http://localhost:9324/queue/process-queue

        CreateQueueRequest createStandardQueueRequest = new CreateQueueRequest("process-queue");
        String standardQueueUrl = amazonSQSClient.createQueue(createStandardQueueRequest).getQueueUrl();

        log.info("create quee worked for -> " + standardQueueUrl);
    }
}
