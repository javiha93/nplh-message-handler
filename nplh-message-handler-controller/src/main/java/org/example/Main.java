package org.example;

import org.example.client.Clients;
import org.example.domain.host.HostInfo;
import org.example.domain.host.HostInfoList;
import org.example.server.Servers;
import org.example.service.IrisService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.example.service.IrisService.parseList;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty("jdk.httpclient.HttpClient.log", "requests,headers,errors");
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public HostInfoList hostInfoList(IrisService irisService) {
        return new HostInfoList(parseList(irisService.getHostInfo(), HostInfo.class));
    }

    @Bean
    public Clients clients(HostInfoList hostInfoList, IrisService irisService) {
        return new Clients(hostInfoList, irisService);
    }


    @Bean
    public Servers servers(HostInfoList hostInfoList, IrisService irisService, Clients clients) {
        return new Servers(hostInfoList, irisService, clients);
    }
}