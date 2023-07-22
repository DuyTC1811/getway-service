//package com.example.gatewayservice;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//@Component
//public class WebClientConfig {
//    @Bean
//    public WebClient getWebClient() {
//        return WebClient.builder()
//                .baseUrl("http://192.168.0.188:8888")
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//    }
//}
