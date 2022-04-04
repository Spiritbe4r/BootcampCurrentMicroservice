package com.bootcamp.currentaccount.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CrudService<T, ID> {

  /**
   * Create mono.
   *
   * @param obj the obj
   * @return the mono
   */
  Mono<T> create(T obj);

  /**
   * Find all flux.
   *
   * @return the flux
   */
  Flux<T> findAll();

  /**
   * Find by id mono.
   *
   * @param id the id
   * @return the mono
   */
  Mono<T> findById(ID id);

  /**
   * Update mono.
   *
   * @param obj the obj
   * @return the mono
   */
  Mono<T> update(T obj);

  /**
   * Delete mono.
   *
   * @param id the id
   * @return the mono
   */
  Mono<Void> delete(String id);
}
