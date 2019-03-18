package com.hakan.example.amazonconsumera.process;

import com.hakan.example.amazonconsumera.model.ProcessMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class ProcessRunner implements Runnable {

    private final ProcessMessage processMessage;

    public ProcessRunner(ProcessMessage processMessage) {
        this.processMessage = processMessage;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(new Random().nextInt(20 * 1000));
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();

        }

    }
}
