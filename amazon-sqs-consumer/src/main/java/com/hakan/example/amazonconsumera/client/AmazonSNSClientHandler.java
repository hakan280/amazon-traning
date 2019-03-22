package com.hakan.example.amazonconsumera.client;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class AmazonSNSClientHandler {


    @Autowired
    private AmazonSNS amazonSNS;

    @Value("${amazon.sns.arn}")
    private String amazonSNSArn;

    public PublishResult publishMessage(String message) {

        PublishRequest publishRequest = new PublishRequest();

        publishRequest.setTopicArn(amazonSNSArn);
        publishRequest.setMessage(message);

        publishRequest.setSubject("subject");

        return amazonSNS.publish(publishRequest);

    }




}
