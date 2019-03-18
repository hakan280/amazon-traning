package com.hakan.example.amazonconsumera.model;

import com.amazonaws.services.sqs.model.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProcessWrapper {
    private ProcessMessage processMessage;
    private Message message;
}
