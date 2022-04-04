package com.bootcamp.currentaccount.service;

import com.bootcamp.currentaccount.models.dto.Client;
import reactor.core.publisher.Mono;

public interface ClientDTOService {

  /**
   * Find by client by clientIdNumber mono.
   *
   * @param clientIdNumber the client identity number
   * @return the mono
   */
  Mono<Client> getClient(String clientIdNumber);

  /**
   * Find by client by clientIdNumber mono.
   *
   * @param id client id
   * @param clientDto the client identity number
   * @return the mono
   */
  Mono<Client> newPan(String id , Client clientDto);
}
