package com.bootcamp.currentaccount.service.Impl;

import com.bootcamp.currentaccount.models.bean.Account;
import com.bootcamp.currentaccount.repository.AccountRepository;
import com.bootcamp.currentaccount.service.AccountService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Service
public class AccountServiceImpl implements AccountService {

  @Autowired
  private AccountRepository accountRepository;

  @Override
  public Mono<Account> create(Account obj) {
    return accountRepository.save(obj);
  }

  @Override
  public Flux<Account> findAll() {
    return accountRepository.findAll();
  }

  @Override
  public Mono<Account> findById(String s) {
    return accountRepository.findById(s);
  }

  @Override
  public Mono<Account> update(Account obj) {
    return accountRepository.save(obj);
  }

  @Override
  public Mono<Void> delete(String id) {
    return accountRepository.deleteById(id);
  }

  @Override
  public Mono<Account> validateClientIdNumber(String clientIdNumber) {
    return accountRepository.findByClientIdNumber(clientIdNumber)
          .switchIfEmpty(Mono.just(Account.builder()
                .clientIdNumber(null).build()));
  }

  @Override
  public Flux<Account> findAllByClientIdNumber(String clientIdNumber) {
    return accountRepository.findAllByClientIdNumber(clientIdNumber);
  }

  @Override
  public Mono<Account> findByClientIdNumber(String clientIdNumber) {
    return accountRepository.findByClientIdNumber(clientIdNumber);
  }

  @Override
  public Mono<Account> findByAccountNumber(String accountNumber) {
    return accountRepository.findByAccountNumber(accountNumber);
  }
}
