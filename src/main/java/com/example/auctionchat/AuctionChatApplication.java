package com.example.auctionchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.example.auctionchat","com.example.modulecommon"})
@EnableFeignClients
public class AuctionChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionChatApplication.class, args);
    }

}
