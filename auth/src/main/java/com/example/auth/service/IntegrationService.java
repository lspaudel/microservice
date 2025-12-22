package com.example.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IntegrationService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;

    public String hitApi(){
        String url="https://fake-json-api.mock.beeceptor.com/users";
        ResponseEntity<String> response= restTemplate.getForEntity(url, String.class);
        return  response.getBody();
    }

    public String hitApiv2(){
        String url="https://fake-json-api.mock.beeceptor.com/users";
        String response= webClient.get().uri(url).retrieve().bodyToMono(String.class).block();
        return  response;
    }
    /*MONO FLUX*/
}
