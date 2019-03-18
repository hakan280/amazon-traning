package com.hakan.example.amazonproducer.model;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProcessMessage {
    private String id;
    private String message;
}
