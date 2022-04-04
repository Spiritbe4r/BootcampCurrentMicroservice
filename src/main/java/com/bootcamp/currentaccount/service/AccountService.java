package com.bootcamp.currentaccount.service;

import com.bootcamp.currentaccount.models.bean.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService extends CrudService<Account,String> {

  /**
   * Find by clients identity number mono.
   *
   * @param clientIdNumber the client identity number
   * @return the mono
   */
  Flux<Account> findAllByClientIdNumber(String clientIdNumber);

  /**
   * Find by client identity number mono.
   *
   * @param clientIdNumber the client identity number
   * @return the mono
   */
  Mono<Account> findByClientIdNumber(String clientIdNumber);


  /**
   * Validate client identity number mono.
   *
   * @param clientIdNumber the client identity number
   * @return the mono
   */
  Mono<Account> validateClientIdNumber(String clientIdNumber);


  /**
   * Find by account number mono.
   *
   * @param accountNumber the account number
   * @return the mono
   */
  Mono<Account> findByAccountNumber(String accountNumber);
}
