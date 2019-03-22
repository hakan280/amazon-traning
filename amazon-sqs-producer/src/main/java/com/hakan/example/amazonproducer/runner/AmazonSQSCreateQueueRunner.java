package com.hakan.example.amazonproducer.runner;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("local")
@Configuration
@Slf4j
public class AmazonSQSCreateQueueRunner implements ApplicationRunner {

    @Autowired
    private AmazonSQS amazonSQSClient;

    @Value("${amazon.sqs.queue.name}")
    private String queueName;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //ex: http://localhost:9324/queue/process-queue
        //commented , quee will be created by elasticmq.conf
        CreateQueueRequest createStandardQueueRequest = new CreateQueueRequest(queueName);
        String standardQueueUrl = amazonSQSClient.createQueue(createStandardQueueRequest).getQueueUrl();

        log.info("create queue worked for -> " + standardQueueUrl);
    }
}
