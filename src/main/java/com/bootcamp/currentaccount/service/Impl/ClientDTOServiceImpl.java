package com.bootcamp.currentaccount.service.Impl;

import com.bootcamp.currentaccount.models.dto.Client;
import com.bootcamp.currentaccount.service.ClientDTOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;

@Service
public class ClientDTOServiceImpl implements ClientDTOService {

  @Autowired
  private WebClient webClient;

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientDTOServiceImpl.class);

  @Override
  public Mono<Client> getClient(String clientIdNumber) {
    return webClient.get()
          .uri("/findClientCredit/" + clientIdNumber)
          .accept(MediaType.APPLICATION_JSON)
          .exchangeToMono(clientResponse->clientResponse.bodyToMono(Client.class))
          .doOnNext(c -> LOGGER.info("Client response : {}", c.getName()));
  }

  @Override
  public Mono<Client> newPan(String id ,Client client) {
    return webClient.put()
          .uri("/cards/{id}" + Collections.singletonMap("id",id))
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(client)
          .retrieve()
          .bodyToMono(Client.class)
          .doOnNext(c -> LOGGER.info("Client response : {}", c.getName()));
  }
}
