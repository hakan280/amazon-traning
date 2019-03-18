package com.hakan.example.amazonconsumera.model;

import com.hakan.example.amazonconsumera.enums.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProcessMessage {

    @Id
    private String id;
    private String message;

    @Enumerated
    private ProcessStatus processStatus;


}
