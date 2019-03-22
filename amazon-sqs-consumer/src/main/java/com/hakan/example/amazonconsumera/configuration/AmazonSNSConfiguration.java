package com.hakan.example.amazonconsumera.configuration;


import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AmazonSNSConfiguration {

    @Value("${amazon.region}")
    private String region;

    @Value("${amazon.access.key}")
    private String accessKey;

    @Value("${amazon.secret.key}")
    private String secretKey;


    @Profile("local")
    @Bean
    public AmazonSNS amazonSNSClientLocal() {
        AmazonSNS amazonSNS = AmazonSNSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("localhost:9911", region))
                .withClientConfiguration(new ClientConfiguration().withProtocol(Protocol.HTTP))
                .build();

        return amazonSNS;
    }

    @Bean
    @Profile("cloud")
    public AmazonSNS amazonSNSClientCloud() {
        AmazonSNS amazonSNS = AmazonSNSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withRegion(region)
                .build();

        return amazonSNS;
    }
}
