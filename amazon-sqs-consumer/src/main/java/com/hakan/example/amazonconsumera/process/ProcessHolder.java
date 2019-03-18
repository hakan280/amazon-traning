package com.hakan.example.amazonconsumera.process;

import com.hakan.example.amazonconsumera.enums.ProcessStatus;
import com.hakan.example.amazonconsumera.model.ProcessMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ProcessHolder {

    private Set<ProcessMessage> processMessages = new HashSet<>();
    private int maxSetSize = 5;

    public boolean add(ProcessMessage message) {
        if (isContainerFull()) {
            log.warn("Message cannot be added, ");
            return false;
        }

        processMessages.add(message);
        return true;
    }

    public int getMaxReceiveMessageCount() {
        return maxSetSize - processMessages.size();
    }

    public boolean isContainerFull() {
        return (maxSetSize - processMessages.size() < 1);
    }


    public int getMaxSetSize() {
        return maxSetSize;
    }

    public void setMaxSetSize(int maxSetSize) {
        this.maxSetSize = maxSetSize;
    }


    public void clearCompletedProcess() {

        processMessages = processMessages.stream()
                .filter(message -> ProcessStatus.CREATED.equals(message.getProcessStatus()))
                .collect(Collectors.toSet());

        log.info("After clear set size is: " + processMessages.size());

    }
}
