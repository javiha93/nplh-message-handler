package org.example;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Host {
    LIS("127.0.0.1", 55550);

    private final String ip;
    private final int port;

}


