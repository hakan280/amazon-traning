package com.example.hakan.snssubscriber.controller;

import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationSubject;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatus;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationMessageMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationSubscriptionMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationUnsubscribeConfirmationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/process-topic")
public class SNSEndpointController {

    @NotificationUnsubscribeConfirmationMapping
    public void confirmUnsubscribeMessage(
            NotificationStatus notificationStatus) {
        System.out.println("Notification Status");
        notificationStatus.confirmSubscription();
    }

    @NotificationMessageMapping
    public void receiveNotification(@NotificationMessage String message,
                                    @NotificationSubject String subject) {
        System.out.println("We got new message!!!");
        System.out.println(message);
    }

    @NotificationSubscriptionMapping
    public void confirmSubscriptionMessage(
            NotificationStatus notificationStatus) {
        System.out.println("Notification Status");
        notificationStatus.confirmSubscription();
    }
}